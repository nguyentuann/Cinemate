package vn.tutorial.cinemate.presentation.streaming

import android.content.Context
import android.util.Log
import androidx.media3.common.Player
import kotlinx.coroutines.*

class StreamingPlayerCoordinator(
    private val context: Context,
    private val options: StreamingPlayerOptions
) : EventEmitter<Any>() {
    
    private val TAG = "Logging StreamingPlayerCoordinator"
    
    // Core modules
    private val configManager: ConfigManager
    val mseManager: SimpleMseManager
    private val cacheManager: CacheManager
    private val segmentFetcher: SegmentFetcher
    private val bufferManager: BufferManager
    private val peerManager: PeerManager
    private val signalingClient: SignalingClient
    private val integratedFetchClient: IntegratedSegmentFetchClient
    private val abrManager: AbrManager
    
    // State
    private val movieId: String = options.movieId
    private val clientId: String = options.clientId
    private var currentQuality: Quality? = null
    private var availableQualities: List<Quality> = emptyList()
    private var currentSegments: List<SegmentMetadata> = emptyList()
    private var isInitialized = false
    private var abrEnabled = true
    
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    data class StreamingPlayerOptions(
        val movieId: String,
        val clientId: String,
        val signalingUrl: String? = null,
        val configOverrides: StreamingConfig? = null
    )
    
    init {
        // Initialize configuration
        configManager = ConfigManager(options.configOverrides)
        
        // Initialize signaling first
        signalingClient = SignalingClient(clientId, movieId, configManager)
        
        // Initialize cache with segment removal callback
        cacheManager = CacheManager(
            configManager = configManager,
            onSegmentRemoved = { movieId, qualityId, segmentId ->
                signalingClient.reportSegmentRemoval(segmentId, qualityId)
            }
        )
        
        // Initialize MSE (using SimpleMseManager for HLS support with P2P caching)
        mseManager = SimpleMseManager(context, movieId, cacheManager, signalingClient)
        
        // Initialize segment fetcher
        segmentFetcher = SegmentFetcher(movieId, cacheManager, configManager)
        
        // Initialize buffer manager
        bufferManager = BufferManager(mseManager, configManager)
        bufferManager.setCacheManager(cacheManager)
        
        // Initialize peer manager
        peerManager = PeerManager(movieId, signalingClient, configManager, cacheManager)
        
        // Initialize integrated fetch client
        integratedFetchClient = IntegratedSegmentFetchClient(
            movieId,
            signalingClient,
            peerManager,
            cacheManager,
            segmentFetcher,
            mseManager,
            configManager
        )
        
        // Initialize ABR manager
        abrManager = AbrManager(
            movieId,
            peerManager,
            signalingClient,
            cacheManager,
            configManager
        )
        
        setupEventListeners()
        setupBufferManagerFetchCallback()
    }
    
    private fun setupBufferManagerFetchCallback() {
        bufferManager.setFetchCallback { segment, critical ->
            try {
                val result = integratedFetchClient.fetchSegment(
                    IntegratedSegmentFetchClient.SegmentFetchRequest(
                        segment = segment,
                        priority = if (critical) 100 else 50,
                        forSeek = false,
                        critical = critical
                    )
                )
                
                if (result.success && result.data != null) {
                    result.data
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Fetch failed for segment ${segment.id}", e)
                null
            }
        }
    }
    
    private fun setupEventListeners() {
        // MSE events
        mseManager.on("sourceOpen") {
            Log.d(TAG, "MSE source opened")
        }
        
        mseManager.on("error") { data ->
            val error = data as? Exception ?: Exception("MSE error")
            emit("error", error)
        }
        
        mseManager.on("qualityChanged") { data ->
            val quality = data as? Quality
            quality?.let {
                currentQuality = it
                emit("qualityChanged", it)
            }
        }
        
        // Buffer events
        bufferManager.on("bufferingStart") {
            emit("buffering")
        }
        
        bufferManager.on("bufferLow") { data ->
            val bufferAhead = data as? Float ?: 0f
            Log.d(TAG, "Buffer low: ${bufferAhead}s")
        }
        
        bufferManager.on("bufferCritical") { data ->
            val bufferAhead = data as? Float ?: 0f
            Log.w(TAG, "Buffer CRITICAL: ${bufferAhead}s")
            emit("buffering")
        }
        
        bufferManager.on("qualitySwitch") { data ->
            Log.d(TAG, "Quality switched")
            checkAbrSwitch()
        }
        
        // Signaling events
        signalingClient.on("connected") {
            Log.d(TAG, "Connected to signaling server")
            reportAvailableSegments()
        }
        
        signalingClient.on("whoHasReply") { data ->
            val message = data as? WhoHasReplyMessage
            message?.let {
                if (it.peers.isNotEmpty()) {
                    val segmentKey = "${it.qualityId}:${it.segmentId}"
                    it.peers.forEach { peer ->
                        peerManager.updatePeerSegmentAvailability(peer.peerId, listOf(segmentKey))
                    }
                }
            }
        }
        
        // Peer events
        peerManager.on("peerConnected") { data ->
            val peerId = data as? String ?: return@on
            Log.d(TAG, "Peer connected: $peerId")
        }
        
        peerManager.on("peerDisconnected") { data ->
            val peerId = data as? String ?: return@on
            Log.d(TAG, "Peer disconnected: $peerId")
        }
        
        // ABR events
        abrManager.on("qualityChanged") { data ->
            Log.d(TAG, "ABR quality changed")
            // Data would be (oldQuality, newQuality, reason) in TypeScript
            // For Kotlin, we handle it differently
        }
    }
    
    suspend fun initialize() {
        try {
            Log.d(TAG, "Initializing streaming player...")
            
            // Connect to signaling server
            options.signalingUrl?.let {
                signalingClient.connect(it)
            } ?: run {
                signalingClient.connect()
            }
            
            // Fetch master playlist
            val masterPlaylist = segmentFetcher.fetchMasterPlaylist()
            availableQualities = masterPlaylist.qualities
            
            if (availableQualities.isEmpty()) {
                throw Exception("No qualities available")
            }
            
            // Initialize ABR manager
            abrManager.initialize(masterPlaylist)
            
            // Get initial quality
            val initialQuality = abrManager.getCurrentQuality()
                ?: throw Exception("ABR manager failed to select initial quality")
            
            // Fetch variant playlist
            val variantPlaylist = segmentFetcher.fetchVariantPlaylist(initialQuality.id)
            currentSegments = variantPlaylist.segments
            
            // Cache variant playlist
            cacheManager.setVariantPlaylist(movieId, initialQuality.id, variantPlaylist)
            
            // Initialize MSE
            Log.d(TAG, "Initializing MSE...")
            mseManager.initialize()
            
            // Set duration
            Log.d(TAG, "Setting duration: ${variantPlaylist.totalDuration}s")
            mseManager.setDuration(variantPlaylist.totalDuration)
            
            // Load HLS manifest directly - ExoPlayer will handle everything!
            val playlistUrl = configManager.getSeederUrl(
                movieId, 
                initialQuality.id, 
                AppConstants.ApiPaths.VARIANT_PLAYLIST
            )
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            Log.d(TAG, "Movie ID: $movieId")
            Log.d(TAG, "Quality: ${initialQuality.id} (${initialQuality.width}x${initialQuality.height})")
            Log.d(TAG, "Playlist URL: $playlistUrl")
            Log.d(TAG, "Total segments: ${currentSegments.size}")
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            
            mseManager.loadHlsManifest(playlistUrl)
            
            // Initialize buffer manager (for P2P optimization, not critical path)
            Log.d(TAG, "Initializing buffer manager...")
            bufferManager.initialize(initialQuality, currentSegments)
            
            currentQuality = initialQuality
            isInitialized = true
            
            Log.d(TAG, "✓✓✓ Initialization complete ✓✓✓")
            emit("ready")
            
        } catch (e: Exception) {
            Log.e(TAG, "Initialization failed", e)
            emit("error", e)
            throw e
        }
    }
    
    private fun reportAvailableSegments() {
        // Segments are reported individually as they are fetched
    }
    
    suspend fun switchQuality(qualityId: String) {
        val newQuality = availableQualities.find { it.id == qualityId }
        if (newQuality == null || newQuality.id == currentQuality?.id) {
            return
        }
        
        Log.d(TAG, "Switching quality to $qualityId")
        
        try {
            // Use ABR manager to switch quality
            abrManager.setQuality(newQuality, "manual")
            
            // Get variant playlist
            var variantPlaylist = cacheManager.getVariantPlaylist(movieId, qualityId)
            
            if (variantPlaylist == null) {
                variantPlaylist = segmentFetcher.fetchVariantPlaylist(qualityId)
            }
            
            val newSegments = variantPlaylist.segments
            
            // With HLS, switch quality by loading new playlist
            val playlistUrl = configManager.getSeederUrl(
                movieId, 
                qualityId, 
                AppConstants.ApiPaths.VARIANT_PLAYLIST
            )
            
            val currentTime = mseManager.getCurrentTime()
            
            Log.d(TAG, "Loading new quality playlist from: $playlistUrl")
            mseManager.loadHlsManifest(playlistUrl)
            
            // Seek back to maintain continuity
            delay(500) // Wait for new manifest to load
            mseManager.seekTo(currentTime)
            
            if (newSegments.isNotEmpty()) {
                // Update buffer manager for P2P optimization
                // Note: initSegment not needed with HLS
                val dummyInitSegment = InitSegment(qualityId, ByteArray(0), "")
                bufferManager.switchQuality(newQuality, newSegments, dummyInitSegment, skipInitAppend = true)
                currentSegments = newSegments
            }
            
            currentQuality = newQuality
            
            Log.d(TAG, "Quality switched to $qualityId")
            
        } catch (e: Exception) {
            Log.e(TAG, "Quality switch failed", e)
            emit("error", e)
        }
    }
    
    fun enableAutoQuality() {
        abrEnabled = true
        Log.d(TAG, "ABR enabled")
    }
    
    fun disableAutoQuality() {
        abrEnabled = false
        Log.d(TAG, "ABR disabled (manual mode)")
    }
    
    suspend fun setManualQuality(qualityId: String) {
        disableAutoQuality()
        switchQuality(qualityId)
    }
    
    suspend fun play() {
        if (!isInitialized) {
            initialize()
        }
        mseManager.play()
    }
    
    fun pause() {
        mseManager.pause()
    }
    
    suspend fun seek(time: Float) {
        val quality = currentQuality ?: run {
            Log.w(TAG, "Cannot seek: no quality selected")
            return
        }
        
        // Use cache to map time → segmentId
        val targetSegmentId = cacheManager.mapTimeToSegmentId(movieId, quality.id, time)
        
        if (targetSegmentId != null) {
            Log.d(TAG, "Seek ${time}s → segment $targetSegmentId")
            
            // Get segments around seek point
            val prefetchSegments = cacheManager.getSegmentsAroundTime(
                movieId,
                quality.id,
                time,
                2, // 2 before
                5  // 5 after
            )
            
            Log.d(TAG, "Prefetch segments: [${prefetchSegments.joinToString { it.id }}]")
        }
        
        // Perform seek
        mseManager.seekTo(time)
        
        // Buffer manager will handle prefetching
        bufferManager.handleSeek(time)
    }
    
    fun getPlayerState(): PlayerState {
        var currentTime = 0f
        var duration = 0f

        scope.launch {
            currentTime = mseManager.getCurrentTime()
            duration = mseManager.getDuration()
        }
        val player = mseManager.exoPlayer



        return PlayerState(
            isPlaying = player?.isPlaying ?: false,
            isPaused = !(player?.isPlaying ?: false),
            isSeeking = false, // ExoPlayer doesn't expose this directly
            isBuffering = player?.playbackState == Player.STATE_BUFFERING,
            currentTime = currentTime,
            duration = duration,
            currentQuality = currentQuality,
            availableQualities = availableQualities,
            volume = player?.volume ?: 1f,
            muted = player?.volume == 0f
        )
    }
    
    fun getPlaybackMetrics(): PlaybackMetrics {
        // Sai

        var bufferStatus = BufferStatus(emptyList(), 0f, 0f, 0f, 0f)

        scope.launch {
            bufferStatus = bufferManager.getBufferStatus()
        }
        val config = configManager.getConfig()
        val bufferHealth = (bufferStatus.bufferAhead / config.bufferTargetDuration).coerceIn(0f, 1f)
        
        val activePeers = peerManager.getActivePeerCount()
        val p2pRatio = integratedFetchClient.getP2pRatio()
        
        return PlaybackMetrics(
            bufferHealth = bufferHealth,
            downloadSpeed = 0L,
            bandwidthEstimate = abrManager.getEstimatedBandwidth(),
            droppedFrames = 0L,
            stallCount = 0,
            totalStallTime = 0L,
            p2pRatio = p2pRatio,
            activeConnections = activePeers
        )
    }
    
    fun getAvailableQualities(): List<Quality> = availableQualities.toList()
    
    fun getCurrentQuality(): Quality? = currentQuality
    
    fun getCacheStats(): Map<String, Any> {
        val stats = cacheManager.getStats()
        return mapOf(
            "hits" to stats.hits,
            "misses" to stats.misses,
            "evictions" to stats.evictions,
            "currentSize" to stats.currentSize,
            "maxSize" to stats.maxSize,
            "itemCount" to stats.itemCount,
            "hitRate" to "${(cacheManager.getHitRate() * 100).toInt()}%",
            "usagePercentage" to "${cacheManager.getUsagePercentage().toInt()}%"
        )
    }
    
    fun setVolume(volume: Float) {
        mseManager.exoPlayer?.volume = volume.coerceIn(0f, 1f)
    }
    
    fun setMuted(muted: Boolean) {
        mseManager.exoPlayer?.volume = if (muted) 0f else 1f
    }
    
    fun getFetchStats(): IntegratedSegmentFetchClient.SegmentFetchStats {
        return integratedFetchClient.getStats()
    }
    
    fun dispose() {
        Log.d(TAG, "Disposing streaming player")
        
        bufferManager.destroy()
        mseManager.destroy()
        mseManager.clear()
        peerManager.destroy()
        signalingClient.destroy()
        cacheManager.clear()
        integratedFetchClient.destroy()
        abrManager.destroy()
        isInitialized = false
        scope.cancel()
    }
    
    private fun checkAbrSwitch() {
        if (!isInitialized || currentQuality == null || !abrEnabled) {
            return
        }
        
        scope.launch {
            val bufferStatus = bufferManager.getBufferStatus()
            val metrics = getPlaybackMetrics()
            
            val config = configManager.getConfig()
            val quality = currentQuality ?: return@launch
            
            // Simple ABR logic
            if (bufferStatus.bufferAhead > config.bufferTargetDuration * 0.8f && 
                metrics.bandwidthEstimate > quality.bandwidth * 1.2) {
                
                val higherQuality = availableQualities
                    .filter { it.bandwidth > quality.bandwidth }
                    .minByOrNull { it.bandwidth }
                
                if (higherQuality != null) {
                    Log.d(TAG, "ABR: Upgrading to ${higherQuality.id} (${bufferStatus.bufferAhead}s buffer)")
                    switchQuality(higherQuality.id)
                }
            } else if (bufferStatus.bufferAhead < config.bufferTargetDuration * 0.3f) {
                val lowerQuality = availableQualities
                    .filter { it.bandwidth < quality.bandwidth }
                    .maxByOrNull { it.bandwidth }
                
                if (lowerQuality != null) {
                    Log.d(TAG, "ABR: Downgrading to ${lowerQuality.id} (${bufferStatus.bufferAhead}s buffer)")
                    switchQuality(lowerQuality.id)
                }
            }
        }
    }
}
