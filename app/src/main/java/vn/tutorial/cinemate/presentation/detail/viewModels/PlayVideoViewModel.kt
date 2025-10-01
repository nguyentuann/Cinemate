package vn.tutorial.cinemate.presentation.detail.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayVideoViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = false
        }
    }

    private var currentUri: String? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _speed = MutableStateFlow(1f)
    val speed: StateFlow<Float> = _speed

    private val _position = MutableStateFlow(0L)
    val position: StateFlow<Long> = _position

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private val _isFullscreen = MutableStateFlow(false)
    val isFullscreen: StateFlow<Boolean> = _isFullscreen

    private val _locked = MutableStateFlow(false)
    val locked: StateFlow<Boolean> = _locked

    private val _controlsVisible = MutableStateFlow(true)
    val controlsVisible: StateFlow<Boolean> = _controlsVisible

    fun togglePlay() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            _isPlaying.value = false
        } else {
            exoPlayer.play()
            _isPlaying.value = true
        }
    }

    fun seekForward() = exoPlayer.seekForward()
    fun seekBack() = exoPlayer.seekBack()
    fun seekTo(positionMs: Long) = exoPlayer.seekTo(positionMs)

    fun changeSpeed() {
        _speed.value = when (_speed.value) {
            1f -> 1.5f
            1.5f -> 2f
            else -> 1f
        }
        exoPlayer.setPlaybackSpeed(_speed.value)
    }

    fun toggleFullscreen() {
        _isFullscreen.value = !_isFullscreen.value
    }

    fun toggleLock() {
        _locked.value = !_locked.value
    }

    fun toggleControls() {
        _controlsVisible.value = !_controlsVisible.value
    }

    fun updateProgress() {
        _position.value = exoPlayer.currentPosition
        _duration.value = exoPlayer.duration.coerceAtLeast(0L)
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
        exoPlayer.release()
    }
}