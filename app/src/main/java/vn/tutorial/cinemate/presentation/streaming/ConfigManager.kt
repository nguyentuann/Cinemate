package vn.tutorial.cinemate.presentation.streaming

import android.util.Log

class ConfigManager(
    private val configOverrides: StreamingConfig? = null
) {
    
    private val TAG = "Logging ConfigManager"
    private var config: StreamingConfig = StreamingConfig()
    
    init {
        configOverrides?.let { overrides ->
            config = config.copy(
                maxActivePeers = overrides.maxActivePeers,
                minActivePeers = overrides.minActivePeers,
                peerConnectionTimeout = overrides.peerConnectionTimeout,
                peerScoreThreshold = overrides.peerScoreThreshold,
                prefetchWindowAhead = overrides.prefetchWindowAhead,
                prefetchWindowBehind = overrides.prefetchWindowBehind,
                bufferTargetDuration = overrides.bufferTargetDuration,
                bufferMinThreshold = overrides.bufferMinThreshold,
                bufferMaxThreshold = overrides.bufferMaxThreshold,
                minBufferPrefetch = overrides.minBufferPrefetch,
                abrEnabled = overrides.abrEnabled,
                abrSwitchUpThreshold = overrides.abrSwitchUpThreshold,
                abrSwitchDownThreshold = overrides.abrSwitchDownThreshold,
                bandwidthEstimationWindow = overrides.bandwidthEstimationWindow,
                cacheSizeLimit = overrides.cacheSizeLimit,
                cacheSegmentTTL = overrides.cacheSegmentTTL,
                cachePlaylistTTL = overrides.cachePlaylistTTL,
                cacheInitSegmentTTL = overrides.cacheInitSegmentTTL,
                maxConcurrentFetches = overrides.maxConcurrentFetches,
                fetchTimeout = overrides.fetchTimeout,
                maxRetries = overrides.maxRetries,
                retryDelayBase = overrides.retryDelayBase,
                staggeredRequestDelay = overrides.staggeredRequestDelay,
                segmentRequestWaitMin = overrides.segmentRequestWaitMin,
                segmentRequestWaitMax = overrides.segmentRequestWaitMax,
                signalingReconnectInterval = overrides.signalingReconnectInterval,
                signalingHeartbeatInterval = overrides.signalingHeartbeatInterval,
                whoHasTimeout = overrides.whoHasTimeout,
                signalingUrlTemplate = overrides.signalingUrlTemplate,
                seekPrefetchAhead = overrides.seekPrefetchAhead,
                seekPrefetchBehind = overrides.seekPrefetchBehind,
                baseUrl = overrides.baseUrl
            )
        }
        
        Log.d(TAG, "ConfigManager initialized with baseUrl: ${config.baseUrl}")
    }
    
    fun getConfig(): StreamingConfig = config
    
    fun get(key: String): Any {
        return when (key) {
            "maxActivePeers" -> config.maxActivePeers
            "minActivePeers" -> config.minActivePeers
            "peerConnectionTimeout" -> config.peerConnectionTimeout
            "peerScoreThreshold" -> config.peerScoreThreshold
            "prefetchWindowAhead" -> config.prefetchWindowAhead
            "prefetchWindowBehind" -> config.prefetchWindowBehind
            "bufferTargetDuration" -> config.bufferTargetDuration
            "bufferMinThreshold" -> config.bufferMinThreshold
            "bufferMaxThreshold" -> config.bufferMaxThreshold
            "minBufferPrefetch" -> config.minBufferPrefetch
            "abrEnabled" -> config.abrEnabled
            "abrSwitchUpThreshold" -> config.abrSwitchUpThreshold
            "abrSwitchDownThreshold" -> config.abrSwitchDownThreshold
            "bandwidthEstimationWindow" -> config.bandwidthEstimationWindow
            "cacheSizeLimit" -> config.cacheSizeLimit
            "cacheSegmentTTL" -> config.cacheSegmentTTL
            "cachePlaylistTTL" -> config.cachePlaylistTTL
            "cacheInitSegmentTTL" -> config.cacheInitSegmentTTL
            "maxConcurrentFetches" -> config.maxConcurrentFetches
            "fetchTimeout" -> config.fetchTimeout
            "maxRetries" -> config.maxRetries
            "retryDelayBase" -> config.retryDelayBase
            "staggeredRequestDelay" -> config.staggeredRequestDelay
            "segmentRequestWaitMin" -> config.segmentRequestWaitMin
            "segmentRequestWaitMax" -> config.segmentRequestWaitMax
            "signalingReconnectInterval" -> config.signalingReconnectInterval
            "signalingHeartbeatInterval" -> config.signalingHeartbeatInterval
            "whoHasTimeout" -> config.whoHasTimeout
            "signalingUrlTemplate" -> config.signalingUrlTemplate ?: ""
            "seekPrefetchAhead" -> config.seekPrefetchAhead
            "seekPrefetchBehind" -> config.seekPrefetchBehind
            "baseUrl" -> config.baseUrl
            else -> throw IllegalArgumentException("Unknown config key: $key")
        }
    }
    
    /**
     * Build signaling WebSocket URL
     */
    fun buildSignalingUrl(clientId: String, movieId: String): String {
        val template = config.signalingUrlTemplate ?: AppConstants.WebSocket.URL_TEMPLATE
        
        // Parse baseUrl to extract components
        val url = config.baseUrl
        val protocol = if (url.startsWith("https")) "wss" else "ws"
        
        // Extract host and port from baseUrl
        val urlWithoutProtocol = url.removePrefix("http://").removePrefix("https://")
        val parts = urlWithoutProtocol.split(":")
        val host = parts[0]
        val port = if (parts.size > 1) {
            parts[1].split("/")[0]
        } else {
            AppConstants.WebSocket.DEFAULT_PORT.toString()
        }
        
        return template
            .replace("{protocol}", protocol)
            .replace("{host}", host)
            .replace("{port}", port)
            .replace("{path}", AppConstants.WebSocket.PATH)
            .replace("{clientId}", clientId)
            .replace("{movieId}", movieId)
    }
    
    /**
     * Get seeder URL for fetching resources
     */
    fun getSeederUrl(movieId: String, qualityId: String, resource: String): String {
        val baseUrl = config.baseUrl.trimEnd('/')
        val streamsBase = AppConstants.ApiPaths.STREAMS_BASE
        
        return when {
            resource == "master.m3u8" || resource == "master" -> {
                "$baseUrl$streamsBase/$movieId/${AppConstants.ApiPaths.MASTER_PLAYLIST}"
            }
            resource == "playlist.m3u8" || resource == "playlist" -> {
                "$baseUrl$streamsBase/$movieId/$qualityId/${AppConstants.ApiPaths.VARIANT_PLAYLIST}"
            }
            resource.startsWith("init.") -> {
                "$baseUrl$streamsBase/$movieId/$qualityId/$resource"
            }
            resource.startsWith("seg_") && resource.endsWith(".m4s") -> {
                "$baseUrl$streamsBase/$movieId/$qualityId/$resource"
            }
            else -> {
                "$baseUrl$streamsBase/$movieId/$qualityId/$resource"
            }
        }
    }
    
    /**
     * Update specific configuration values at runtime
     */
    fun updateConfig(updates: Map<String, Any>) {
        // This method allows runtime configuration updates
        Log.d(TAG, "Updating config with ${updates.size} values")
        // For simplicity, we won't implement full runtime updates
        // In production, you'd validate and apply updates here
    }
}
