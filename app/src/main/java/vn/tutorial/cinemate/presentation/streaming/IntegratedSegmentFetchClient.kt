package vn.tutorial.cinemate.presentation.streaming

import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

class IntegratedSegmentFetchClient(
    private val movieId: String,
    private val signalingClient: SignalingClient,
    private val peerManager: PeerManager,
    private val cacheManager: CacheManager,
    private val segmentFetcher: SegmentFetcher,
    private val mseManager: MsePlayer,
    private val configManager: ConfigManager
) {
    
    private val TAG = "Logging IntegratedFetchClient"
    
    private val activeFetches = ConcurrentHashMap<String, Deferred<FetchResult>>()
    private val criticalFetchedSegments = mutableSetOf<String>()
    
    private val stats = SegmentFetchStats()
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    data class SegmentFetchStats(
        var totalFetches: Int = 0,
        var p2pFetches: Int = 0,
        var httpFetches: Int = 0,
        var cacheFetches: Int = 0,
        var failedFetches: Int = 0,
        var avgP2pLatency: Long = 0,
        var avgHttpLatency: Long = 0
    )
    
    data class SegmentFetchRequest(
        val segment: SegmentMetadata,
        val priority: Int,
        val forSeek: Boolean = false,
        val critical: Boolean = false
    )
    
    suspend fun fetchSegment(request: SegmentFetchRequest): FetchResult {
        val segment = request.segment
        val segmentKey = getSegmentKey(segment)
        
        // Check if already fetching
        activeFetches[segmentKey]?.let { existingFetch ->
            try {
                return existingFetch.await()
            } catch (e: Exception) {
                Log.w(TAG, "Existing fetch for $segmentKey failed, will retry: ${e.message}")
                activeFetches.remove(segmentKey)
            }
        }
        
        // Start new fetch
        val config = configManager.getConfig()
        val fetchTimeout = config.fetchTimeout
        
        val isCritical = request.critical || request.forSeek
        val fetchDeferred = scope.async {
            executeSegmentFetch(segment, isCritical)
        }
        
        activeFetches[segmentKey] = fetchDeferred
        
        return try {
            if (isCritical) {
                // No timeout for critical segments
                fetchDeferred.await()
            } else {
                withTimeout(fetchTimeout) {
                    fetchDeferred.await()
                }
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Segment fetch timeout for $segmentKey")
            FetchResult(
                success = false,
                source = FetchSource.SEEDER,
                latency = 0,
                error = Exception("Timeout after ${fetchTimeout}ms")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching $segmentKey", e)
            FetchResult(
                success = false,
                source = FetchSource.SEEDER,
                latency = 0,
                error = e
            )
        } finally {
            activeFetches.remove(segmentKey)
        }
    }
    
    private suspend fun executeSegmentFetch(
        segment: SegmentMetadata,
        critical: Boolean
    ): FetchResult {
        val segmentKey = getSegmentKey(segment)
        val startTime = System.currentTimeMillis()
        
        stats.totalFetches++
        
        // Step 1: Check cache
        val cached = cacheManager.getSegment(movieId, segment.qualityId, segment.id)
        if (cached != null) {
            stats.cacheFetches++
            Log.d(TAG, "✓ Cache hit for $segmentKey")
            
            return FetchResult(
                success = true,
                data = cached,
                source = FetchSource.CACHE,
                latency = System.currentTimeMillis() - startTime
            )
        }
        
        // Step 2: If CRITICAL, skip P2P and fetch directly from Seeder
        if (critical) {
            Log.d(TAG, "CRITICAL mode - fetching $segmentKey directly from Seeder (skipping P2P)")
            
            criticalFetchedSegments.add(segmentKey)
            
            val httpResult = fetchFromSeeder(segment)
            
            if (httpResult.success && httpResult.data != null) {
                stats.httpFetches++
                updateHttpLatency(httpResult.latency)
                
                // Cache the segment
                cacheManager.setSegment(movieId, segment.qualityId, segment.id, httpResult.data)
                
                // Report to signaling server
                signalingClient.reportSegmentFetch(segment.id, segment.qualityId, "server")
                Log.d(TAG, "✓ CRITICAL segment $segmentKey fetched from Seeder")
                
                return httpResult
            }
            
            // Critical fetch failed
            Log.e(TAG, "✗ CRITICAL fetch failed for $segmentKey")
            stats.failedFetches++
            
            return FetchResult(
                success = false,
                source = FetchSource.SEEDER,
                latency = System.currentTimeMillis() - startTime,
                error = Exception("Failed to fetch critical segment")
            )
        }
        
        // Step 3: Query signaling for peers with segment
        var peerIds = emptyList<String>()
        if (!criticalFetchedSegments.contains(segmentKey)) {
            Log.d(TAG, "Querying WhoHas for $segmentKey...")
            
            try {
                val whoHasResponse = signalingClient.whoHas(segment.qualityId, segment.id)
                peerIds = whoHasResponse.peers.map { it.peerId }
                Log.d(TAG, "WhoHas response for $segmentKey: ${peerIds.size} peers found")
            } catch (e: Exception) {
                Log.w(TAG, "WhoHas query failed for $segmentKey: ${e.message}")
            }
        } else {
            Log.d(TAG, "Skipping P2P query for $segmentKey (previously fetched as critical)")
        }
        
        // Step 4: Try P2P fetch if peers available
        if (peerIds.isNotEmpty()) {
            val p2pResult = fetchFromPeers(segment, peerIds)
            
            if (p2pResult.success && p2pResult.data != null) {
                Log.d(TAG, "✓ P2P success for $segmentKey from ${p2pResult.peerId}")
                stats.p2pFetches++
                updateP2pLatency(p2pResult.latency)
                
                // Cache the segment
                cacheManager.setSegment(movieId, segment.qualityId, segment.id, p2pResult.data)
                
                // Report to signaling server
                signalingClient.reportSegmentFetch(segment.id, segment.qualityId, "peer")
                
                return p2pResult
            }
            
            Log.w(TAG, "P2P fetch failed for $segmentKey, falling back to HTTP")
        }
        
        // Step 5: Fallback to HTTP from Seeder
        Log.d(TAG, "Fetching $segmentKey from Seeder (HTTP fallback)")
        
        val httpResult = fetchFromSeeder(segment)
        
        if (httpResult.success && httpResult.data != null) {
            stats.httpFetches++
            updateHttpLatency(httpResult.latency)
            
            // Cache the segment
            cacheManager.setSegment(movieId, segment.qualityId, segment.id, httpResult.data)
            
            // Report to signaling server
            signalingClient.reportSegmentFetch(
                segment.id,
                segment.qualityId,
                "server",
                httpResult.latency
            )
            
            Log.d(TAG, "✓ HTTP success for $segmentKey")
            return httpResult
        }
        
        // All methods failed
        Log.e(TAG, "✗ All fetch methods failed for $segmentKey")
        stats.failedFetches++
        
        return FetchResult(
            success = false,
            source = FetchSource.SEEDER,
            latency = System.currentTimeMillis() - startTime,
            error = Exception("All fetch methods failed")
        )
    }
    
    private suspend fun fetchFromPeers(
        segment: SegmentMetadata,
        peerIds: List<String>
    ): FetchResult {
        val segmentKey = "${segment.qualityId}:${segment.id}"
        
        // Get best peers for this segment
        val bestPeers = peerManager.getBestPeersForSegment(segmentKey, 3)
            .ifEmpty { peerIds.take(3) }
        
        // Try peers in order of score
        for (peerId in bestPeers) {
            try {
                // Try to connect to peer if not already connected
                peerManager.connectToPeer(peerId)
                
                // Fetch segment from peer
                val result = peerManager.fetchSegmentFromPeer(peerId, segment)
                
                if (result.success && result.data != null) {
                    return result
                }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to fetch from peer $peerId: ${e.message}")
                continue
            }
        }
        
        return FetchResult(
            success = false,
            source = FetchSource.PEER,
            latency = 0,
            error = Exception("No peers could provide segment")
        )
    }
    
    private suspend fun fetchFromSeeder(segment: SegmentMetadata): FetchResult {
        return segmentFetcher.fetchFromSeeder(segment)
    }
    
    private fun updateP2pLatency(latency: Long) {
        if (stats.p2pFetches > 0) {
            stats.avgP2pLatency = ((stats.avgP2pLatency * (stats.p2pFetches - 1)) + latency) / stats.p2pFetches
        } else {
            stats.avgP2pLatency = latency
        }
    }
    
    private fun updateHttpLatency(latency: Long) {
        if (stats.httpFetches > 0) {
            stats.avgHttpLatency = ((stats.avgHttpLatency * (stats.httpFetches - 1)) + latency) / stats.httpFetches
        } else {
            stats.avgHttpLatency = latency
        }
    }
    
    fun getStats(): SegmentFetchStats = stats.copy()
    
    fun getP2pRatio(): Float {
        val total = stats.p2pFetches + stats.httpFetches
        return if (total > 0) {
            stats.p2pFetches.toFloat() / total
        } else 0f
    }
    
    private fun getSegmentKey(segment: SegmentMetadata): String {
        return "${segment.qualityId}:${segment.id}"
    }
    
    fun destroy() {
        activeFetches.clear()
        criticalFetchedSegments.clear()
        scope.cancel()
        Log.d(TAG, "IntegratedSegmentFetchClient destroyed")
    }
}
