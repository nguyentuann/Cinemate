package com.movpla.detail.streaming.data.model

data class HlsSource(
    val url: String,
    val type: String = "application/x-mpegURL",
    val quality: String? = null
)

data class VideoSource(
    val url: String,
    val type: String,
    val quality: String? = null
)

data class StreamingData(
    val sources: List<VideoSource>,
    val subtitles: List<SubtitleTrack> = emptyList(),
    val thumbnails: String? = null,
    val skipIntro: SkipTime? = null,
    val skipOutro: SkipTime? = null
)

data class SubtitleTrack(
    val url: String,
    val lang: String,
    val label: String,
    val isDefault: Boolean = false
)

data class SkipTime(
    val start: Float,
    val end: Float
)

sealed class StreamingQuality {
    data class Auto(val current: String) : StreamingQuality()
    data class Manual(val quality: String) : StreamingQuality()
}

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentTime: Float = 0f,
    val duration: Float = 0f,
    val bufferedPercentage: Int = 0,
    val currentQuality: StreamingQuality = StreamingQuality.Auto("Auto"),
    val currentSubtitle: String? = null,
    val volume: Float = 1f,
    val playbackSpeed: Float = 1f
)
