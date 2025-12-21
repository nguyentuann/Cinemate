package com.movpla.detail.streaming.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.movpla.detail.streaming.data.model.PlaybackState
import com.movpla.detail.streaming.data.model.StreamingData
import com.movpla.detail.streaming.data.model.SubtitleTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@UnstableApi
class HlsPlayerManager(private val context: Context) {
    
    private var exoPlayer: ExoPlayer? = null
    private val trackSelector = DefaultTrackSelector(context)
    
    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()
    
    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playbackState.value = _playbackState.value.copy(isPlaying = isPlaying)
        }
        
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    // Handle buffering
                }
                Player.STATE_READY -> {
                    exoPlayer?.let { player ->
                        _playbackState.value = _playbackState.value.copy(
                            duration = player.duration.toFloat() / 1000f
                        )
                    }
                }
                Player.STATE_ENDED -> {
                    // Handle ended
                }
            }
        }
    }
    
    fun initializePlayer(streamingData: StreamingData): ExoPlayer {
        releasePlayer()
        
        val player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
        
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val hlsSource = streamingData.sources.firstOrNull()
        
        hlsSource?.let { source ->
            val mediaItem = MediaItem.Builder()
                .setUri(source.url)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build()
            
            val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
            
            // Add subtitles if available
            val mediaSources = mutableListOf(hlsMediaSource)
            streamingData.subtitles.forEach { subtitle ->
                mediaSources.add(createSubtitleSource(subtitle, dataSourceFactory))
            }
            
            val mergedSource = if (mediaSources.size > 1) {
                MergingMediaSource(*mediaSources.toTypedArray())
            } else {
                hlsMediaSource
            }
            
            player.setMediaSource(mergedSource)
            player.prepare()
        }
        
        player.addListener(playerListener)
        exoPlayer = player
        
        return player
    }
    
    private fun createSubtitleSource(
        subtitle: SubtitleTrack,
        dataSourceFactory: DefaultHttpDataSource.Factory
    ) = androidx.media3.exoplayer.source.SingleSampleMediaSource.Factory(dataSourceFactory)
        .createMediaSource(
            MediaItem.SubtitleConfiguration.Builder(android.net.Uri.parse(subtitle.url))
                .setMimeType(MimeTypes.TEXT_VTT)
                .setLanguage(subtitle.lang)
                .setLabel(subtitle.label)
                .setSelectionFlags(
                    if (subtitle.isDefault) androidx.media3.common.C.SELECTION_FLAG_DEFAULT 
                    else 0
                )
                .build(),
            androidx.media3.common.C.TIME_UNSET
        )
    
    fun play() {
        exoPlayer?.play()
    }
    
    fun pause() {
        exoPlayer?.pause()
    }
    
    fun seekTo(position: Float) {
        exoPlayer?.seekTo((position * 1000).toLong())
    }
    
    fun setPlaybackSpeed(speed: Float) {
        exoPlayer?.setPlaybackSpeed(speed)
        _playbackState.value = _playbackState.value.copy(playbackSpeed = speed)
    }
    
    fun setVolume(volume: Float) {
        exoPlayer?.volume = volume
        _playbackState.value = _playbackState.value.copy(volume = volume)
    }
    
    fun getCurrentPosition(): Long {
        return exoPlayer?.currentPosition ?: 0L
    }
    
    fun releasePlayer() {
        exoPlayer?.removeListener(playerListener)
        exoPlayer?.release()
        exoPlayer = null
    }
}
