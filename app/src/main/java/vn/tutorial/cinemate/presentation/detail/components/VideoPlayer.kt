package vn.tutorial.cinemate.presentation.detail.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.presentation.detail.viewModels.PlayVideoViewModel

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    viewModel: PlayVideoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context.findActivity() ?: return

    val exoPlayer = viewModel.exoPlayer
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isFullscreen by viewModel.isFullscreen.collectAsState()
    val speed by viewModel.speed.collectAsState()
    val locked by viewModel.locked.collectAsState()
    val position by viewModel.position.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val controlsVisible by viewModel.controlsVisible.collectAsState()

    LaunchedEffect(exoPlayer) {
        viewModel.setMedia("https://tiktok-clone-taplamit.s3.ap-southeast-2.amazonaws.com/videos-hls/FF2_a_vyVj68aOyI4V1nZ/master.m3u8")
    }

    // update progress
    LaunchedEffect(exoPlayer) {
        while (true) {
            withContext(Dispatchers.Main) {
                viewModel.updateProgress()
                delay(500)
            }
        }
    }
    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    viewModel.toggleControls()
                }
            }) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isFullscreen) Modifier.fillMaxHeight() else Modifier.aspectRatio(16f / 9f)
                )
        )

        if (controlsVisible && !locked) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                CustomSlider(
                    position = position.toFloat(),
                    duration = duration.toFloat(),
                    onValueChange = { viewModel.seekTo(it.toLong()) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // back 10s
                    IconButton(onClick = { viewModel.seekBack() }) {
                        Icon(AppIcons.backward10(), null, tint = Color.White)
                    }

                    // play/pause
                    IconButton(onClick = {
                        viewModel.togglePlay()
                    }) {
                        Icon(
                            if (isPlaying) AppIcons.pause() else AppIcons.play(),
                            null,
                            tint = Color.White,
                        )
                    }

                    // forward 10s
                    IconButton(onClick = { viewModel.seekForward() }) {
                        Icon(AppIcons.forward10(), null, tint = Color.White)
                    }

                    // playback speed
                    TextButton(onClick = {
                        viewModel.changeSpeed()
                    }) {
                        Text("${speed}x", color = Color.White)
                    }

                    // fullscreen toggle
                    IconButton(onClick = {
                        viewModel.toggleFullscreen()
                    }) {
                        Icon(
                            painter = if (isFullscreen) AppIcons.fullscreenExit() else AppIcons.fullscreen(),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    // lock controls
                    IconButton(onClick = { viewModel.toggleLock() }) {
                        Icon(AppIcons.unlock(), null, tint = Color.White)
                    }
                }
            }
        }
        if (locked && controlsVisible) {
            // khi lock, chỉ hiện nút unlock ở giữa màn hình
            IconButton(
                onClick = { viewModel.toggleLock() },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    AppIcons.lock(),
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }


    LaunchedEffect(controlsVisible) {
        if (controlsVisible) {
            delay(3000)
            viewModel.toggleControls()
        }
    }

    LaunchedEffect(isFullscreen) {
        if (isFullscreen) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            activity.enterFullscreen()
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            activity.exitFullscreen()
        }
    }
}

fun Activity.enterFullscreen() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Activity.exitFullscreen() {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
}


