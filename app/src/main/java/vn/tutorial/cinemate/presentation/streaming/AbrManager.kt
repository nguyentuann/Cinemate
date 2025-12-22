package vn.tutorial.cinemate.presentation.streaming

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import vn.tutorial.cinemate.data.local.LocalStorage

class AbrManager(
    private val movieId: String,
    private val peerManager: PeerManager,
    private val signalingClient: SignalingClient,
    private val cacheManager: CacheManager,
    private val configManager: ConfigManager,
    private val localStorage: LocalStorage
) : EventEmitter<AbrManagerEvents>() {
    
    private val TAG = "Logging AbrManager"
    
    private var masterPlaylist: MasterPlaylist? = null
    private val variantPlaylists = mutableMapOf<String, VariantPlaylist>()
    private val initSegments = mutableMapOf<String, InitSegment>()
    
    private var currentQuality: Quality? = null
    private var isQualitySwitching = false
    
    private val bandwidthSamples = mutableListOf<Long>()
    private var estimatedBandwidth = 0L
    
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    suspend fun initialize(masterPlaylist: MasterPlaylist) {
        this.masterPlaylist = masterPlaylist
        
        Log.d(TAG, "Initializing with ${masterPlaylist.qualities.size} quality variants")
        
        // Load all variant playlists
        masterPlaylist.qualities.forEach { quality ->
            scope.launch {
                try {
                    loadVariantPlaylist(quality.id)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to load playlist for ${quality.id}", e)
                }
            }
        }
        
        // Wait for playlists to load
        delay(1000)
        
        // Select initial quality
        val defaultQualityId = masterPlaylist.defaultQualityId ?: masterPlaylist.qualities.first().id
        val defaultQuality = masterPlaylist.qualities.find { it.id == defaultQualityId }
        
        if (defaultQuality != null) {
            setQuality(defaultQuality, "initial")
        }
        
        Log.d(TAG, "Initialized with quality: ${defaultQuality?.id}")
    }
    
    private suspend fun loadVariantPlaylist(qualityId: String): VariantPlaylist {
        // Check if already loaded
        variantPlaylists[qualityId]?.let { return it }
        
        try {
            val url = configManager.getSeederUrl(movieId, qualityId, "playlist.m3u8")
            
            // Fetch playlist content (using SegmentFetcher or direct HTTP)
            // For now, assume we have a helper to fetch
            val playlistText = fetchPlaylistContent(url)
            val playlist = parseM3U8Playlist(qualityId, playlistText)
            
            variantPlaylists[qualityId] = playlist
            
            Log.d(TAG, "Loaded playlist for $qualityId: ${playlist.segments.size} segments")
            return playlist
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load playlist for $qualityId", e)
            throw e
        }
    }
    
    private fun parseM3U8Playlist(qualityId: String, content: String): VariantPlaylist {
        val lines = content.split("\n").filter { it.trim().isNotEmpty() }
        val segments = mutableListOf<SegmentMetadata>()
        var targetDuration = 0f
        var currentTimestamp = 0f
        
        var i = 0
        while (i < lines.size) {
            val line = lines[i].trim()
            
            if (line.startsWith("#EXT-X-TARGETDURATION:")) {
                targetDuration = line.split(":")[1].toFloatOrNull() ?: 0f
            }
            
            if (line.startsWith("#EXTINF:")) {
                val duration = line.split(":")[1].split(",")[0].toFloatOrNull() ?: 0f
                if (i + 1 < lines.size) {
                    val nextLine = lines[i + 1]
                    
                    if (!nextLine.startsWith("#")) {
                        val formatMatch = AppConstants.SegmentPatterns.REGEX.find(nextLine)
                        val segmentId = formatMatch?.value ?: throw Exception("Failed to extract segment ID")
                        
                        segments.add(
                            SegmentMetadata(
                                id = segmentId,
                                movieId = movieId,
                                qualityId = qualityId,
                                duration = duration,
                                timestamp = currentTimestamp
                            )
                        )
                        
                        currentTimestamp += duration
                        i++
                    }
                }
            }
            i++
        }
        
        val totalDuration = segments.sumOf { it.duration.toDouble() }.toFloat()
        
        return VariantPlaylist(
            qualityId = qualityId,
            segments = segments,
            targetDuration = targetDuration,
            totalDuration = totalDuration
        )
    }
    
    suspend fun setQuality(quality: Quality, reason: String) {
        val oldQuality = currentQuality
        
        if (oldQuality?.id == quality.id && !isQualitySwitching) {
            return
        }
        
        Log.d(TAG, "Quality switch: ${oldQuality?.id ?: "none"} -> ${quality.id} ($reason)")
        
        isQualitySwitching = true
        
        try {
            // Ensure init segment is loaded
            ensureInitSegment(quality.id)
            
            currentQuality = quality
            isQualitySwitching = false
            
            emit("qualityChanged", oldQuality, quality, reason)
            Log.d(TAG, "Quality switched to ${quality.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to switch quality", e)
            isQualitySwitching = false
            throw e
        }
    }
    
    private suspend fun ensureInitSegment(qualityId: String): InitSegment {
        // Check cache first
        cacheManager.getInitSegment(movieId, qualityId)?.let { return it }
        
        // Check local map
        initSegments[qualityId]?.let { return it }
        
        // Fetch from seeder
        try {
            val url = configManager.getSeederUrl(movieId, qualityId, "init.mp4")
            val data = fetchInitSegmentData(url)
            
            val initSegment = InitSegment(
                qualityId = qualityId,
                data = data,
                url = url
            )
            
            // Cache it
            cacheManager.setInitSegment(movieId, qualityId, initSegment)
            initSegments[qualityId] = initSegment
            
            emit("initSegmentFetched", qualityId, data.size)
            Log.d(TAG, "Fetched init segment for $qualityId (${data.size} bytes)")
            
            return initSegment
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch init segment for $qualityId", e)
            throw e
        }
    }
    
    fun updateBandwidthEstimate(segmentSize: Long, downloadTime: Long) {
        if (downloadTime <= 0) return
        
        val bandwidth = (segmentSize * 8 * 1000) / downloadTime // bits per second
        
        val config = configManager.getConfig()
        bandwidthSamples.add(bandwidth)
        
        if (bandwidthSamples.size > config.bandwidthEstimationWindow) {
            bandwidthSamples.removeAt(0)
        }
        
        estimatedBandwidth = if (bandwidthSamples.isNotEmpty()) {
            bandwidthSamples.average().toLong()
        } else 0L
        
        emit("bandwidthEstimated", estimatedBandwidth)
    }
    
    suspend fun selectBestQuality(bufferHealth: Float): Quality? {
        val qualities = masterPlaylist?.qualities ?: return null
        val config = configManager.getConfig()
        
        if (!config.abrEnabled) {
            return currentQuality
        }
        
        val current = currentQuality ?: return null
        
        // Switch up if buffer is healthy and bandwidth allows
        if (bufferHealth > config.abrSwitchUpThreshold) {
            val higherQualities = qualities
                .filter { it.bandwidth > current.bandwidth }
                .filter { it.bandwidth <= estimatedBandwidth * 0.8 }
                .sortedBy { it.bandwidth }
            
            if (higherQualities.isNotEmpty()) {
                val target = higherQualities.first()
                Log.d(TAG, "ABR: Upgrading to ${target.id} (buffer: ${(bufferHealth * 100).toInt()}%)")
                return target
            }
        }
        
        // Switch down if buffer is low
        if (bufferHealth < config.abrSwitchDownThreshold) {
            val lowerQualities = qualities
                .filter { it.bandwidth < current.bandwidth }
                .sortedByDescending { it.bandwidth }
            
            if (lowerQualities.isNotEmpty()) {
                val target = lowerQualities.first()
                Log.d(TAG, "ABR: Downgrading to ${target.id} (buffer: ${(bufferHealth * 100).toInt()}%)")
                return target
            }
        }
        
        return null
    }
    
    fun getCurrentQuality(): Quality? = currentQuality
    
    fun getAvailableQualities(): List<Quality> = masterPlaylist?.qualities ?: emptyList()
    
    fun getVariantPlaylist(qualityId: String): VariantPlaylist? = variantPlaylists[qualityId]
    
    fun getInitSegment(qualityId: String): InitSegment? = initSegments[qualityId]
    
    fun getEstimatedBandwidth(): Long = estimatedBandwidth
    
    // Helper functions that would be implemented with actual HTTP fetching
    private suspend fun fetchPlaylistContent(url: String): String {
        // This would use OkHttp or similar to fetch the content
        // For now, return empty string as placeholder
        return withContext(Dispatchers.IO) {
            val token = localStorage.getAccessToken()
            val client = OkHttpClient()
            val requestBuilder = Request.Builder().url(url)
            
            // Add Authorization header if token exists
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            
            val request = requestBuilder.build()
            val response = client.newCall(request).execute()
            response.body?.string() ?: ""
        }
    }
    
    private suspend fun fetchInitSegmentData(url: String): ByteArray {
        return withContext(Dispatchers.IO) {
            val token = localStorage.getAccessToken()
            val client = OkHttpClient()
            val requestBuilder = Request.Builder().url(url)
            
            // Add Authorization header if token exists
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            
            val request = requestBuilder.build()
            val response = client.newCall(request).execute()
            response.body?.bytes() ?: ByteArray(0)
        }
    }
    
    fun clear() {
        scope.cancel()
        bandwidthSamples.clear()
        variantPlaylists.clear()
        initSegments.clear()
        Log.d(TAG, "AbrManager destroyed")
    }
}
