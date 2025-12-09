# Android Kotlin Streaming Player

Đây là phiên bản Android Kotlin được chuyển đổi từ TypeScript/React codebase, sử dụng Jetpack Compose cho UI.

## Tổng quan kiến trúc

### Luồng hoạt động chính:

```
StreamingPlayerCoordinator (điều phối tổng thể)
    ├── ConfigManager (quản lý cấu hình)
    ├── SignalingClient (WebSocket với signaling server)
    ├── PeerManager (quản lý WebRTC P2P connections)
    ├── CacheManager (LRU cache với hot cache protection)
    ├── SegmentFetcher (HTTP fallback)
    ├── IntegratedSegmentFetchClient (P2P + HTTP hybrid)
    ├── MseManager/ExoPlayer (phát video)
    ├── BufferManager (quản lý buffer, prefetch)
    └── AbrManager (adaptive bitrate switching)
```

## Các thành phần chính

### 1. Types.kt
Định nghĩa tất cả data classes và types:
- Quality, Segment, SegmentMetadata
- PeerInfo, BufferStatus, CacheStats
- SignalingMessage và các subtypes
- PlayerState, PlaybackMetrics
- StreamingConfig

### 2. AppConstants.kt
Chứa tất cả constants:
- File extensions, segment patterns
- API paths
- Timing constants
- WebRTC configuration
- Cache defaults
- WebSocket configuration

### 3. ConfigManager.kt
Quản lý cấu hình:
- Load và merge config overrides
- Build signaling URL
- Get seeder URL cho fetch
- Runtime config updates

### 4. EventEmitter.kt
Base class cho event handling:
- Sử dụng Coroutines
- Thread-safe event dispatching
- Support multiple listeners per event

### 5. SignalingClient.kt
WebSocket client kết nối tới signaling server:
- Connect/disconnect với auto-reconnect
- WhoHas query (tìm peer có segment)
- Report segment fetch (báo cáo đã tải)
- Report segment removal (báo segment bị evict)
- WebRTC signaling (offer/answer/ICE)
- Response caching và debouncing

### 6. CacheManager.kt
LRU cache với hot cache protection:
- Lưu segments, init segments, playlists
- LRU eviction cho media segments
- Hot cache cho init/playlist (không bị evict)
- Time-based TTL
- Segment time mapping cho seek optimization
- Callback khi segment bị evict (notify signaling)

### 7. SegmentFetcher.kt
HTTP fallback fetcher:
- Fetch master/variant playlists
- Fetch init segments
- Fetch media segments từ seeder
- Parse M3U8 playlists
- Retry logic với exponential backoff

### 8. PeerManager.kt (TODO)
Quản lý WebRTC P2P connections:
- Lazy connection (chỉ connect khi cần)
- Peer scoring và selection
- DataChannel communication
- Segment request/response qua P2P
- Connection cleanup

### 9. BufferManager.kt (TODO)
Quản lý buffering và prefetching:
- Monitor buffer health
- Sequential segment append (tránh gaps)
- Critical fetch khi buffer low
- Prefetch segments ahead
- Seek handling với prefetch
- Quality switch coordination

### 10. AbrManager.kt (TODO)
Adaptive bitrate logic:
- Load all variant playlists
- Bandwidth estimation
- Quality selection based on buffer/bandwidth
- Init segment management cho từng quality
- Smooth quality switching

### 11. IntegratedSegmentFetchClient.kt (TODO)
Hybrid fetch strategy (P2P + HTTP):
- Try cache first
- Critical mode: skip P2P, fetch HTTP ngay
- Normal mode: query WhoHas → try P2P → fallback HTTP
- Report segment sau khi fetch thành công
- Segment append queue coordination

### 12. StreamingPlayerCoordinator.kt (TODO)
Main coordinator:
- Initialize tất cả modules
- Event routing
- Play/pause/seek controls
- Quality switching (manual/auto ABR)
- Metrics collection
- Lifecycle management

### 13. UI với Jetpack Compose
`ui/StreamingPlayerDemo.kt`:
- Video player với ExoPlayer
- Play/pause controls
- Quality selector
- Seek bar
- Stats display
- Error handling

## Luồng fetch segment

### Normal fetch (buffer prefetch):
```
BufferManager.prefetchSegments()
  → IntegratedFetchClient.fetchSegment()
    → Check cache
      → If cached: return
    → Query SignalingClient.whoHas()
      → Get list of peers có segment
    → PeerManager.fetchFromPeers()
      → Try fetch từ P2P
        → Success: cache + report to signaling → return
    → Fallback: SegmentFetcher.fetchFromSeeder()
      → HTTP fetch
      → cache + report to signaling
```

