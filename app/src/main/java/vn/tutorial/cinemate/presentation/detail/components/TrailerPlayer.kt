package vn.tutorial.cinemate.presentation.detail.components

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.media3.common.util.UnstableApi

@OptIn(UnstableApi::class)
@Composable
fun TrailerPlayer(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(
                "http://10.0.2.2:9000/movies/$url"
            )
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            volume = 0f
        }
    }

    // State để lưu tỷ lệ video
    var videoRatio by remember { mutableStateOf(16 / 9f) }

    // Lắng nghe thay đổi kích thước video
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                if (videoSize.width > 0 && videoSize.height > 0) {
                    videoRatio = videoSize.width.toFloat() / videoSize.height
                }
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // AndroidView hiển thị PlayerView
    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(videoRatio) // cập nhật đúng tỷ lệ video gốc
    )
}
