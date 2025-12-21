package vn.tutorial.cinemate.presentation.streaming

import android.util.Log
import java.util.concurrent.ConcurrentHashMap

/**
 * Cache Manager with LRU eviction and hot cache protection
 */
class CacheManager(
    private val configManager: ConfigManager,
    private val config: CacheConfig = CacheConfig(),
    private val onSegmentRemoved: ((String, String, String) -> Unit)? = null
) {
    
    private val TAG = "Logging CacheManager"
    private val cache = ConcurrentHashMap<String, CacheEntry<Any>>()
    private val accessOrder = mutableListOf<String>()
    private val hotCache = mutableSetOf<String>()
    private val segmentTimeMaps = ConcurrentHashMap<String, List<SegmentMetadata>>()
    
    private var currentSize = 0L
    private val stats = CacheStats(maxSize = config.maxSize)
    
    data class CacheConfig(
        val maxSize: Long = AppConstants.CacheDefaults.MAX_SIZE,
        val segmentTTL: Long = AppConstants.CacheDefaults.SEGMENT_TTL,
        val initTTL: Long = AppConstants.CacheDefaults.INIT_TTL,
        val playlistTTL: Long = AppConstants.CacheDefaults.PLAYLIST_TTL,
        val hotCacheProtection: Boolean = AppConstants.CacheDefaults.HOT_CACHE_PROTECTION
    )
    
    // ============ Core Cache Operations ============
    
    fun set(key: String, data: Any, ttl: Long, isHot: Boolean = false) {
        val size = calculateSize(data)
        
        if (!isHot) {
            while (currentSize + size > config.maxSize && cache.size > 0) {
                evict()
            }
        }
        
        cache[key]?.let { existing ->
            currentSize -= existing.size
            removeFromAccessOrder(key)
        }
        
        val entry = CacheEntry(
            key = key,
            data = data,
            size = size,
            timestamp = System.currentTimeMillis(),
            ttl = ttl
        )
        
        cache[key] = entry
        
        if (isHot && config.hotCacheProtection) {
            hotCache.add(key)
        } else {
            accessOrder.add(key)
        }
        
        currentSize += size
        updateStats()
    }
    
    fun <T> get(key: String): T? {
        val entry = cache[key] ?: run {
            stats.misses++
            return null
        }
        
        val now = System.currentTimeMillis()
        if (now - entry.timestamp > entry.ttl) {
            delete(key)
            stats.misses++
            return null
        }
        
        entry.accessCount++
        entry.lastAccessed = now
        
        if (!hotCache.contains(key)) {
            removeFromAccessOrder(key)
            accessOrder.add(key)
        }
        
        stats.hits++
        @Suppress("UNCHECKED_CAST")
        return entry.data as? T
    }
    
    fun has(key: String): Boolean {
        val entry = cache[key] ?: return false
        
        val now = System.currentTimeMillis()
        if (now - entry.timestamp > entry.ttl) {
            delete(key)
            return false
        }
        
        return true
    }
    
    fun delete(key: String): Boolean {
        val entry = cache.remove(key) ?: return false
        
        notifySegmentRemoval(key)
        
        hotCache.remove(key)
        currentSize -= entry.size
        removeFromAccessOrder(key)
        updateStats()
        
        return true
    }
    
    fun clear() {
        cache.clear()
        hotCache.clear()
        accessOrder.clear()
        segmentTimeMaps.clear()
        currentSize = 0
        updateStats()
        Log.d(TAG, "Cache cleared")
    }
    
    // ============ Segment Operations ============
    
    fun setSegment(movieId: String, qualityId: String, segmentId: String, data: ByteArray) {
        val key = getSegmentKey(movieId, qualityId, segmentId)
        set(key, data, config.segmentTTL, false)
    }
    
    fun getSegment(movieId: String, qualityId: String, segmentId: String): ByteArray? {
        val key = getSegmentKey(movieId, qualityId, segmentId)
        return get(key)
    }
    
    fun hasSegment(movieId: String, qualityId: String, segmentId: String): Boolean {
        val key = getSegmentKey(movieId, qualityId, segmentId)
        return has(key)
    }
    
    // ============ Init Segment Operations ============
    
    fun setInitSegment(movieId: String, qualityId: String, data: InitSegment) {
        val key = getInitSegmentKey(movieId, qualityId)
        set(key, data, config.initTTL, true)
    }
    
    fun getInitSegment(movieId: String, qualityId: String): InitSegment? {
        val key = getInitSegmentKey(movieId, qualityId)
        return get(key)
    }
    
    fun hasInitSegment(movieId: String, qualityId: String): Boolean {
        val key = getInitSegmentKey(movieId, qualityId)
        return has(key)
    }
    
    // ============ Playlist Operations ============
    
    fun setMasterPlaylist(movieId: String, playlist: MasterPlaylist) {
        val key = getMasterPlaylistKey(movieId)
        set(key, playlist, config.playlistTTL, true)
    }
    
    fun getMasterPlaylist(movieId: String): MasterPlaylist? {
        val key = getMasterPlaylistKey(movieId)
        return get(key)
    }
    
    fun setVariantPlaylist(movieId: String, qualityId: String, playlist: VariantPlaylist) {
        val key = getVariantPlaylistKey(movieId, qualityId)
        set(key, playlist, config.playlistTTL, true)
        
        // Build segment time map for seek optimization
        buildSegmentTimeMap(movieId, qualityId, playlist.segments)
    }
    
    fun getVariantPlaylist(movieId: String, qualityId: String): VariantPlaylist? {
        val key = getVariantPlaylistKey(movieId, qualityId)
        return get(key)
    }
    
    // ============ Time Mapping for Seek ============
    
    fun buildSegmentTimeMap(movieId: String, qualityId: String, segments: List<SegmentMetadata>) {
        val mapKey = "$movieId:$qualityId"
        segmentTimeMaps[mapKey] = segments.sortedBy { it.timestamp }
        Log.d(TAG, "Built segment time map for $qualityId: ${segments.size} segments")
    }
    
    fun mapTimeToSegmentId(movieId: String, qualityId: String, time: Float): String? {
        val mapKey = "$movieId:$qualityId"
        val segments = segmentTimeMaps[mapKey] ?: return null
        
        return segments.find { segment ->
            time >= segment.timestamp && time < segment.timestamp + segment.duration
        }?.id
    }
    
    fun getSegmentsAroundTime(
        movieId: String,
        qualityId: String,
        time: Float,
        before: Int,
        after: Int
    ): List<SegmentMetadata> {
        val mapKey = "$movieId:$qualityId"
        val segments = segmentTimeMaps[mapKey] ?: return emptyList()
        
        val targetIndex = segments.indexOfFirst { segment ->
            time >= segment.timestamp && time < segment.timestamp + segment.duration
        }
        
        if (targetIndex == -1) return emptyList()
        
        val startIndex = maxOf(0, targetIndex - before)
        val endIndex = minOf(segments.size, targetIndex + after + 1)
        
        return segments.subList(startIndex, endIndex)
    }
    
    // ============ Statistics ============
    
    fun getStats(): CacheStats = stats.copy(currentSize = currentSize, itemCount = cache.size)
    
    fun getHitRate(): Float {
        val total = stats.hits + stats.misses
        return if (total > 0) stats.hits.toFloat() / total else 0f
    }
    
    fun getUsagePercentage(): Float {
        return if (config.maxSize > 0) (currentSize.toFloat() / config.maxSize) * 100 else 0f
    }
    
    // ============ Private Helper Methods ============
    
    private fun calculateSize(data: Any): Long {
        return when (data) {
            is ByteArray -> data.size.toLong()
            is InitSegment -> data.data.size.toLong()
            is MasterPlaylist -> 1024L // Approximate
            is VariantPlaylist -> 2048L // Approximate
            else -> 0L
        }
    }
    
    private fun evict() {
        // LRU eviction - skip hot cache entries
        val keyToEvict = accessOrder.firstOrNull { !hotCache.contains(it) } ?: return
        
        Log.d(TAG, "Evicting cache entry: $keyToEvict")
        delete(keyToEvict)
        stats.evictions++
    }
    
    private fun removeFromAccessOrder(key: String) {
        accessOrder.remove(key)
    }
    
    private fun updateStats() {
        stats.currentSize = currentSize
        stats.itemCount = cache.size
    }
    
    private fun notifySegmentRemoval(key: String) {
        // Check if this is a media segment and notify if needed
        val parts = key.split(":")
        if (parts.size == 3 && parts[2].startsWith("seg_")) {
            val (movieId, qualityId, segmentId) = parts
            onSegmentRemoved?.invoke(movieId, qualityId, segmentId)
        }
    }
    
    private fun getSegmentKey(movieId: String, qualityId: String, segmentId: String) =
        "$movieId:$qualityId:$segmentId"
    
    private fun getInitSegmentKey(movieId: String, qualityId: String) =
        "$movieId:$qualityId:init"
    
    private fun getMasterPlaylistKey(movieId: String) =
        "$movieId:master"
    
    private fun getVariantPlaylistKey(movieId: String, qualityId: String) =
        "$movieId:$qualityId:playlist"
}
