package vn.tutorial.cinemate.presentation.streaming

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import vn.tutorial.cinemate.core.util.LogUtil
import java.util.concurrent.TimeUnit

/**
 * Segment Fetcher - HTTP fallback for segments
 */
class SegmentFetcher(
    private val movieId: String,
    private val cacheManager: CacheManager,
    private val configManager: ConfigManager
) {
    
    private val TAG = "Logging SegmentFetcher"
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    /**
     * Fetch master playlist
     */
    suspend fun fetchMasterPlaylist(): MasterPlaylist = withContext(Dispatchers.IO) {
        // Check cache first
        cacheManager.getMasterPlaylist(movieId)?.let { return@withContext it }
        
        // Fetch from seeder
        val config = configManager.getConfig()
        val url = configManager.getSeederUrl(movieId, "", "master")

        LogUtil("url master playlist: $url")
        
        try {
            val response = fetchWithTimeout(url, config.fetchTimeout)
            val text = response.body?.string() ?: throw Exception("Empty response")
            val playlist = parseMasterPlaylist(text)
            
            // Cache it
            cacheManager.setMasterPlaylist(movieId, playlist)
            
            playlist
        } catch (e: Exception) {
            throw Exception("Failed to fetch master playlist: ${e.message}")
        }
    }
    
    /**
     * Fetch variant playlist for specific quality
     */
    suspend fun fetchVariantPlaylist(qualityId: String): VariantPlaylist = withContext(Dispatchers.IO) {
        // Check cache first
        cacheManager.getVariantPlaylist(movieId, qualityId)?.let { return@withContext it }
        
        // Fetch from seeder
        val config = configManager.getConfig()
        val url = configManager.getSeederUrl(movieId, qualityId, "playlist")
        
        try {
            val response = fetchWithTimeout(url, config.fetchTimeout)
            val text = response.body?.string() ?: throw Exception("Empty response")
            val playlist = parseVariantPlaylist(text, qualityId)
            
            // Cache it
            cacheManager.setVariantPlaylist(movieId, qualityId, playlist)
            
            playlist
        } catch (e: Exception) {
            throw Exception("Failed to fetch variant playlist for $qualityId: ${e.message}")
        }
    }
    
    /**
     * Fetch init segment
     */
    suspend fun fetchInitSegment(qualityId: String, ext: String = "mp4"): InitSegment = withContext(Dispatchers.IO) {
        // Check cache first
        cacheManager.getInitSegment(movieId, qualityId)?.let { return@withContext it }
        
        // Fetch from seeder
        val config = configManager.getConfig()
        val url = configManager.getSeederUrl(movieId, qualityId, "init.$ext")
        
        try {
            val response = fetchWithTimeout(url, config.fetchTimeout)
            val data = response.body?.bytes() ?: throw Exception("Empty response")
            
            val initSegment = InitSegment(
                qualityId = qualityId,
                data = data,
                url = url
            )
            
            // Cache it
            cacheManager.setInitSegment(movieId, qualityId, initSegment)
            
            initSegment
        } catch (e: Exception) {
            throw Exception("Failed to fetch init segment for $qualityId: ${e.message}")
        }
    }
    
    /**
     * Fetch media segment from seeder via HTTP
     */
    suspend fun fetchMediaSegment(segment: SegmentMetadata): FetchResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        
        // Check cache first
        cacheManager.getSegment(movieId, segment.qualityId, segment.id)?.let { cached ->
            return@withContext FetchResult(
                success = true,
                data = cached,
                source = FetchSource.CACHE,
                latency = System.currentTimeMillis() - startTime
            )
        }
        
        // Fetch from seeder
        fetchFromSeeder(segment)
    }
    
    /**
     * Fetch segment from seeder via HTTP with retries
     */
    suspend fun fetchFromSeeder(segment: SegmentMetadata): FetchResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val config = configManager.getConfig()
        val url = configManager.getSeederUrl(movieId, segment.qualityId, segment.id)
        
        var lastError: Exception? = null
        
        for (attempt in 0..config.maxRetries) {
            try {
                val response = fetchWithTimeout(url, config.fetchTimeout)
                val data = response.body?.bytes() ?: throw Exception("Empty response")
                
                // Cache the segment
                cacheManager.setSegment(movieId, segment.qualityId, segment.id, data)
                
                return@withContext FetchResult(
                    success = true,
                    data = data,
                    source = FetchSource.SEEDER,
                    latency = System.currentTimeMillis() - startTime
                )
            } catch (e: Exception) {
                lastError = e
                Log.w(TAG, "Attempt ${attempt + 1}/${config.maxRetries + 1} failed for segment ${segment.id}: ${e.message}")
                
                // Exponential backoff
                if (attempt < config.maxRetries) {
                    delay(config.retryDelayBase * (1L shl attempt))
                }
            }
        }
        
        // All retries failed
        FetchResult(
            success = false,
            source = FetchSource.SEEDER,
            latency = System.currentTimeMillis() - startTime,
            error = lastError ?: Exception("Unknown error")
        )
    }
    
    /**
     * Parse master playlist (simplified m3u8 parser)
     */
    private fun parseMasterPlaylist(content: String): MasterPlaylist {
        val lines = content.split("\n").map { it.trim() }
        val qualities = mutableListOf<Quality>()
        
        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            
            if (line.startsWith("#EXT-X-STREAM-INF:")) {
                val attrs = parseAttributes(line)
                if (i + 1 < lines.size) {
                    val urlLine = lines[i + 1]
                    
                    if (!urlLine.startsWith("#")) {
                        // Extract quality ID from URL
                        val match = Regex("""(\w+)/playlist\.m3u8""").find(urlLine)
                        val qualityId = match?.groupValues?.get(1) ?: "quality_${qualities.size}"
                        
                        val resolution = attrs["RESOLUTION"]?.split("x") ?: listOf("1920", "1080")
                        
                        qualities.add(
                            Quality(
                                id = qualityId,
                                bandwidth = attrs["BANDWIDTH"]?.toLongOrNull() ?: 0L,
                                width = resolution[0].toIntOrNull() ?: 1920,
                                height = resolution[1].toIntOrNull() ?: 1080,
                                codecs = attrs["CODECS"] ?: "avc1.64001f,mp4a.40.2",
                                frameRate = attrs["FRAME-RATE"]?.toFloatOrNull()
                            )
                        )
                        i++ // Skip URL line
                    }
                }
            }
            i++
        }
        
        return MasterPlaylist(qualities)
    }
    
    /**
     * Parse variant playlist
     */
    private fun parseVariantPlaylist(content: String, qualityId: String): VariantPlaylist {
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
                        // Extract segment ID
                        val formatMatch = AppConstants.SegmentPatterns.REGEX.find(nextLine)
                        val segmentId = formatMatch?.value ?: throw Exception("Failed to extract segment ID from: $nextLine")
                        
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
                        i++ // Skip next line
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
    
    /**
     * Parse M3U8 attributes
     */
    private fun parseAttributes(line: String): Map<String, String> {
        val attrs = mutableMapOf<String, String>()
        val attrString = line.substringAfter(":")
        
        val regex = Regex("""(\w+(?:-\w+)*)=(".*?"|[^,]+)""")
        regex.findAll(attrString).forEach { match ->
            val key = match.groupValues[1]
            val value = match.groupValues[2].trim('"')
            attrs[key] = value
        }
        
        return attrs
    }
    
    /**
     * Fetch with timeout using OkHttp
     */
    private suspend fun fetchWithTimeout(url: String, timeout: Long): Response = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .build()
        
        val response = client.newCall(request).execute()
        
        if (!response.isSuccessful) {
            throw Exception("HTTP ${response.code}")
        }
        
        response
    }
}
