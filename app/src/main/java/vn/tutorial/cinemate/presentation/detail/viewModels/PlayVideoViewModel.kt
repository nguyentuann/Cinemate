package vn.tutorial.cinemate.presentation.detail.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.util.LogUtil
import javax.inject.Inject

data class VideoPlayerState(
    var isPlaying: Boolean = false,
    var speed: Float = 1f,
    var position: Long = 0L,
    var duration: Long = 0L,
    var locked: Boolean = false,
    var controlsVisible: Boolean = true,
    var isSilent: Boolean = false,
    var quality: String = "Auto"
)

@UnstableApi
@HiltViewModel
class PlayVideoViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val trackSelector = DefaultTrackSelector(context).apply {
        setParameters(buildUponParameters().setMaxVideoSizeSd())
    }

    val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector).setSeekForwardIncrementMs(10000)
            .setSeekBackIncrementMs(10000)
            .build().apply {
                playWhenReady = false
            }
    }

    private var currentUri: String? = null

    private val _state = MutableStateFlow(VideoPlayerState())
    val state: StateFlow<VideoPlayerState> = _state

    fun selectQuality(height: Int?, label: String) {
        val parameters = if (height == null) {
            // Auto
            trackSelector.buildUponParameters()
                .clearVideoSizeConstraints()
                .setForceHighestSupportedBitrate(false)
        } else {
            trackSelector.buildUponParameters()
                .setMaxVideoSize(Int.MAX_VALUE, height)
                .setMinVideoSize(0, height)
        }

        trackSelector.setParameters(parameters)
        _state.value = _state.value.copy(quality = label)
    }

    fun togglePlay() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            _state.value = _state.value.copy(isPlaying = false)
        } else {
            exoPlayer.play()
            _state.value = _state.value.copy(isPlaying = true)
        }
    }

    fun seekForward() = exoPlayer.seekForward()
    fun seekBack() = exoPlayer.seekBack()
    fun seekTo(positionMs: Long) = exoPlayer.seekTo(positionMs)

    fun changeSpeed() {
        val newSpeed = when (_state.value.speed) {
            1f -> 1.5f
            1.5f -> 2f
            else -> 1f
        }
        exoPlayer.setPlaybackSpeed(newSpeed)
        _state.value = _state.value.copy(speed = newSpeed)
    }

    fun toggleLock() {
        _state.value = _state.value.copy(locked = !_state.value.locked)
    }

    fun toggleControls() {
        _state.value = _state.value.copy(controlsVisible = !_state.value.controlsVisible)
    }

    fun updateProgress() {
        _state.value = _state.value.copy(
            position = exoPlayer.currentPosition,
            duration = exoPlayer.duration.coerceAtLeast(0L)
        )
    }

    fun toggleSilent() {
        val isSilent = !_state.value.isSilent
        exoPlayer.volume = if (isSilent) 0f else 1f
        _state.value = _state.value.copy(isSilent = isSilent)
    }

    fun setMedia(uri: String) {
        if (uri != currentUri) {
            currentUri = uri
            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
    }

    override fun onCleared() {
        super.onCleared()
        LogUtil("call clear")
        exoPlayer.release()
    }
}