### Critical fetch (buffer critically low):
```
BufferManager detects buffer < threshold/3
  → IntegratedFetchClient.fetchSegment(critical=true)
    → Skip P2P query
    → SegmentFetcher.fetchFromSeeder() (ngay lập tức)
      → HTTP fetch
      → cache + report to signaling
```

### Seek fetch:
```
StreamingPlayerCoordinator.seek(time)
  → CacheManager.mapTimeToSegmentId(time)
    → Get target segment
  → CacheManager.getSegmentsAroundTime(time, before, after)
    → Get range of segments
  → BufferManager handles prefetch
    → Similar to normal fetch
```

## Dependencies cần thiết

```gradle
// Kotlin
implementation "org.jetbrains.kotlin:kotlin-stdlib:1.9.0"
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

// Jetpack Compose
implementation "androidx.compose.ui:ui:1.5.4"
implementation "androidx.compose.material3:material3:1.1.2"
implementation "androidx.compose.ui:ui-tooling-preview:1.5.4"
implementation "androidx.activity:activity-compose:1.8.0"

// ExoPlayer
implementation "com.google.android.exoplayer:exoplayer:2.19.1"

// WebRTC
implementation "org.webrtc:google-webrtc:1.0.32006"

// Networking
implementation "com.squareup.okhttp3:okhttp:4.11.0"
implementation "com.google.code.gson:gson:2.10.1"

// Coroutines
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3"
```

## Permissions cần thiết

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Sử dụng trong code

### Cách 1: Sử dụng với Compose (Recommended)

```kotlin
@Composable
fun MyStreamingApp() {
    val clientId = remember { UUID.randomUUID().toString() }
    
    StreamingPlayerDemo(
        clientId = clientId,
        movieId = "your-movie-id-here"
    )
}
```

### Cách 2: Sử dụng trực tiếp StreamingPlayerCoordinator

```kotlin
class VideoPlayerActivity : ComponentActivity() {
    private lateinit var player: StreamingPlayerCoordinator
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize player
        player = StreamingPlayerCoordinator(
            context = this,
            options = StreamingPlayerCoordinator.StreamingPlayerOptions(
                movieId = "7e20b92f-9a57-4c98-afce-763f2edbf7b7",
                clientId = UUID.randomUUID().toString(),
                configOverrides = StreamingConfig(
                    baseUrl = "http://your-server.com:8080",
                    maxActivePeers = 6,
                    bufferTargetDuration = 30f
                )
            )
        )
        
        // Setup event listeners
        player.on("ready") {
            println("Player ready!")
        }
        
        player.on("error") { data ->
            val error = data as? Exception
            println("Player error: ${error?.message}")
        }
        
        // Initialize
        lifecycleScope.launch {
            player.initialize()
            player.play()
        }
        
        setContent {
            MaterialTheme {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player = player.mseManager.exoPlayer
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
    
    override fun onDestroy() {
        player.dispose()
        super.onDestroy()
    }
}
```

### Cách 3: Custom Configuration

```kotlin
val customConfig = StreamingConfig(
    // Server settings
    baseUrl = "http://192.168.1.100:8080",
    
    // P2P settings
    maxActivePeers = 8,
    minActivePeers = 3,
    peerConnectionTimeout = 5000L,
    
    // Buffer settings
    bufferTargetDuration = 40f,
    bufferMinThreshold = 10f,
    prefetchWindowAhead = 60f,
    
    // ABR settings
    abrEnabled = true,
    abrSwitchUpThreshold = 0.85f,
    abrSwitchDownThreshold = 0.25f,
    
    // Cache settings
    cacheSizeLimit = 1024L * 1024 * 1024, // 1GB
    
    // Fetch settings
    maxConcurrentFetches = 6,
    fetchTimeout = 15000L
)

val player = StreamingPlayerCoordinator(
    context = context,
    options = StreamingPlayerCoordinator.StreamingPlayerOptions(
        movieId = movieId,
        clientId = clientId,
        configOverrides = customConfig
    )
)
```

## Các điểm khác biệt so với TypeScript version

1. **Media Source Extensions (MSE) → ExoPlayer**
   - TypeScript dùng MSE API của browser
   - Android dùng ExoPlayer để handle media playback
   - ExoPlayer tự động handle buffering, codec, etc.

2. **WebSocket**
   - TypeScript: native WebSocket API
   - Android: OkHttp WebSocket

3. **WebRTC**
   - TypeScript: browser WebRTC API
   - Android: google-webrtc library

