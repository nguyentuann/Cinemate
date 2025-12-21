package vn.tutorial.cinemate.presentation.streaming

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.webrtc.*
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.get
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PeerManager(
    private val movieId: String,
    private val signalingClient: SignalingClient,
    private val configManager: ConfigManager,
    private val cacheManager: CacheManager
) : EventEmitter<PeerManagerEvents>() {
    
    private val TAG = "Logging PeerManager"
    private val peers = ConcurrentHashMap<String, PeerInfo>()
    private val pendingRequests = ConcurrentHashMap<String, PendingSegmentRequest>()
    private val pendingPings = ConcurrentHashMap<String, PingRequest>()
    private val iceServers = AppConstants.WebRTC.ICE_SERVERS
    private var requestIdCounter = 0
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    private val recentOffers = ConcurrentHashMap<String, Long>()
    private val recentAnswers = ConcurrentHashMap<String, Long>()
    
    private val gson = Gson()
    
    data class PendingSegmentRequest(
        val segmentId: String,
        val qualityId: String,
        val onSuccess: (ByteArray) -> Unit,
        val onError: (Exception) -> Unit,
        val timeoutJob: Job,
        val startTime: Long,
        val chunks: MutableList<ByteArray> = mutableListOf(),
        var totalChunks: Int = 0,
        var receivedChunks: Int = 0
    )
    
    data class PingRequest(
        val onSuccess: () -> Unit,
        val onError: (Exception) -> Unit,
        val timeoutJob: Job,
        val startTime: Long
    )
    
    init {
        setupSignalingListeners()
    }
    
    private fun setupSignalingListeners() {
        signalingClient.on("rtcOffer") { data ->
            val message = data as? RtcOfferMessage ?: return@on
            scope.launch {
                handlePeerOffer(message.from, SessionDescription(SessionDescription.Type.OFFER, message.sdp))
            }
        }
        
        signalingClient.on("rtcAnswer") { data ->
            val message = data as? RtcAnswerMessage ?: return@on
            scope.launch {
                handlePeerAnswer(message.from, SessionDescription(SessionDescription.Type.ANSWER, message.sdp))
            }
        }
        
        signalingClient.on("iceCandidate") { data ->
            val message = data as? IceCandidateMessage ?: return@on
            scope.launch {
                handleIceCandidate(message.from, message.candidate)
            }
        }
    }
    
    suspend fun connectToPeer(peerId: String): PeerInfo {
        val existing = peers[peerId]
        if (existing != null && (existing.connectionState == ConnectionState.CONNECTED ||
                                 existing.connectionState == ConnectionState.CONNECTING)) {
            return existing
        }
        
        val config = configManager.getConfig()
        val activePeers = getActivePeerCount()
        if (activePeers >= config.maxActivePeers) {
            Log.d(TAG, "Max peers (${config.maxActivePeers}) reached, disconnecting lowest scored peer")
            disconnectLowestScoredPeer()
        }
        
        val peerConnection = createPeerConnection()
        val dataChannel = peerConnection.createDataChannel(
            AppConstants.WebRTC.DataChannel.NAME,
            DataChannel.Init().apply {
                ordered = AppConstants.WebRTC.DataChannel.ORDERED
                maxRetransmits = AppConstants.WebRTC.DataChannel.MAX_RETRANSMITS
            }
        )
        
        val peerInfo = PeerInfo(
            peerId = peerId,
            connectionState = ConnectionState.CONNECTING,
            dataChannel = dataChannel,
            peerConnection = peerConnection,
            score = PeerScoreDetails(
                overall = 0.5f,
                latency = 0.5f,
                uploadSpeed = 0.5f,
                reliability = 0.5f
            ),
            lastActive = System.currentTimeMillis(),
            metrics = PeerMetrics()
        )
        
        peers[peerId] = peerInfo
        
        setupPeerConnectionHandlers(peerInfo)
        setupDataChannelHandlers(peerInfo)
        
        try {
            val offer = suspendCoroutine<SessionDescription> { continuation ->
                peerConnection.createOffer(object : SdpObserver {
                    override fun onCreateSuccess(sdp: SessionDescription) {
                        continuation.resume(sdp)
                    }
                    override fun onCreateFailure(error: String) {
                        continuation.resumeWithException(Exception(error))
                    }
                    override fun onSetSuccess() {}
                    override fun onSetFailure(error: String) {}
                }, MediaConstraints())
            }
            
            suspendCoroutine<Unit> { continuation ->
                peerConnection.setLocalDescription(object : SdpObserver {
                    override fun onSetSuccess() {
                        continuation.resume(Unit)
                    }
                    override fun onSetFailure(error: String) {
                        continuation.resumeWithException(Exception(error))
                    }
                    override fun onCreateSuccess(p0: SessionDescription?) {}
                    override fun onCreateFailure(p0: String?) {}
                }, offer)
            }
            
            signalingClient.sendOffer(peerId, offer)
            Log.d(TAG, "Initiated connection to peer $peerId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create offer for $peerId", e)
            disconnectPeer(peerId)
            throw e
        }
        
        return peerInfo
    }
    
    private suspend fun handlePeerOffer(peerId: String, offer: SessionDescription) {
        try {
            val lastOfferTime = recentOffers[peerId]
            val now = System.currentTimeMillis()
            if (lastOfferTime != null && (now - lastOfferTime) < AppConstants.Timing.SIGNALING_DEBOUNCE) {
                Log.d(TAG, "Ignoring duplicate offer from $peerId")
                return
            }
            recentOffers[peerId] = now
            
            Log.d(TAG, "Received valid offer from $peerId")
            
            val existingPeer = peers[peerId]
            if (existingPeer != null) {
                Log.d(TAG, "Peer $peerId already exists, cleaning up old connection")
                disconnectPeer(peerId)
                delay(AppConstants.Timing.RECONNECT_CLEANUP_DELAY)
            }
            
            val config = configManager.getConfig()
            val activePeers = getActivePeerCount()
            if (activePeers >= config.maxActivePeers) {
                Log.d(TAG, "Max peers reached, rejecting offer from $peerId")
                return
            }
            
            val peerConnection = createPeerConnection()
            
            val peerInfo = PeerInfo(
                peerId = peerId,
                connectionState = ConnectionState.CONNECTING,
                peerConnection = peerConnection,
                score = PeerScoreDetails(
                    overall = 0.5f,
                    latency = 0.5f,
                    uploadSpeed = 0.5f,
                    reliability = 0.5f
                ),
                lastActive = System.currentTimeMillis(),
                metrics = PeerMetrics()
            )
            
            peers[peerId] = peerInfo
            setupPeerConnectionHandlers(peerInfo)
            
            peerConnection.setOnDataChannel { dataChannel ->
                peerInfo.dataChannel = dataChannel
                setupDataChannelHandlers(peerInfo)
            }
            
            suspendCoroutine<Unit> { continuation ->
                peerConnection.setRemoteDescription(object : SdpObserver {
                    override fun onSetSuccess() {
                        continuation.resume(Unit)
                    }
                    override fun onSetFailure(error: String) {
                        continuation.resumeWithException(Exception(error))
                    }
                    override fun onCreateSuccess(p0: SessionDescription?) {}
                    override fun onCreateFailure(p0: String?) {}
                }, offer)
            }
            
            val answer = suspendCoroutine<SessionDescription> { continuation ->
                peerConnection.createAnswer(object : SdpObserver {
                    override fun onCreateSuccess(sdp: SessionDescription) {
                        continuation.resume(sdp)
                    }
                    override fun onCreateFailure(error: String) {
                        continuation.resumeWithException(Exception(error))
                    }
                    override fun onSetSuccess() {}
                    override fun onSetFailure(error: String) {}
                }, MediaConstraints())
            }
            
            suspendCoroutine<Unit> { continuation ->
                peerConnection.setLocalDescription(object : SdpObserver {
                    override fun onSetSuccess() {
                        continuation.resume(Unit)
                    }
                    override fun onSetFailure(error: String) {
                        continuation.resumeWithException(Exception(error))
                    }
                    override fun onCreateSuccess(p0: SessionDescription?) {}
                    override fun onCreateFailure(p0: String?) {}
                }, answer)
            }
            
            signalingClient.sendAnswer(peerId, answer)
            Log.d(TAG, "Accepted connection from peer $peerId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to handle offer from $peerId", e)
            disconnectPeer(peerId)
        }
    }
    
    private suspend fun handlePeerAnswer(peerId: String, answer: SessionDescription) {
        val lastAnswerTime = recentAnswers[peerId]
        val now = System.currentTimeMillis()
        if (lastAnswerTime != null && (now - lastAnswerTime) < AppConstants.Timing.SIGNALING_DEBOUNCE) {
            Log.d(TAG, "Ignoring duplicate answer from $peerId")
            return
        }
        recentAnswers[peerId] = now
        
        val peer = peers[peerId] ?: run {
            Log.w(TAG, "Received answer for unknown peer $peerId")
            return
        }
        
        try {
            Log.d(TAG, "Received valid answer from $peerId")
            
            suspendCoroutine<Unit> { continuation ->
                peer.peerConnection?.setRemoteDescription(object : SdpObserver {
                    override fun onSetSuccess() {
                        continuation.resume(Unit)
                    }
                    override fun onSetFailure(error: String) {
                        continuation.resumeWithException(Exception(error))
                    }
                    override fun onCreateSuccess(p0: SessionDescription?) {}
                    override fun onCreateFailure(p0: String?) {}
                }, answer)
            }
            
            Log.d(TAG, "Set remote description for peer $peerId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to set remote description for $peerId", e)
            disconnectPeer(peerId)
        }
    }
    
    private fun handleIceCandidate(peerId: String, candidateData: Map<String, Any?>) {
        val peer = peers[peerId] ?: run {
            Log.w(TAG, "Received ICE candidate for unknown peer $peerId")
            return
        }
        
        try {
            val candidate = IceCandidate(
                candidateData["sdpMid"] as? String ?: "",
                (candidateData["sdpMLineIndex"] as? Double)?.toInt() ?: 0,
                candidateData["candidate"] as? String ?: ""
            )
            
            peer.peerConnection?.addIceCandidate(candidate)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add ICE candidate from $peerId", e)
        }
    }
    
    suspend fun fetchSegmentFromPeer(peerId: String, segment: SegmentMetadata): FetchResult {
        val peer = peers[peerId] ?: return FetchResult(
            success = false,
            source = FetchSource.PEER,
            latency = 0,
            error = Exception("Peer not found")
        )
        
        if (peer.connectionState != ConnectionState.CONNECTED || peer.dataChannel?.state() != DataChannel.State.OPEN) {
            return FetchResult(
                success = false,
                source = FetchSource.PEER,
                latency = 0,
                error = Exception("Peer not connected")
            )
        }
        
        val startTime = System.currentTimeMillis()
        val requestId = "req_${requestIdCounter++}_${System.currentTimeMillis()}"
        
        return try {
            val data = suspendCoroutine<ByteArray> { continuation ->
                val config = configManager.getConfig()
                
                val timeoutJob = scope.launch {
                    delay(config.fetchTimeout)
                    pendingRequests.remove(requestId)
                    continuation.resumeWithException(Exception("Peer fetch timeout"))
                }
                
                pendingRequests[requestId] = PendingSegmentRequest(
                    segmentId = segment.id,
                    qualityId = segment.qualityId,
                    onSuccess = { data ->
                        continuation.resume(data)
                    },
                    onError = { error ->
                        continuation.resumeWithException(error)
                    },
                    timeoutJob = timeoutJob,
                    startTime = startTime
                )
                
                val request = mapOf(
                    "type" to "request",
                    "requestId" to requestId,
                    "segmentId" to segment.id,
                    "qualityId" to segment.qualityId
                )
                
                val json = gson.toJson(request)
                val buffer = ByteBuffer.wrap(json.toByteArray())
                peer.dataChannel?.send(DataChannel.Buffer(buffer, false))
            }
            
            val latency = System.currentTimeMillis() - startTime
            
            peer.metrics.successCount++
            peer.metrics.bytesReceived += data.size
            updatePeerScore(peerId, latency, true)

            FetchResult(
                success = true,
                data = data,
                source = FetchSource.PEER,
                peerId = peerId,
                latency = latency
            )
        } catch (e: Exception) {
            val latency = System.currentTimeMillis() - startTime
            peer.metrics.failureCount++
            updatePeerScore(peerId, latency, false)

            FetchResult(
                success = false,
                source = FetchSource.PEER,
                peerId = peerId,
                latency = latency,
                error = e
            )
        }
    }
    
    fun updatePeerSegmentAvailability(peerId: String, segmentKeys: List<String>) {
        val peer = peers[peerId] ?: return
        peer.availableSegments.addAll(segmentKeys)
        peer.lastActive = System.currentTimeMillis()
    }
    
    fun getActivePeerCount(): Int {
        return peers.values.count { it.connectionState == ConnectionState.CONNECTED }
    }
    
    fun getBestPeersForSegment(segmentKey: String, count: Int): List<String> {
        return peers.values
            .filter { it.connectionState == ConnectionState.CONNECTED }
            .filter { it.availableSegments.contains(segmentKey) }
            .sortedByDescending { it.score.overall }
            .take(count)
            .map { it.peerId }
    }
    
    private fun disconnectPeer(peerId: String) {
        val peer = peers.remove(peerId) ?: return
        
        peer.dataChannel?.close()
        peer.peerConnection?.close()
        peer.connectionState = ConnectionState.DISCONNECTED
        
        emit("peerDisconnected", peerId)
        Log.d(TAG, "Disconnected peer $peerId")
    }
    
    private fun disconnectLowestScoredPeer() {
        val lowestScored = peers.values
            .filter { it.connectionState == ConnectionState.CONNECTED }
            .minByOrNull { it.score.overall }
        
        lowestScored?.let { disconnectPeer(it.peerId) }
    }
    
    private fun updatePeerScore(peerId: String, latency: Long, success: Boolean) {
        val peer = peers[peerId] ?: return
        
        val latencyScore = if (latency > 0) {
            1f - (latency.toFloat() / 5000f).coerceIn(0f, 1f)
        } else 0.5f
        
        val totalRequests = peer.metrics.successCount + peer.metrics.failureCount
        val reliabilityScore = if (totalRequests > 0) {
            peer.metrics.successCount.toFloat() / totalRequests
        } else 0.5f
        
        peer.score.latency = latencyScore
        peer.score.reliability = reliabilityScore
        peer.score.overall = (latencyScore + reliabilityScore) / 2f
        
        if (success) {
            val oldAvg = peer.metrics.avgLatency
            val count = peer.metrics.successCount
            peer.metrics.avgLatency = ((oldAvg * (count - 1)) + latency) / count
        }
        
        emit("peerScoreUpdated", peerId, peer.score.overall)
    }
    
    private fun createPeerConnection(): PeerConnection {
        val config = PeerConnection.RTCConfiguration(iceServers)
        
        return PeerConnectionFactory.builder()
            .createPeerConnectionFactory()
            .createPeerConnection(config, object : PeerConnection.Observer {
                override fun onIceCandidate(candidate: IceCandidate) {
                    // Will be set per connection
                }

                override fun onIceCandidatesRemoved(p0: Array<out IceCandidate?>?) {
                }

                override fun onDataChannel(dataChannel: DataChannel) {}
                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {}
                override fun onIceConnectionReceivingChange(p0: Boolean) {}
                override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState) {}
                override fun onAddStream(p0: MediaStream) {}
                override fun onSignalingChange(p0: PeerConnection.SignalingState) {}
                override fun onRemoveStream(p0: MediaStream) {}
                override fun onRenegotiationNeeded() {}
                override fun onAddTrack(p0: RtpReceiver, p1: Array<out MediaStream>) {}
            })!!
    }
    
    private fun setupPeerConnectionHandlers(peerInfo: PeerInfo) {
        val peer = peerInfo.peerConnection ?: return
        
        peer.setOnIceCandidate { candidate ->
            signalingClient.sendIceCandidate(peerInfo.peerId, candidate)
        }
        
        peer.setOnIceConnectionChange { state ->
            when (state) {
                PeerConnection.IceConnectionState.CONNECTED -> {
                    peerInfo.connectionState = ConnectionState.CONNECTED
                    emit("peerConnected", peerInfo.peerId)
                    Log.d(TAG, "Peer ${peerInfo.peerId} connected")
                }
                PeerConnection.IceConnectionState.DISCONNECTED,
                PeerConnection.IceConnectionState.FAILED -> {
                    disconnectPeer(peerInfo.peerId)
                }
                else -> {}
            }
        }
    }
    
    private fun setupDataChannelHandlers(peerInfo: PeerInfo) {
        val channel = peerInfo.dataChannel ?: return
        
        channel.registerObserver(object : DataChannel.Observer {
            override fun onMessage(buffer: DataChannel.Buffer) {
                handleDataChannelMessage(peerInfo.peerId, buffer)
            }
            
            override fun onBufferedAmountChange(amount: Long) {}
            override fun onStateChange() {
                Log.d(TAG, "DataChannel state: ${channel.state()} for ${peerInfo.peerId}")
            }
        })
    }
    
    private fun handleDataChannelMessage(peerId: String, buffer: DataChannel.Buffer) {
        try {
            val data = ByteArray(buffer.data.remaining())
            buffer.data.get(data)
            
            if (!buffer.binary) {
                val json = String(data)
                val message = gson.fromJson(json, Map::class.java)
                val type = message["type"] as? String
                
                when (type) {
                    "request" -> handleSegmentRequest(peerId, message)
                    "response" -> handleSegmentResponse(message, data)
                    "chunk" -> handleSegmentChunk(message, data)
                    "error" -> handleSegmentError(message)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling data channel message from $peerId", e)
        }
    }
    
    private fun handleSegmentRequest(peerId: String, message: Map<*, *>) {
        val requestId = message["requestId"] as? String ?: return
        val segmentId = message["segmentId"] as? String ?: return
        val qualityId = message["qualityId"] as? String ?: return
        
        val peer = peers[peerId] ?: return
        
        scope.launch {
            val segmentData = cacheManager.getSegment(movieId, qualityId, segmentId)
            
            if (segmentData != null) {
                sendSegmentResponse(peer, requestId, segmentData)
            } else {
                sendSegmentError(peer, requestId, "Segment not found")
            }
        }
    }
    
    private fun sendSegmentResponse(peer: PeerInfo, requestId: String, data: ByteArray) {
        val channel = peer.dataChannel ?: return
        
        val CHUNK_SIZE = 16384
        val totalChunks = (data.size + CHUNK_SIZE - 1) / CHUNK_SIZE
        
        for (i in 0 until totalChunks) {
            val start = i * CHUNK_SIZE
            val end = minOf(start + CHUNK_SIZE, data.size)
            val chunk = data.copyOfRange(start, end)
            
            val response = mapOf(
                "type" to "chunk",
                "requestId" to requestId,
                "chunkIndex" to i,
                "totalChunks" to totalChunks,
                "data" to chunk
            )
            
            val json = gson.toJson(response)
            val buffer = ByteBuffer.wrap(json.toByteArray())
            channel.send(DataChannel.Buffer(buffer, false))
        }
    }
    
    private fun handleSegmentResponse(message: Map<*, *>, data: ByteArray) {
        val requestId = message["requestId"] as? String ?: return
        val pending = pendingRequests[requestId] ?: return
        
        pending.timeoutJob.cancel()
        pending.onSuccess(data)
        pendingRequests.remove(requestId)
    }
    
    private fun handleSegmentChunk(message: Map<*, *>, fullData: ByteArray) {
        val requestId = message["requestId"] as? String ?: return
        val chunkIndex = (message["chunkIndex"] as? Double)?.toInt() ?: return
        val totalChunks = (message["totalChunks"] as? Double)?.toInt() ?: return
        
        val pending = pendingRequests[requestId] ?: return
        
        if (pending.totalChunks == 0) {
            pending.totalChunks = totalChunks
        }
        
        pending.chunks.add(fullData)
        pending.receivedChunks++
        
        if (pending.receivedChunks >= totalChunks) {
            val completeData = pending.chunks.reduce { acc, bytes -> acc + bytes }
            pending.timeoutJob.cancel()
            pending.onSuccess(completeData)
            pendingRequests.remove(requestId)
        }
    }
    
    private fun sendSegmentError(peer: PeerInfo, requestId: String, errorMsg: String) {
        val channel = peer.dataChannel ?: return
        
        val error = mapOf(
            "type" to "error",
            "requestId" to requestId,
            "error" to errorMsg
        )
        
        val json = gson.toJson(error)
        val buffer = ByteBuffer.wrap(json.toByteArray())
        channel.send(DataChannel.Buffer(buffer, false))
    }
    
    private fun handleSegmentError(message: Map<*, *>) {
        val requestId = message["requestId"] as? String ?: return
        val error = message["error"] as? String ?: "Unknown error"
        
        val pending = pendingRequests.remove(requestId) ?: return
        pending.timeoutJob.cancel()
        pending.onError(Exception(error))
    }
    
    fun clear() {
        peers.values.forEach { disconnectPeer(it.peerId) }
        peers.clear()
        pendingRequests.clear()
        scope.cancel()
        super.destroy()
    }
}

// Extension functions for PeerConnection
private fun PeerConnection.setOnIceCandidate(callback: (IceCandidate) -> Unit) {
    // This would need to be set during creation with proper observer
}

private fun PeerConnection.setOnIceConnectionChange(callback: (PeerConnection.IceConnectionState) -> Unit) {
    // This would need to be set during creation with proper observer
}

private fun PeerConnection.setOnDataChannel(callback: (DataChannel) -> Unit) {
    // This would need to be set during creation with proper observer
}
