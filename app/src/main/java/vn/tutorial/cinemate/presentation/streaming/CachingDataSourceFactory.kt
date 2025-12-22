package vn.tutorial.cinemate.presentation.streaming

import android.net.Uri
import android.util.Log
import androidx.media3.common.C
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.TransferListener
import kotlinx.coroutines.runBlocking
import vn.tutorial.cinemate.data.local.LocalStorage

/**
 * Custom DataSource that caches segments for P2P sharing
 */
class CachingDataSourceFactory(
    private val movieId: String,
    private val cacheManager: CacheManager,
    private val signalingClient: SignalingClient,
    private val localStorage: LocalStorage
) : DataSource.Factory {
    
    private val httpDataSourceFactory: DefaultHttpDataSource.Factory
        get() {
            val factory = DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)
                .setConnectTimeoutMs(30000)
                .setReadTimeoutMs(30000)
            
            // Add Authorization header if token exists
            localStorage.getAccessToken()?.let { token ->
                factory.setDefaultRequestProperties(mapOf("Authorization" to "Bearer $token"))
            }
            
            return factory
        }
    
    override fun createDataSource(): DataSource {
        return CachingDataSource(
            movieId,
            cacheManager,
            signalingClient,
            localStorage,
            httpDataSourceFactory.createDataSource()
        )
    }
}

class CachingDataSource(
    private val movieId: String,
    private val cacheManager: CacheManager,
    private val signalingClient: SignalingClient,
    private val localStorage: LocalStorage,
    private val upstreamDataSource: DataSource
) : DataSource {
    
    private val TAG = "Logging CachingDataSource"
    private var currentUri: Uri? = null
    private var currentQualityId: String? = null
    private var currentSegmentId: String? = null
    private val buffer = mutableListOf<Byte>()
    
    override fun open(dataSpec: DataSpec): Long {
        currentUri = dataSpec.uri
        buffer.clear()
        
        // Parse URL to extract quality and segment ID
        val path = currentUri?.path ?: ""
        parseSegmentInfo(path)
        
        Log.d(TAG, "Opening: $path (quality=$currentQualityId, segment=$currentSegmentId)")
        
        // NOTE: We check cache for logging but ALWAYS fetch from upstream
        // This avoids timestamp discontinuity issues with ExoPlayer
        // Cache is only used for P2P sharing, not for playback
        if (currentQualityId != null && currentSegmentId != null && !path.endsWith(".m3u8")) {
            val cached = cacheManager.getSegment(movieId, currentQualityId!!, currentSegmentId!!)
            if (cached != null) {
                Log.d(TAG, "✓ Cache available: $currentSegmentId (${cached.size} bytes) - but fetching fresh for playback consistency")
            } else {
                Log.d(TAG, "✗ Cache MISS: $currentSegmentId - fetching from network")
            }
        }
        
        // Always use upstream to avoid ExoPlayer timestamp issues
        return upstreamDataSource.open(dataSpec)
    }
    
    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        val bytesRead = upstreamDataSource.read(buffer, offset, length)
        
        if (bytesRead > 0) {
            // Accumulate data for caching
            for (i in 0 until bytesRead) {
                this.buffer.add(buffer[offset + i])
            }
        }
        
        return bytesRead
    }
    
    override fun close() {
        upstreamDataSource.close()
        
        // Cache the segment data if it's a media segment
        if (currentQualityId != null && currentSegmentId != null && buffer.isNotEmpty()) {
            val uri = currentUri?.toString() ?: ""
            
            // Only cache media segments (.m4s), not playlists (.m3u8)
            if (!uri.endsWith(".m3u8") && uri.contains(".m4s")) {
                val data = buffer.toByteArray()
                
                Log.d(TAG, "✓ Caching segment: $currentSegmentId (${data.size} bytes)")
                
                runBlocking {
                    try {
                        // Cache segment
                        cacheManager.setSegment(movieId, currentQualityId!!, currentSegmentId!!, data)
                        
                        // Report to signaling server for P2P
                        signalingClient.reportSegmentFetch(
                            segmentId = currentSegmentId!!,
                            qualityId = currentQualityId!!,
                            source = "http",
                            latency = 0L,
                            speed = null
                        )
                        
                        Log.d(TAG, "✓ Segment cached and reported: $currentSegmentId")
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to cache segment: $currentSegmentId", e)
                    }
                }
            }
        }
        
        buffer.clear()
        currentUri = null
        currentQualityId = null
        currentSegmentId = null
    }
    
    override fun addTransferListener(transferListener: TransferListener) {
        upstreamDataSource.addTransferListener(transferListener)
    }
    
    override fun getUri(): Uri? = currentUri
    
    private fun parseSegmentInfo(path: String) {
        try {
            // Expected format: /streams/movies/{movieId}/{qualityId}/seg_0001.m4s
            val parts = path.split("/")
            
            if (parts.size >= 5) {
                // Find quality ID (should be before the last segment)
                for (i in parts.indices.reversed()) {
                    val part = parts[i]
                    
                    // Check if it's a segment file
                    if (part.matches(Regex("seg_\\d+\\.m4s"))) {
                        currentSegmentId = part
                        // Quality ID should be the previous part
                        if (i > 0) {
                            currentQualityId = parts[i - 1]
                        }
                        break
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse segment info from: $path", e)
        }
    }
}
