package vn.tutorial.cinemate.presentation.streaming.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.presentation.detail.components.CustomSlider
import vn.tutorial.cinemate.presentation.detail.components.findActivity

fun Context.findActivity2(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayer2(
    movieId: String,
    viewModel: StreamingViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val activity = context.findActivity2() ?: return
    val navController = LocalNavController.current

    // Initialize player
    LaunchedEffect(movieId) {
        viewModel.initializePlayer(context, movieId)
        viewModel.getProgress(movieId)
    }

    val uiState by viewModel.uiState.collectAsState()
    
    // Restore progress when player is ready and progress is loaded
    LaunchedEffect(uiState.isPlayerReady, uiState.initialProgress) {
        if (uiState.isPlayerReady && uiState.initialProgress != null && !uiState.hasRestoredProgress) {
            delay(500) // Small delay to ensure player is fully ready
            viewModel.restoreProgress()
        }
    }
    
    val controlsVisible = true
    val locked = uiState.locked
    val speed = uiState.speed
    val silent = uiState.isSilent
    val isPlaying = uiState.isPlaying

    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    val availableQualities = uiState.availableQualities
    val currentQuality = uiState.currentQuality


    var showQualityMenu by remember { mutableStateOf(false) }


    // update progress
    LaunchedEffect(Unit) {
        while (true) {
            withContext(Dispatchers.Main) {
                while (true) {
                    currentPosition = viewModel.getCurrentPosition()
                    duration = viewModel.getDuration()
                    delay(500)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            activity.exitFullscreen2()
            println("🎬 ExoPlayer released")
        }
    }

    // todo handle back press
    BackHandler {
        viewModel.reportProgress(movieId)
        navController.popBackStack()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentViewModel by rememberUpdatedState(viewModel)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                currentViewModel.reportProgress(movieId)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures() {
                    viewModel.toggleControls()
                }
            }
    )
    {
        if (uiState.isPlayerReady) {
            val exoPlayer = viewModel.getExoPlayer()

            // Only show PlayerView if ExoPlayer is available
            if (exoPlayer != null) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = false
                        }
                    },
                    update = { playerView ->
                        // Update player reference if it changes
                        if (playerView.player != exoPlayer) {
                            playerView.player = exoPlayer
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
        }
        if (controlsVisible && !locked) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 32.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    // todo close button
                    IconButton(
                        modifier = Modifier.semantics {
                            contentDescription = "close_video_button"
                        },
                        onClick = {
                            viewModel.reportProgress(movieId)
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            AppIcons.close(),
                            null,
                            tint = Color.White,
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // todo back 10s
                    IconButton(
                        modifier = Modifier.semantics {
                            contentDescription = "backward_10_button"
                        },
                        onClick = { viewModel.seekBack() }
                    ) {
                        Icon(
                            AppIcons.backward10(),
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // todo play/pause
                    IconButton(
                        modifier = Modifier.semantics {
                            contentDescription = "play_pause_button"
                        },
                        onClick = {
                            if (isPlaying) viewModel.pause()
                            else viewModel.play()
                        }
                    ) {
                        Icon(
                            if (isPlaying) AppIcons.pause() else AppIcons.play(),
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // todo forward 10s
                    IconButton(
                        modifier = Modifier.semantics {
                            contentDescription = "forward_10_button"
                        },
                        onClick = { viewModel.seekForward() }
                    ) {
                        Icon(
                            AppIcons.forward10(),
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                val safeDuration = if (duration > 0) duration.toFloat() else 1f
                val safePosition = currentPosition.coerceIn(0L, duration.coerceAtLeast(0L)).toFloat()

                CustomSlider(
                    position = safePosition,
                    duration = safeDuration,
                    onValueChange = { viewModel.seekTo(it.toLong()) },
                    modifier = Modifier.fillMaxWidth()
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    // todo quality
                    TextButton(
                        modifier = Modifier.semantics {
                            contentDescription = "quality_button"
                        },
                        onClick = { showQualityMenu = true }
                    ) {
                        Text(currentQuality, color = Color.White)
                    }

                    DropdownMenu(
                        expanded = showQualityMenu,
                        onDismissRequest = { showQualityMenu = false },
                    ) {
                        availableQualities.forEach { quality ->
                            DropdownMenuItem(
                                modifier = Modifier.semantics {
                                    contentDescription = "${quality}_quality_option"
                                },
                                text = { Text(quality) },
                                onClick = {
                                    viewModel.switchQuality(quality)
                                    showQualityMenu = false
                                }
                            )
                        }
                    }

                    // todo playback speed
                    TextButton(
                        modifier = Modifier.semantics {
                            contentDescription = "speed_button"
                        },
                        onClick = { viewModel.changeSpeed() }
                    ) {
                        Text("${speed}x", color = Color.White)
                    }

                    // todo lock controls
                    IconButton(
                        modifier = Modifier.semantics {
                            contentDescription = "silent_button"
                        },
                        onClick = { viewModel.toggleSilent() }) {
                        Icon(
                            if (silent) AppIcons.silent() else AppIcons.sound(),
                            null,
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(AppIcons.sub(), null, tint = Color.White)
                    }

                    IconButton(
                        modifier = Modifier.semantics {
                            contentDescription = "lock_button"
                        },
                        onClick = { viewModel.toggleLock()
                        }
                    ) {
                        Icon(AppIcons.unlock(), null, tint = Color.White)
                    }
                }
            }
        }
        if (locked && controlsVisible) {
            // todo khi lock, chỉ hiện nút unlock ở giữa màn hình
            IconButton(
                onClick = { viewModel.toggleLock() },
                modifier = Modifier
                    .align(Alignment.Center)
                    .semantics {
                        contentDescription = "unlock_button"
                    }
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
            showQualityMenu = false
            viewModel.toggleControls()
        }
    }

    LaunchedEffect(Unit) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        activity.enterFullscreen2()
    }
}

fun Activity.enterFullscreen2() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Activity.exitFullscreen2() {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
}


