package vn.tutorial.cinemate.presentation.streaming

import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

/**
 * BufferManager - P2P optimization layer
 * 
 * NOTE: With SimpleMseManager using HLS, ExoPlayer handles all buffering automatically.
 * This manager now serves as an OPTIONAL P2P optimization layer:
 * - Prefetches segments via P2P when available
 * - Caches segments for sharing with other peers
 * - Monitors buffer health and emits events
 * 
 * ExoPlayer will ALWAYS fetch segments via HTTP as fallback, so this is not critical path.
 */
class BufferManager(
    private val mseManager: SimpleMseManager,
    private val configManager: ConfigManager
) : EventEmitter<BufferManagerEvents>() {

    private val TAG = "Logging BufferManager"

    // State
    private var currentQuality: Quality? = null
    private var segments: List<SegmentMetadata> = emptyList()
    private var isInitialized = false
    private var isPaused = false

    // Buffering state
    private val bufferedSegments = ConcurrentHashMap<String, Boolean>()
    private val pendingSegments = ConcurrentHashMap<String, Job>()
    private var bufferingJob: Job? = null

    // Fetch callback
    private var fetchCallback: (suspend (SegmentMetadata, Boolean) -> ByteArray?)? = null

    // Cache manager
    private var cacheManager: CacheManager? = null

    // Timing
    private var lastBufferCheckTime = 0L
    private var lastCriticalFetchTime = 0L
    private var lastBufferLowEmitTime = 0L
    private var lastBufferCriticalEmitTime = 0L
    private val bufferCheckInterval = 1000L // 1 second

    // Coroutine scope
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun initialize(quality: Quality, segmentList: List<SegmentMetadata>) {
        currentQuality = quality
        segments = segmentList
        isInitialized = true

        Log.d(TAG, "Initialized with quality ${quality.id}, ${segments.size} segments")

        startBuffering()
    }

    fun setCacheManager(manager: CacheManager) {
        cacheManager = manager
    }

    fun setFetchCallback(callback: suspend (SegmentMetadata, Boolean) -> ByteArray?) {
        fetchCallback = callback
    }

    private fun startBuffering() {
        bufferingJob?.cancel()
        bufferingJob = scope.launch {
            while (isActive && isInitialized) {
                try {
                    checkAndPrefetchSegments()
                    delay(bufferCheckInterval)
                } catch (e: Exception) {
                    Log.e(TAG, "Error in buffering loop", e)
                }
            }
        }
    }

    private suspend fun checkAndPrefetchSegments() {
        if (!isInitialized || isPaused) return
        
        // Don't check buffer if no segments or no fetch callback
        if (segments.isEmpty() || fetchCallback == null) return

        val now = System.currentTimeMillis()
        if (now - lastBufferCheckTime < bufferCheckInterval) return
        lastBufferCheckTime = now

        val config = configManager.getConfig()
        val currentTime = mseManager.getCurrentTime()
        val bufferStatus = getBufferStatus()

        // Check buffer health
        if (bufferStatus.bufferAhead < config.bufferMinThreshold) {
            // Debounce bufferLow event (emit max once per 3 seconds)
            if (now - lastBufferLowEmitTime > 3000L) {
                Log.d(TAG, "Buffer LOW: ahead=${bufferStatus.bufferAhead}s, threshold=${config.bufferMinThreshold}s")
                emit("bufferLow", bufferStatus.bufferAhead)
                lastBufferLowEmitTime = now
            }

            if (bufferStatus.bufferAhead < config.minBufferPrefetch) {
                // Debounce bufferCritical event (emit max once per 5 seconds)
                if (now - lastBufferCriticalEmitTime > 5000L) {
                    Log.w(TAG, "Buffer CRITICAL: ahead=${bufferStatus.bufferAhead}s, minPrefetch=${config.minBufferPrefetch}s, segments=${segments.size}, hasCallback=${fetchCallback != null}")
                    emit("bufferCritical", bufferStatus.bufferAhead)
                    lastBufferCriticalEmitTime = now
                }
                handleCriticalBuffer(currentTime)
            }
        }

        // Prefetch segments ahead
        prefetchSegmentsAhead(currentTime, config)
    }

    private suspend fun handleCriticalBuffer(currentTime: Float) {
        val now = System.currentTimeMillis()
        if (now - lastCriticalFetchTime < 2000L) return // Debounce critical fetches
        lastCriticalFetchTime = now

        emit("bufferingStart")

        // Find next unbuffered segment
        val nextSegment = findNextUnbufferedSegment(currentTime) ?: return

        Log.d(TAG, "Critical buffer: fetching segment ${nextSegment.id}")

        val data = fetchCallback?.invoke(nextSegment, true)
        if (data != null) {
            // With HLS, ExoPlayer handles buffering automatically
            // We just cache for P2P sharing
            bufferedSegments[nextSegment.id] = true
            Log.d(TAG, "Critical segment ${nextSegment.id} fetched and cached (${data.size} bytes)")
        }
    }

    private suspend fun prefetchSegmentsAhead(currentTime: Float, config: StreamingConfig) {
        val quality = currentQuality ?: return

        // Calculate time window for prefetch
        val prefetchStart = currentTime
        val prefetchEnd = currentTime + config.prefetchWindowAhead

        // Get segments in window
        val segmentsToPrefetch = segments.filter { segment ->
            segment.timestamp >= prefetchStart &&
                    segment.timestamp < prefetchEnd &&
                    segment.qualityId == quality.id &&
                    !isSegmentBuffered(segment.id) &&
                    !isPendingSegment(segment.id)
        }.take(config.maxConcurrentFetches)

        if (segmentsToPrefetch.isEmpty()) return

        Log.d(TAG, "Prefetching ${segmentsToPrefetch.size} segments ahead")

        // Fetch segments with staggered delays
        segmentsToPrefetch.forEachIndexed { index, segment ->
            if (pendingSegments.size >= config.maxConcurrentFetches) return

            val job = scope.launch {
                delay(index * config.staggeredRequestDelay)
                fetchAndBufferSegment(segment, false)
            }

            pendingSegments[segment.id] = job
        }
    }

    private suspend fun fetchAndBufferSegment(segment: SegmentMetadata, critical: Boolean) {
        if (isSegmentBuffered(segment.id)) {
            pendingSegments.remove(segment.id)
            return
        }

        try {
            val data = fetchCallback?.invoke(segment, critical)

            if (data != null) {
                // With HLS, ExoPlayer handles segment appending automatically
                // We just cache the data for P2P sharing
                bufferedSegments[segment.id] = true
                Log.d(TAG, "Fetched segment ${segment.id} (${data.size} bytes) - cached for P2P")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch segment ${segment.id}", e)
        } finally {
            pendingSegments.remove(segment.id)
        }
    }

    suspend fun handleSeek(time: Float) {
        Log.d(TAG, "Handling seek to ${time}s")

        val config = configManager.getConfig()

        // Clear pending fetches
        pendingSegments.values.forEach { it.cancel() }
        pendingSegments.clear()

        // Find segment at seek point
        val targetSegment = findSegmentAtTime(time) ?: return

        // Prefetch segments around seek point
        val segmentsToFetch = getSegmentsAroundTime(
            time,
            config.seekPrefetchBehind,
            config.seekPrefetchAhead
        )

        Log.d(TAG, "Prefetching ${segmentsToFetch.size} segments around seek point")

        // Fetch segments with priority to current position
        segmentsToFetch.forEachIndexed { index, segment ->
            val priority = if (segment.id == targetSegment.id) 100 else 50 - index

            scope.launch {
                delay(index * 50L)
                fetchAndBufferSegment(segment, segment.id == targetSegment.id)
            }
        }
    }

    suspend fun switchQuality(
        newQuality: Quality,
        newSegments: List<SegmentMetadata>,
        initSegment: InitSegment,
        skipInitAppend: Boolean = false
    ) {
        Log.d(TAG, "Switching quality to ${newQuality.id}")

        emit("qualitySwitch", newQuality)

        // Cancel pending fetches
        pendingSegments.values.forEach { it.cancel() }
        pendingSegments.clear()
        bufferedSegments.clear()

        // Update state
        currentQuality = newQuality
        segments = newSegments

        // With HLS, ExoPlayer handles quality switching automatically
        // Init segment is managed by the HLS player
        Log.d(TAG, "Quality switched to ${newQuality.id}, HLS player will handle segment loading")

        // Start buffering new quality
        val currentTime = mseManager.getCurrentTime()
        handleSeek(currentTime)
    }

    suspend fun getBufferStatus(): BufferStatus {
        val currentTime = mseManager.getCurrentTime()
        val duration = mseManager.getDuration()
        val bufferedRanges = mseManager.getBufferedRanges()

        var bufferAhead = 0f
        var bufferBehind = 0f

        // Find buffer ahead and behind current time
        for (range in bufferedRanges) {
            if (range.start <= currentTime && range.end >= currentTime) {
                bufferAhead = range.end - currentTime
                bufferBehind = currentTime - range.start
                break
            }
        }

        return BufferStatus(
            buffered = bufferedRanges,
            currentTime = currentTime,
            duration = duration,
            bufferAhead = bufferAhead,
            bufferBehind = bufferBehind
        )
    }

    fun pause() {
        isPaused = true
        Log.d(TAG, "Buffering paused")
    }

    fun resume() {
        isPaused = false
        Log.d(TAG, "Buffering resumed")
    }

    private fun isSegmentBuffered(segmentId: String): Boolean {
        return bufferedSegments.containsKey(segmentId)
    }

    private fun isPendingSegment(segmentId: String): Boolean {
        return pendingSegments.containsKey(segmentId)
    }

    private fun findNextUnbufferedSegment(currentTime: Float): SegmentMetadata? {
        return segments.find { segment ->
            segment.timestamp >= currentTime &&
                    !isSegmentBuffered(segment.id) &&
                    segment.qualityId == currentQuality?.id
        }
    }

    private fun findSegmentAtTime(time: Float): SegmentMetadata? {
        return segments.find { segment ->
            time >= segment.timestamp &&
                    time < segment.timestamp + segment.duration &&
                    segment.qualityId == currentQuality?.id
        }
    }

    private fun getSegmentsAroundTime(
        time: Float,
        countBefore: Int,
        countAfter: Int
    ): List<SegmentMetadata> {
        val quality = currentQuality ?: return emptyList()

        val qualitySegments = segments.filter { it.qualityId == quality.id }
        val currentIndex = qualitySegments.indexOfFirst {
            time >= it.timestamp && time < it.timestamp + it.duration
        }

        if (currentIndex == -1) return emptyList()

        val startIndex = max(0, currentIndex - countBefore)
        val endIndex = (currentIndex + countAfter).coerceAtMost(qualitySegments.size - 1)

        return qualitySegments.subList(startIndex, endIndex + 1)
    }

    fun clear() {
        bufferingJob?.cancel()
        pendingSegments.values.forEach { it.cancel() }
        pendingSegments.clear()
        bufferedSegments.clear()
        scope.cancel()
        super.destroy()

        Log.d(TAG, "BufferManager destroyed")
    }
}

