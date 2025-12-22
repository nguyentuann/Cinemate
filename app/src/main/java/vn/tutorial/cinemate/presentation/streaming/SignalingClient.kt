package vn.tutorial.cinemate.presentation.streaming

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.local.LocalStorage
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.collections.get
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SignalingClient(
    private val clientId: String,
    private val movieId: String,
    private val configManager: ConfigManager,
    private val localStorage: LocalStorage
) : EventEmitter<SignalingClientEvents>() {
    
    private val TAG = "Logging SignalingClient"
    private var webSocket: WebSocket? = null
    private val gson = Gson()
    private var isConnected = false
    private var reconnectJob: Job? = null
    private var heartbeatJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    private val pendingRequests = ConcurrentHashMap<String, PendingRequest>()
    private val whoHasCache = ConcurrentHashMap<String, CachedWhoHasResponse>()
    private var signalingUrl: String = ""
    
    private data class PendingRequest(
        val onSuccess: (WhoHasReplyMessage) -> Unit,
        val onError: (Exception) -> Unit,
        val job: Job,
        val startTime: Long
    )
    
    private data class CachedWhoHasResponse(
        val response: WhoHasReplyMessage,
        val timestamp: Long
    )
    
    init {
        signalingUrl = configManager.buildSignalingUrl(clientId, movieId)
        LogUtil("signalingUrl  $signalingUrl")
    }
    
    suspend fun connect(url: String? = null): Unit = suspendCancellableCoroutine  { continuation ->
        if (url != null) {
            signalingUrl = url
        }
        
        val client = OkHttpClient.Builder()
            .connectTimeout(AppConstants.Timing.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()
        
        val requestBuilder = Request.Builder()
            .url(signalingUrl)
        
        // Add Authorization header if token exists
        localStorage.getAccessToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        
        val request = requestBuilder.build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "Connected to signaling server")
                isConnected = true
                emit("connected")
                startHeartbeat()
                continuation.resume(Unit)
            }
            
            override fun onMessage(webSocket: WebSocket, text: String) {
                handleMessage(text)
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket error", t)
                emit("error", Exception("WebSocket error: ${t.message}"))
                if (!continuation.isActive) {
                    handleDisconnection()
                }
            }
            
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "Disconnected from signaling server")
                isConnected = false
                emit("disconnected")
                stopHeartbeat()
                scheduleReconnect()
            }
        })
        
        // Connection timeout
        scope.launch {
            delay(5000)
            if (!isConnected && continuation.isActive) {
                continuation.resumeWithException(Exception("Signaling connection timeout"))
                webSocket?.close(1000, "Timeout")
            }
        }
    }
    
    fun disconnect() {
        reconnectJob?.cancel()
        stopHeartbeat()
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
        isConnected = false
        Log.d(TAG, "Manually disconnected")
    }
    
    suspend fun whoHas(qualityId: String, segmentId: String): WhoHasReplyMessage {
        if (!isConnected) {
            throw Exception("Not connected to signaling server")
        }
        
        // Check cache
        val cacheKey = "${movieId}_${qualityId}_$segmentId"
        whoHasCache[cacheKey]?.let { cached ->
            if (System.currentTimeMillis() - cached.timestamp < AppConstants.Timing.WHOHAS_CACHE_TTL) {
                Log.d(TAG, "Using cached whoHas result for $segmentId (${cached.response.peers.size} peers)")
                return cached.response
            }
        }
        
        val request = WhoHasRequest(
            movieId = movieId,
            qualityId = qualityId,
            segmentId = segmentId
        )
        
        val requestId = "whohas_${movieId}_${qualityId}_${segmentId}_${System.currentTimeMillis()}"
        val config = configManager.getConfig()
        val startTime = System.currentTimeMillis()
        
        Log.d(TAG, "whoHas query: movieId=$movieId, qualityId=$qualityId, segmentId=$segmentId")
        
        return suspendCoroutine { continuation ->
            val timeoutJob = scope.launch {
                delay(config.whoHasTimeout)
                pendingRequests.remove(requestId)
                val elapsed = System.currentTimeMillis() - startTime
                Log.w(TAG, "whoHas timeout after ${elapsed}ms for segment $segmentId")
                continuation.resumeWithException(Exception("whoHas timeout for $qualityId:$segmentId"))
            }
            
            pendingRequests[requestId] = PendingRequest(
                onSuccess = { response ->
                    whoHasCache[cacheKey] = CachedWhoHasResponse(response, System.currentTimeMillis())
                    continuation.resume(response)
                },
                onError = { error ->
                    continuation.resumeWithException(error)
                },
                job = timeoutJob,
                startTime = startTime
            )
            
            send(request)
        }
    }
    
    fun reportSegmentFetch(
        segmentId: String,
        qualityId: String,
        source: String = "peer",
        latency: Long? = null,
        speed: Float? = null
    ) {
        if (!isConnected) {
            Log.w(TAG, "Not connected, cannot report segment")
            return
        }
        
        val report = ReportSegmentRequest(
            movieId = movieId,
            qualityId = qualityId,
            segmentId = segmentId,
            source = source,
            latency = latency,
            speed = speed
        )
        
        Log.d(TAG, "Reporting segment: $qualityId:$segmentId from $source" +
                (if (latency != null) " (latency: ${latency}ms)" else "") +
                (if (speed != null) " (speed: ${speed}Mbps)" else ""))
        
        send(report)
    }
    
    fun reportSegmentRemoval(segmentId: String, qualityId: String) {
        if (!isConnected) {
            Log.w(TAG, "Not connected, cannot report segment removal")
            return
        }
        
        val report = mapOf(
            "type" to "removeSegment",
            "movieId" to movieId,
            "qualityId" to qualityId,
            "segmentId" to segmentId
        )
        
        Log.d(TAG, "Reporting segment removal: $qualityId:$segmentId")
        send(report)
    }
    
    fun getSeederUrl(qualityId: String, segmentId: String): String {
        return configManager.getSeederUrl(movieId, qualityId, segmentId)
    }
    
    fun sendOffer(peerId: String, offer: SessionDescription) {
        val message = RtcOfferRequest(
            to = peerId,
            streamId = movieId,
            sdp = offer.description
        )
        
        Log.d(TAG, "Sending rtcOffer to $peerId")
        send(message)
    }
    
    fun sendAnswer(peerId: String, answer: SessionDescription) {
        val message = RtcAnswerRequest(
            to = peerId,
            streamId = movieId,
            sdp = answer.description
        )
        
        Log.d(TAG, "Sending rtcAnswer to $peerId")
        send(message)
    }
    
    fun sendIceCandidate(peerId: String, candidate: IceCandidate) {
        val candidateMap = mapOf(
            "candidate" to candidate.sdp,
            "sdpMid" to candidate.sdpMid,
            "sdpMLineIndex" to candidate.sdpMLineIndex
        )
        
        val message = IceCandidateRequest(
            to = peerId,
            streamId = movieId,
            candidate = candidateMap
        )
        
        send(message)
    }
    
    private fun send(message: Any) {
        val json = gson.toJson(message)
        webSocket?.send(json)
    }
    
    private fun handleMessage(text: String) {
        try {
            val baseMessage = gson.fromJson(text, Map::class.java)
            val type = baseMessage["type"] as? String ?: return
            
            when (type) {
                "whoHasReply" -> {
                    val message = gson.fromJson(text, WhoHasReplyMessage::class.java)
                    handleWhoHasReply(message)
                }
                "peerList" -> {
                    val message = gson.fromJson(text, PeerListMessage::class.java)
                    emit("peerList", message)
                }
                "reportAck" -> {
                    val message = gson.fromJson(text, ReportAckMessage::class.java)
                    emit("reportAck", message)
                }
                "rtcOffer" -> {
                    val message = gson.fromJson(text, RtcOfferMessage::class.java)
                    emit("rtcOffer", message)
                }
                "rtcAnswer" -> {
                    val message = gson.fromJson(text, RtcAnswerMessage::class.java)
                    emit("rtcAnswer", message)
                }
                "iceCandidate" -> {
                    val message = gson.fromJson(text, IceCandidateMessage::class.java)
                    emit("iceCandidate", message)
                }
                "error" -> {
                    val message = gson.fromJson(text, ErrorMessage::class.java)
                    emit("error", Exception(message.message))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling message", e)
        }
    }
    
    private fun handleWhoHasReply(message: WhoHasReplyMessage) {
        val requestId = "whohas_${movieId}_${message.qualityId}_${message.segmentId}"
        val requests = pendingRequests.filter { it.key.startsWith(requestId) }
        
        requests.forEach { (key, pending) ->
            pending.job.cancel()
            pending.onSuccess(message)
            pendingRequests.remove(key)
        }
        
        emit("whoHasReply", message)
    }
    
    private fun handleDisconnection() {
        isConnected = false
        emit("disconnected")
        stopHeartbeat()
        scheduleReconnect()
    }
    
    private fun scheduleReconnect() {
        val config = configManager.getConfig()
        reconnectJob = scope.launch {
            delay(config.signalingReconnectInterval)
            try {
                connect()
                Log.d(TAG, "Reconnected to signaling server")
            } catch (e: Exception) {
                Log.e(TAG, "Reconnection failed", e)
                scheduleReconnect()
            }
        }
    }
    
    private fun startHeartbeat() {
        val config = configManager.getConfig()
        heartbeatJob = scope.launch {
            while (isActive) {
                delay(config.signalingHeartbeatInterval)
                if (isConnected) {
                    webSocket?.send(gson.toJson(mapOf("type" to "ping")))
                }
            }
        }
    }
    
    private fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }
    
    fun clear() {
        disconnect()
        scope.cancel()
        super.destroy()
    }
}
