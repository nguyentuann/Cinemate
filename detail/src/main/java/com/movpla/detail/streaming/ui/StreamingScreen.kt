package com.movpla.detail.streaming.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.movpla.detail.streaming.data.model.StreamingData
import com.movpla.detail.streaming.player.HlsPlayerManager

@UnstableApi
@Composable
fun StreamingScreen(
    streamingData: StreamingData,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val playerManager = remember { HlsPlayerManager(context) }
    val playbackState by playerManager.playbackState.collectAsState()
    
    var showControls by remember { mutableStateOf(true) }
    
    DisposableEffect(Unit) {
        val player = playerManager.initializePlayer(streamingData)
        onDispose {
            playerManager.releasePlayer()
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Player View
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = playerManager.initializePlayer(streamingData)
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Custom Controls
        if (showControls) {
            StreamingControls(
                playbackState = playbackState,
                onPlayPause = {
                    if (playbackState.isPlaying) playerManager.pause()
                    else playerManager.play()
                },
                onSeek = { position ->
                    playerManager.seekTo(position)
                },
                onBack = onBackPressed,
                onSpeedChange = { speed ->
                    playerManager.setPlaybackSpeed(speed)
                },
                onVolumeChange = { volume ->
                    playerManager.setVolume(volume)
                },
                skipIntro = streamingData.skipIntro,
                skipOutro = streamingData.skipOutro,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun StreamingControls(
    playbackState: PlaybackState,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    onBack: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    skipIntro: com.movpla.detail.streaming.data.model.SkipTime?,
    skipOutro: com.movpla.detail.streaming.data.model.SkipTime?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp)
                .align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        
        // Center Play/Pause
        IconButton(
            onClick = onPlayPause,
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.Center)
        ) {
            Icon(
                if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
        
        // Bottom Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            // Progress Bar
            Slider(
                value = playbackState.currentTime,
                onValueChange = onSeek,
                valueRange = 0f..playbackState.duration,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Time and Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTime(playbackState.currentTime),
                    color = Color.White
                )
                
                Row {
                    // Settings, Quality, etc.
                    IconButton(onClick = { /* Show settings */ }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
                
                Text(
                    text = formatTime(playbackState.duration),
                    color = Color.White
                )
            }
        }
        
        // Skip Intro/Outro buttons
        skipIntro?.let {
            if (playbackState.currentTime in it.start..it.end) {
                Button(
                    onClick = { onSeek(it.end) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 100.dp, end = 16.dp)
                ) {
                    Text("Skip Intro")
                }
            }
        }
    }
}

private fun formatTime(seconds: Float): String {
    val totalSeconds = seconds.toInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val secs = totalSeconds % 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format("%d:%02d", minutes, secs)
    }
}