4. **Event System**
   - TypeScript: EventEmitter pattern
   - Android: Custom EventEmitter với Coroutines

5. **Async/Promise → Coroutines**
   - TypeScript: async/await, Promise
   - Android: Kotlin Coroutines, suspend functions

6. **UI Framework**
   - TypeScript: React với hooks
   - Android: Jetpack Compose với @Composable

## TODO (các phần đã hoàn thiện)

Tất cả các file đã được implement đầy đủ:
- [x] PeerManager.kt (WebRTC P2P logic)
- [x] BufferManager.kt (buffering và prefetch logic)
- [x] AbrManager.kt (ABR decision logic)
- [x] IntegratedSegmentFetchClient.kt (hybrid fetch)
- [x] StreamingPlayerCoordinator.kt (main coordinator)
- [x] MseManager.kt (ExoPlayer wrapper)
- [x] MainActivity.kt (entry point)
- [x] build.gradle (dependencies)
- [x] AndroidManifest.xml (permissions)

## Cách sử dụng

### 1. Setup Project

Tạo Android project mới trong Android Studio:
```
File -> New -> New Project -> Empty Compose Activity
```

### 2. Copy Files

Copy tất cả các file `.kt` vào package `com.example.streaming`:
```
app/src/main/java/com/example/streaming/
├── Types.kt
├── AppConstants.kt
├── ConfigManager.kt
├── EventEmitter.kt
├── SignalingClient.kt
├── PeerManager.kt
├── CacheManager.kt
├── SegmentFetcher.kt
├── MseManager.kt
├── BufferManager.kt
├── AbrManager.kt
├── IntegratedSegmentFetchClient.kt
├── StreamingPlayerCoordinator.kt
├── MainActivity.kt
└── ui/
    └── StreamingPlayerDemo.kt
```

### 3. Update Dependencies

Copy nội dung `build.gradle` vào `app/build.gradle`

### 4. Update Manifest

Copy nội dung `AndroidManifest.xml` vào `app/src/main/AndroidManifest.xml`

### 5. Sync và Run

```
File -> Sync Project with Gradle Files
Run -> Run 'app'
```

## Cấu trúc Project hoàn chỉnh

```
android-kotlin/
├── Types.kt                              # ✅ Data classes, enums
├── AppConstants.kt                       # ✅ Constants
├── ConfigManager.kt                      # ✅ Configuration management
├── EventEmitter.kt                       # ✅ Event system
├── SignalingClient.kt                    # ✅ WebSocket signaling (hoàn chỉnh)
├── PeerManager.kt                        # ✅ WebRTC P2P (hoàn chỉnh)
├── CacheManager.kt                       # ✅ LRU cache (hoàn chỉnh)
├── SegmentFetcher.kt                     # ✅ HTTP fetcher (hoàn chỉnh)
├── MseManager.kt                         # ✅ ExoPlayer wrapper (hoàn chỉnh)
├── BufferManager.kt                      # ✅ Buffer management (hoàn chỉnh)
├── AbrManager.kt                         # ✅ ABR logic (hoàn chỉnh)
├── IntegratedSegmentFetchClient.kt      # ✅ Hybrid fetch (hoàn chỉnh)
├── StreamingPlayerCoordinator.kt        # ✅ Main coordinator (hoàn chỉnh)
├── MainActivity.kt                       # ✅ Entry point
├── ui/
│   └── StreamingPlayerDemo.kt           # ✅ Compose UI (hoàn chỉnh)
├── build.gradle                          # ✅ Dependencies
├── AndroidManifest.xml                   # ✅ Permissions
└── README.md                             # ✅ Documentation
```

## Ghi chú kỹ thuật

### Cache Strategy
- **Hot Cache**: Init segments + Playlists (không bị evict)
- **LRU Cache**: Media segments (có thể bị evict)
- **TTL**: Segments 30min, Playlists 1h, Init 24h

### Buffer Strategy
- **Target**: 30s buffer
- **Min**: 8s (bắt đầu buffer nếu dưới)
- **Critical**: 8s/3 ≈ 2.7s (fetch HTTP ngay, skip P2P)
- **Max**: 60s (dừng prefetch)

### ABR Strategy
- **Switch Up**: buffer > 80% target && bandwidth > 120% current
- **Switch Down**: buffer < 30% target
- **Bandwidth estimation**: sliding window 5 segments

### P2P Strategy
- **Max peers**: 6 active connections
- **Peer selection**: score-based (latency, reliability, availability)
- **Lazy connection**: chỉ connect khi cần fetch
- **Timeout**: 5s cho peer fetch, fallback HTTP

## License

MIT License (giống như TypeScript version)
