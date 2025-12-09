package vn.tutorial.cinemate.presentation.streaming

import androidx.media3.exoplayer.ExoPlayer

/**
 * Interface for MSE-like player implementations
 */
interface MsePlayer {
    val exoPlayer: ExoPlayer?
    
    suspend fun getCurrentTime(): Float
    suspend fun getDuration(): Float
    suspend fun getBufferedRanges(): List<BufferRange>
    suspend fun seekTo(time: Float)
    suspend fun play()
    fun pause()
    fun isPlaying(): Boolean
    fun setDuration(duration: Float)
    fun clear()
}
