package vn.tutorial.cinemate.presentation.streaming

import org.webrtc.DataChannel
import org.webrtc.PeerConnection
import vn.tutorial.cinemate.core.constant.api_endpoint.BaseEndpoint

// ============ Quality & Segment Types ============

data class Quality(
    val id: String,
    val bandwidth: Long,
    val width: Int,
    val height: Int,
    val codecs: String,
    val frameRate: Float? = null
)

data class Segment(
    val id: String,
    val qualityId: String,
    val duration: Float,
    val url: String,
    val byteRange: ByteRange? = null,
    val timestamp: Float
)

data class ByteRange(
    val start: Long,
    val end: Long
)

data class SegmentMetadata(
    val id: String,
    val movieId: String,
    val qualityId: String,
    val duration: Float,
    val size: Long? = null,
    val timestamp: Float
)

data class InitSegment(
    val qualityId: String,
    val data: ByteArray,
    val url: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as InitSegment
        if (qualityId != other.qualityId) return false
        if (!data.contentEquals(other.data)) return false
        if (url != other.url) return false
        return true
    }

    override fun hashCode(): Int {
        var result = qualityId.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + url.hashCode()
        return result
    }
}

// ============ Playlist Types ============

data class MasterPlaylist(
    val qualities: List<Quality>,
    val defaultQualityId: String? = null
)

data class VariantPlaylist(
    val qualityId: String,
    val segments: List<SegmentMetadata>,
    val targetDuration: Float,
    val totalDuration: Float
)

// ============ Peer & P2P Types ============

data class PeerInfo(
    val peerId: String,
    var connectionState: ConnectionState,
    var dataChannel: DataChannel? = null,
    var peerConnection: PeerConnection? = null,
    var score: PeerScoreDetails,
    val availableSegments: MutableSet<String> = mutableSetOf(),
    var lastActive: Long,
    val metrics: PeerMetrics
)

enum class ConnectionState {
    NEW, CONNECTING, CONNECTED, DISCONNECTED, FAILED
}

data class PeerScoreDetails(
    var overall: Float,
    var latency: Float,
    var uploadSpeed: Float,
    var reliability: Float
)

data class PeerMetrics(
    var successCount: Int = 0,
    var failureCount: Int = 0,
    var avgLatency: Long = 0,
    var bytesReceived: Long = 0
)

data class PeerScore(
    val peerId: String,
    val score: Float,
    val latency: Long,
    val successRate: Float,
    val availability: Float
)

// ============ Buffer Types ============

data class BufferRange(
    val start: Float,
    val end: Float
)

data class BufferStatus(
    val buffered: List<BufferRange>,
    val currentTime: Float,
    val duration: Float,
    val bufferAhead: Float,
    val bufferBehind: Float
)

// ============ Cache Types ============

data class CacheEntry<T>(
    val key: String,
    val data: T,
    val size: Long,
    val timestamp: Long,
    val ttl: Long,
    var accessCount: Int = 0,
    var lastAccessed: Long = System.currentTimeMillis()
)

data class CacheStats(
    var hits: Long = 0,
    var misses: Long = 0,
    var evictions: Long = 0,
    var currentSize: Long = 0,
    val maxSize: Long = 0,
    var itemCount: Int = 0
)

// ============ Fetch Types ============

enum class FetchSource {
    PEER, SEEDER, ORIGIN, CACHE
}

data class FetchResult(
    val success: Boolean,
    val data: ByteArray? = null,
    val source: FetchSource,
    val peerId: String? = null,
    val latency: Long,
    val error: Exception? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as FetchResult
        if (success != other.success) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false
        if (source != other.source) return false
        if (peerId != other.peerId) return false
        if (latency != other.latency) return false
        return true
    }

    override fun hashCode(): Int {
        var result = success.hashCode()
        result = 31 * result + (data?.contentHashCode() ?: 0)
        result = 31 * result + source.hashCode()
        result = 31 * result + (peerId?.hashCode() ?: 0)
        result = 31 * result + latency.hashCode()
        return result
    }
}

// ============ Signaling Types ============

interface SignalingMessage {
    val type: String
}

// CLIENT -> SERVER

data class WhoHasRequest(
    override val type: String = "whoHas",
    val movieId: String,
    val qualityId: String,
    val segmentId: String
) : SignalingMessage

data class ReportSegmentRequest(
    override val type: String = "reportSegment",
    val movieId: String? = null,
    val qualityId: String,
    val segmentId: String,
    val source: String = "peer",
    val latency: Long? = null,
    val speed: Float? = null
) : SignalingMessage

