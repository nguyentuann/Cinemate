package vn.tutorial.cinemate.presentation.streaming

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.datasource.DefaultHttpDataSource
import kotlinx.coroutines.*

/**
 * Simplified MseManager that uses HLS playback directly
 * This is a much simpler approach for ExoPlayer
 */
class SimpleMseManager(
    private val context: Context,
    private val movieId: String,
    private val cacheManager: CacheManager,
    private val signalingClient: SignalingClient
) : EventEmitter<MseManagerEvents>(), MsePlayer {
    
    private val TAG = "Logging SimpleMseManager"
    override var exoPlayer: ExoPlayer? = null
        private set
    
    private var totalDuration = 0f
    private var isInitialized = false
    private var playbackState: PlaybackState = PlaybackState.PAUSED
    
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    enum class PlaybackState {
        PLAYING, PAUSED, BUFFERING, ENDED
    }

    suspend fun initialize() {
        withContext(Dispatchers.Main) {
            Log.d(TAG, "Initializing ExoPlayer...")
            exoPlayer = ExoPlayer.Builder(context).build()
            
            exoPlayer?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> {
                            if (exoPlayer?.playWhenReady == true) {
                                updatePlaybackState(PlaybackState.PLAYING)
                            }
                            val duration = exoPlayer?.duration ?: 0
                            val hasVideo = exoPlayer?.videoFormat != null
                            val hasAudio = exoPlayer?.audioFormat != null
                            Log.d(TAG, "Player READY - duration: ${duration}ms, hasVideo: $hasVideo, hasAudio: $hasAudio")
                            Log.d(TAG, "Video format: ${exoPlayer?.videoFormat}")
                            Log.d(TAG, "Audio format: ${exoPlayer?.audioFormat}")
                            emit("sourceOpen")
                        }
                        Player.STATE_BUFFERING -> {
                            updatePlaybackState(PlaybackState.BUFFERING)
                            Log.d(TAG, "Player BUFFERING - buffered: ${exoPlayer?.bufferedPosition}ms")
                        }
                        Player.STATE_ENDED -> {
                            updatePlaybackState(PlaybackState.ENDED)
                            Log.d(TAG, "Player ENDED")
                        }
                        Player.STATE_IDLE -> {
                            Log.d(TAG, "Player IDLE")
                        }
                    }
                }
                
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    Log.d(TAG, "onIsPlayingChanged: $isPlaying")
                    if (isPlaying) {
                        updatePlaybackState(PlaybackState.PLAYING)
                    } else if (exoPlayer?.playbackState != Player.STATE_ENDED) {
                        updatePlaybackState(PlaybackState.PAUSED)
                    }
                }
                
                override fun onPlayerError(error: PlaybackException) {
                    Log.e(TAG, "━━━ PLAYER ERROR ━━━")
                    Log.e(TAG, "Error: ${error.message}")
                    Log.e(TAG, "Error code: ${error.errorCode}")
                    Log.e(TAG, "Error type: ${error.errorCodeName}")
                    
                    // Check if it's audio discontinuity (can be ignored or handled)
                    if (error.message?.contains("UnexpectedDiscontinuityException") == true ||
                        error.message?.contains("audio track timestamp discontinuity") == true) {
                        Log.w(TAG, "Audio timestamp discontinuity detected - ExoPlayer will attempt recovery")
                        // Don't emit error for this, let ExoPlayer handle it
                        return
                    }
                    
                    Log.e(TAG, "━━━━━━━━━━━━━━━━━━━")
                    emit("error", Exception(error.message))
                }
                
                override fun onTracksChanged(tracks: androidx.media3.common.Tracks) {
                    val videoTracks = tracks.groups.count { it.type == androidx.media3.common.C.TRACK_TYPE_VIDEO }
                    val audioTracks = tracks.groups.count { it.type == androidx.media3.common.C.TRACK_TYPE_AUDIO }
                    Log.d(TAG, "Tracks changed - Video: $videoTracks, Audio: $audioTracks")
                    
                    // Log video track details
                    tracks.groups.forEach { group ->
                        if (group.type == androidx.media3.common.C.TRACK_TYPE_VIDEO) {
                            for (i in 0 until group.length) {
                                val format = group.getTrackFormat(i)
                                Log.d(TAG, "Video track $i: ${format.width}x${format.height}, codec: ${format.codecs}, bitrate: ${format.bitrate}")
                            }
                        }
                    }
                }
                
                override fun onVideoSizeChanged(videoSize: androidx.media3.common.VideoSize) {
                    Log.d(TAG, "━━━ VIDEO SIZE CHANGED ━━━")
                    Log.d(TAG, "Video size: ${videoSize.width}x${videoSize.height}")
                    Log.d(TAG, "Pixel aspect ratio: ${videoSize.pixelWidthHeightRatio}")
                    Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━")
                }
                
                override fun onRenderedFirstFrame() {
                    Log.d(TAG, "🎬🎬🎬 FIRST VIDEO FRAME RENDERED! 🎬🎬🎬")
                }
            })
            
            isInitialized = true
            Log.d(TAG, "SimpleMseManager initialized with ExoPlayer")
        }
    }
    
    /**
     * Load HLS manifest directly
     */
    @OptIn(UnstableApi::class)
    suspend fun loadHlsManifest(manifestUrl: String) {
        withContext(Dispatchers.Main) {
            if (!isInitialized || exoPlayer == null) {
                val error = Exception("MSE not initialized")
                Log.e(TAG, "Cannot load HLS manifest: MSE not initialized")
                throw error
            }
            
            try {
                Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                Log.d(TAG, "Loading HLS manifest from: $manifestUrl")
                
                // Use custom caching data source factory
                val dataSourceFactory = CachingDataSourceFactory(
                    movieId,
                    cacheManager,
                    signalingClient
                )
                
                val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
                    .setAllowChunklessPreparation(true)
                    .createMediaSource(MediaItem.fromUri(manifestUrl))
                
                Log.d(TAG, "Setting media source...")
                exoPlayer?.setMediaSource(hlsMediaSource)
                
                Log.d(TAG, "Preparing player...")
                exoPlayer?.prepare()
                
                Log.d(TAG, "HLS manifest loaded - Player state: ${exoPlayer?.playbackState}")
                Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            } catch (e: Exception) {
                Log.e(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                Log.e(TAG, "Failed to load HLS manifest from $manifestUrl", e)
                Log.e(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                throw e
            }
        }
    }
    
    /**
     * Load a simple MP4 URL
     */
    suspend fun loadUrl(url: String) {
        withContext(Dispatchers.Main) {
            if (!isInitialized || exoPlayer == null) {
                throw Exception("MSE not initialized")
            }
            
            Log.d(TAG, "Loading URL: $url")
            
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
            
            Log.d(TAG, "Media loaded and prepared")
        }
    }
    
    override fun setDuration(duration: Float) {
        totalDuration = duration
        emit("durationChanged", duration)
        Log.d(TAG, "Duration set to ${duration}s")
    }
    
    override suspend fun getCurrentTime(): Float = withContext(Dispatchers.Main) {
        exoPlayer?.currentPosition?.toFloat()?.div(1000f) ?: 0f
    }

    override suspend fun getDuration(): Float = withContext(Dispatchers.Main) {
        val duration = exoPlayer?.duration ?: 0L
        if (duration > 0) {
            duration.toFloat() / 1000f
        } else {
            totalDuration
        }
    }

    override suspend fun getBufferedRanges(): List<BufferRange> = withContext(Dispatchers.Main) {
        val player = exoPlayer ?: return@withContext emptyList()
        val ranges = mutableListOf<BufferRange>()

        val bufferedPosition = player.bufferedPosition
        val currentPosition = player.currentPosition
        
        if (bufferedPosition > currentPosition) {
            ranges.add(BufferRange(
                start = currentPosition.toFloat() / 1000f,
                end = bufferedPosition.toFloat() / 1000f
            ))
        }

        ranges
    }
    
    override suspend fun seekTo(time: Float) {
        withContext(Dispatchers.Main) {
            emit("seekStart", time)
            exoPlayer?.seekTo((time * 1000).toLong())
            
            delay(100)
            emit("seekEnd", time)
            Log.d(TAG, "Seeked to ${time}s")
        }
    }
    
    override suspend fun play() {
        withContext(Dispatchers.Main) {
            Log.d(TAG, "▶▶▶ play() called")
            Log.d(TAG, "Player state before play: ${exoPlayer?.playbackState}")
            Log.d(TAG, "Has video: ${exoPlayer?.videoFormat != null}")
            Log.d(TAG, "Has audio: ${exoPlayer?.audioFormat != null}")
            
            exoPlayer?.playWhenReady = true
            exoPlayer?.play()
            updatePlaybackState(PlaybackState.PLAYING)
            
            Log.d(TAG, "Playback started - isPlaying: ${exoPlayer?.isPlaying}")
        }
    }
    
    override fun pause() {
        exoPlayer?.pause()
        exoPlayer?.playWhenReady = false
        updatePlaybackState(PlaybackState.PAUSED)
        Log.d(TAG, "Playback paused")
    }
    
    override fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying == true
    }
    
    private fun updatePlaybackState(state: PlaybackState) {
        if (playbackState != state) {
            playbackState = state
            val stateString = when (state) {
                PlaybackState.PLAYING -> "playing"
                PlaybackState.PAUSED -> "paused"
                PlaybackState.BUFFERING -> "buffering"
                PlaybackState.ENDED -> "ended"
            }
            emit("playbackStateChanged", stateString)
            Log.d(TAG, "Playback state: $stateString")
        }
    }
    
    override fun clear() {
        scope.cancel()
        exoPlayer?.release()
        exoPlayer = null
        isInitialized = false
        Log.d(TAG, "SimpleMseManager destroyed")
    }
}
