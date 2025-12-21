package vn.tutorial.cinemate.presentation.streaming.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.usecase.movies.GetProgressUseCase
import vn.tutorial.cinemate.domain.usecase.movies.ReportProgressUseCase
import vn.tutorial.cinemate.presentation.streaming.StreamingPlayerCoordinator
import java.util.UUID
import javax.inject.Inject

data class StreamingUiState(
    val isPlayerReady: Boolean = false,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentQuality: String = "auto",
    val availableQualities: List<String> = emptyList(),
    val currentTime: Float = 0f,
    val duration: Float = 0f,
    val error: String? = null,
    val bufferHealth: Int = 0,
    val p2pRatio: Int = 0,
    val activeConnections: Int = 0,
    val cacheHitRate: String = "0%",
    val p2pFetches: Int = 0,
    val httpFetches: Int = 0,
    // VideoPlayer2 specific states
    val controlsVisible: Boolean = true,
    val locked: Boolean = false,
    val speed: Float = 1.0f,
    val isSilent: Boolean = false,
    // Progress restoration
    val initialProgress: Int? = null,
    val hasRestoredProgress: Boolean = false
)

@HiltViewModel
class StreamingViewModel @Inject constructor(
    private val reportProgressUseCase: ReportProgressUseCase,
    private val getProgressUseCase: GetProgressUseCase
) : ViewModel() {

    private val TAG = "StreamingViewModel"
    private val clientId = UUID.randomUUID().toString()

    private val _uiState = MutableStateFlow(StreamingUiState())
    val uiState: StateFlow<StreamingUiState> = _uiState.asStateFlow()

    private var player: StreamingPlayerCoordinator? = null

    fun initializePlayer(context: Context, movieId: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Initializing player for movieId: $movieId")

                player = StreamingPlayerCoordinator(
                    context = context,
                    options = StreamingPlayerCoordinator.StreamingPlayerOptions(
                        movieId = movieId,
                        clientId = clientId
                    )
                )

                player?.initialize()

                _uiState.update {
                    it.copy(
                        isPlayerReady = true,
                        availableQualities = player?.getAvailableQualities()?.map { q -> q.id }
                            ?: emptyList(),
                        currentQuality = player?.getCurrentQuality()?.id ?: "auto"
                    )
                }

                Log.d(TAG, "Player initialized successfully")

                // Start periodic stats update
                startStatsUpdate()

            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize player", e)
                _uiState.update {
                    it.copy(
                        error = "Failed to initialize player: ${e.message}"
                    )
                }
            }
        }
    }

    private fun startStatsUpdate() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                updatePlayerStats()
            }
        }
    }

    private suspend fun updatePlayerStats() {
        player?.let { p ->
            val state = p.getPlayerState()
            val metrics = p.getPlaybackMetrics()
            val cacheStats = p.getCacheStats()
            val fetchStats = p.getFetchStats()

            _uiState.update {
                it.copy(
                    currentTime = state.currentTime,
                    duration = state.duration,
                    isPlaying = state.isPlaying,
                    isBuffering = state.isBuffering,
                    bufferHealth = (metrics.bufferHealth * 100).toInt(),
                    p2pRatio = (metrics.p2pRatio * 100).toInt(),
                    activeConnections = metrics.activeConnections,
                    cacheHitRate = cacheStats["hitRate"] as? String ?: "0%",
                    p2pFetches = fetchStats.p2pFetches,
                    httpFetches = fetchStats.httpFetches
                )
            }
        }
    }

    fun play() {
        viewModelScope.launch {
            try {
                player?.play()
                Log.d(TAG, "Play requested")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to play", e)
                _uiState.update { it.copy(error = "Failed to play: ${e.message}") }
            }
        }
    }

    fun pause() {
        try {
            player?.pause()
            Log.d(TAG, "Pause requested")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to pause", e)
            _uiState.update { it.copy(error = "Failed to pause: ${e.message}") }
        }
    }

    fun seek(time: Float) {
        viewModelScope.launch {
            try {
                player?.seek(time)
                Log.d(TAG, "Seek to $time requested")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to seek", e)
                _uiState.update { it.copy(error = "Failed to seek: ${e.message}") }
            }
        }
    }

    fun enableAutoQuality() {
        try {
            player?.enableAutoQuality()
            _uiState.update { it.copy(currentQuality = "auto") }
            Log.d(TAG, "Auto quality enabled")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to enable auto quality", e)
        }
    }

    fun setManualQuality(qualityId: String) {
        viewModelScope.launch {
            try {
                player?.setManualQuality(qualityId)
                _uiState.update { it.copy(currentQuality = qualityId) }
                Log.d(TAG, "Manual quality set to $qualityId")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to set quality", e)
                _uiState.update { it.copy(error = "Failed to set quality: ${e.message}") }
            }
        }
    }

    fun getExoPlayer() = player?.mseManager?.exoPlayer

    fun getCurrentPosition(): Long {
        return (player?.mseManager?.exoPlayer?.currentPosition ?: 0L)
    }

    fun getDuration(): Long {
        return (player?.mseManager?.exoPlayer?.duration ?: 0L)
    }

    fun seekTo(positionMs: Long) {
        viewModelScope.launch {
            try {
                player?.seek(positionMs / 1000f)
                Log.d(TAG, "Seek to $positionMs ms")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to seek", e)
            }
        }
    }

    fun seekBack() {
        val currentPos = getCurrentPosition()
        val newPos = (currentPos - 10000).coerceAtLeast(0)
        seekTo(newPos)
    }

    fun seekForward() {
        val currentPos = getCurrentPosition()
        val duration = getDuration()
        val newPos = (currentPos + 10000).coerceAtMost(duration)
        seekTo(newPos)
    }

    fun switchQuality(qualityId: String) {
        setManualQuality(qualityId)
    }

    fun toggleControls() {
        _uiState.update { it.copy(controlsVisible = !it.controlsVisible) }
    }

    fun toggleLock() {
        _uiState.update { it.copy(locked = !it.locked) }
    }

    fun toggleSilent() {
        val newSilent = !_uiState.value.isSilent
        _uiState.update { it.copy(isSilent = newSilent) }
        player?.setMuted(newSilent)
    }

    fun changeSpeed() {
        val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
        val currentSpeed = _uiState.value.speed
        val currentIndex = speeds.indexOf(currentSpeed)
        val nextSpeed = speeds[(currentIndex + 1) % speeds.size]

        _uiState.update { it.copy(speed = nextSpeed) }
        player?.mseManager?.exoPlayer?.setPlaybackSpeed(nextSpeed)
        Log.d(TAG, "Playback speed changed to ${nextSpeed}x")
    }

    fun getProgress(movieId: String) {
        executeUseCase(
            state = _uiState,
            block = {
                getProgressUseCase.invoke(movieId)
            },
            onSuccess = { progress ->
                _uiState.value.copy(
                    initialProgress = progress
                )
            },
            onError = {
                _uiState.value
            },
            onLoading = {
                _uiState.value
            }
        )
    }

    fun restoreProgress() {
        val progressInSeconds = _uiState.value.initialProgress
        if (progressInSeconds != null && progressInSeconds > 0 && !_uiState.value.hasRestoredProgress) {
            val progressInMs = progressInSeconds * 1000L // Convert giây sang milliseconds
            seekTo(progressInMs)
            _uiState.update { it.copy(hasRestoredProgress = true) }
        }
    }
    fun reportProgress(movieId: String) {
        val currentPos = getCurrentPosition()
        val totalDur = getDuration()
        
        // Validate before sending
        if (totalDur <= 0) {
            return
        }
        
        if (currentPos < 0) {
            return
        }
        
        val lastWatchedPosition = (currentPos / 1000).toInt()
        val totalDuration = (totalDur / 1000).toInt()
        
        // Ensure lastWatchedPosition doesn't exceed totalDuration
        val safeLastWatchedPosition = lastWatchedPosition.coerceAtMost(totalDuration)
        
        executeUseCase(
            state = _uiState,
            block = {
                reportProgressUseCase(
                    ReportProgressUseCase.Param(
                        movieId = movieId,
                        lastWatchedPosition = safeLastWatchedPosition,
                        totalDuration = totalDuration
                    )
                )
            },
            onSuccess = {
                _uiState.value
            },
            onError = { error ->
                _uiState.value
            },
            onLoading = {
                _uiState.value
            }
        )
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel cleared, disposing player")
        player?.dispose()
        player = null
    }
}