data class RtcOfferRequest(
    override val type: String = "rtcOffer",
    val from: String? = null,
    val to: String,
    val streamId: String,
    val sdp: String
) : SignalingMessage

data class RtcAnswerRequest(
    override val type: String = "rtcAnswer",
    val from: String? = null,
    val to: String,
    val streamId: String,
    val sdp: String
) : SignalingMessage

data class IceCandidateRequest(
    override val type: String = "iceCandidate",
    val from: String? = null,
    val to: String,
    val streamId: String,
    val candidate: Map<String, Any?>
) : SignalingMessage

// SERVER -> CLIENT

data class PeerListMessage(
    override val type: String = "peerList",
    val streamId: String,
    val peers: List<String>
) : SignalingMessage

data class SignalingPeerMetrics(
    val uploadSpeed: Float,
    val latency: Long,
    val successRate: Float,
    val lastActive: Long
)

data class SignalingPeerInfo(
    val peerId: String,
    val metrics: SignalingPeerMetrics
)

data class WhoHasReplyMessage(
    override val type: String = "whoHasReply",
    val qualityId: String,
    val segmentId: String,
    val peers: List<SignalingPeerInfo>
) : SignalingMessage

data class ReportAckMessage(
    override val type: String = "reportAck",
    val segmentId: String
) : SignalingMessage

data class RtcOfferMessage(
    override val type: String = "rtcOffer",
    val from: String,
    val to: String,
    val streamId: String,
    val sdp: String
) : SignalingMessage

data class RtcAnswerMessage(
    override val type: String = "rtcAnswer",
    val from: String,
    val to: String,
    val streamId: String,
    val sdp: String
) : SignalingMessage

data class IceCandidateMessage(
    override val type: String = "iceCandidate",
    val from: String,
    val to: String,
    val streamId: String,
    val candidate: Map<String, Any?>
) : SignalingMessage

data class ErrorMessage(
    override val type: String = "error",
    val message: String
) : SignalingMessage

// ============ Player State Types ============

data class PlayerState(
    val isPlaying: Boolean,
    val isPaused: Boolean,
    val isSeeking: Boolean,
    val isBuffering: Boolean,
    val currentTime: Float,
    val duration: Float,
    val currentQuality: Quality?,
    val availableQualities: List<Quality>,
    val volume: Float,
    val muted: Boolean
)

data class PlaybackMetrics(
    val bufferHealth: Float,
    val downloadSpeed: Long,
    val bandwidthEstimate: Long,
    val droppedFrames: Long,
    val stallCount: Int,
    val totalStallTime: Long,
    val p2pRatio: Float,
    val activeConnections: Int
)

// ============ Configuration Types ============

data class StreamingConfig(
    // Peer settings
    val maxActivePeers: Int = 6,
    val minActivePeers: Int = 2,
    val peerConnectionTimeout: Long = 5000,
    val peerScoreThreshold: Float = 0.3f,

    // Buffer settings
    val prefetchWindowAhead: Float = 60f,
    val prefetchWindowBehind: Float = 20f,
    val bufferTargetDuration: Float = 30f,
    val bufferMinThreshold: Float = 8f,
    val bufferMaxThreshold: Float = 60f,
    val minBufferPrefetch: Float = 2f,

    // ABR settings
    val abrEnabled: Boolean = true,
    val abrSwitchUpThreshold: Float = 0.8f,
    val abrSwitchDownThreshold: Float = 0.3f,
    val bandwidthEstimationWindow: Int = 5,

    // Cache settings
    val cacheSizeLimit: Long = 500 * 1024 * 1024,
    val cacheSegmentTTL: Long = 30 * 60 * 1000,
    val cachePlaylistTTL: Long = 60 * 60 * 1000,
    val cacheInitSegmentTTL: Long = 24 * 60 * 60 * 1000,

    // Fetch settings
    val maxConcurrentFetches: Int = 4,
    val fetchTimeout: Long = 10000,
    val maxRetries: Int = 3,
    val retryDelayBase: Long = 1000,
    val staggeredRequestDelay: Long = 100,
    val segmentRequestWaitMin: Long = 50,
    val segmentRequestWaitMax: Long = 200,

    // Signaling settings
    val signalingReconnectInterval: Long = 5000,
    val signalingHeartbeatInterval: Long = 30000,
    val whoHasTimeout: Long = 3000,
    val signalingUrlTemplate: String? = null,

    // Seek optimization settings
    val seekPrefetchAhead: Int = 5,
    val seekPrefetchBehind: Int = 2,

    // API endpoints
    val baseUrl: String = BaseEndpoint.BASE_URL
)

// ============ Utility Functions ============

fun compareSegmentIds(a: String, b: String): Int {
    return a.compareTo(b)
}
