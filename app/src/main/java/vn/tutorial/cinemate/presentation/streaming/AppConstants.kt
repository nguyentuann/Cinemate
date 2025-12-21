package vn.tutorial.cinemate.presentation.streaming

import org.webrtc.PeerConnection

object AppConstants {
    
    // File extensions and formats
    object FileExtensions {
        const val MASTER_PLAYLIST = ".m3u8"
        const val VARIANT_PLAYLIST = ".m3u8"
        const val INIT_SEGMENT = ".mp4"
        const val MEDIA_SEGMENT = ".m4s"
    }
    
    // Segment naming patterns
    object SegmentPatterns {
        const val PREFIX = "seg_"
        val REGEX = Regex("""seg_(\d+)\.m4s""")
        const val PADDING_LENGTH = 4
        const val PADDING_CHAR = '0'
    }
    
    // API paths
    object ApiPaths {
        const val STREAMS_BASE = "/streams/movies"
        const val MASTER_PLAYLIST = "master.m3u8"
        const val VARIANT_PLAYLIST = "playlist.m3u8"
        const val INIT_SEGMENT = "init.mp4"
    }
    
    // Timing constants (in milliseconds)
    object Timing {
        const val MONITORING_INTERVAL = 1000L
        const val CLEANUP_INTERVAL = 10000L
        const val CRITICAL_FETCH_DEBOUNCE = 1000L
        const val CACHE_CLEANUP_INTERVAL = 5 * 60 * 1000L // 5 minutes
        const val SIGNALING_DEBOUNCE = 500L
        const val CONNECTION_TIMEOUT = 5000L
        const val WHOHAS_CACHE_TTL = 5000L
        const val RECONNECT_CLEANUP_DELAY = 100L
    }
    
    // Bandwidth calculation constants
    object Bandwidth {
        const val BITS_PER_BYTE = 8
        const val MS_TO_SECONDS = 1000
    }
    
    // WebRTC configuration
    object WebRTC {
        val ICE_SERVERS = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer()
        )
        
        object DataChannel {
            const val NAME = "segments"
            const val ORDERED = true
            const val MAX_RETRANSMITS = 3
        }
    }
    
    // Cache configuration defaults
    object CacheDefaults {
        const val MAX_SIZE = 500L * 1024 * 1024 // 500MB
        const val SEGMENT_TTL = 30L * 60 * 1000 // 30 minutes
        const val INIT_TTL = 24L * 60 * 60 * 1000 // 24 hours
        const val PLAYLIST_TTL = 60L * 60 * 1000 // 1 hour
        const val HOT_CACHE_PROTECTION = true
    }
    
    // WebSocket configuration
    object WebSocket {
        const val PROTOCOL = "ws"
        const val PATH = "/ws/signaling"
        const val DEFAULT_PORT = 8080
        const val URL_TEMPLATE = "{protocol}://{host}:{port}{path}?clientId={clientId}&movieId={movieId}"
    }
    
    // MIME types
    object MimeTypes {
        const val DEFAULT_VIDEO = "video/mp4; codecs=\"avc1.64001f,mp4a.40.2\""
    }
}
