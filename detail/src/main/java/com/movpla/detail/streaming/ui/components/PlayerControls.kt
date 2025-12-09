package com.movpla.detail.streaming.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.movpla.detail.streaming.data.model.PlaybackState
import com.movpla.detail.streaming.data.model.SkipTime

@Composable
fun PlayerControls(
    playbackState: PlaybackState,
    isVisible: Boolean,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    onSeekForward: () -> Unit,
    onSeekBackward: () -> Unit,
    onBack: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onShowSettings: () -> Unit,
    skipIntro: SkipTime?,
    skipOutro: SkipTime?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        ) {
            // Top gradient bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Transparent
                            )
                        )
                    )
                    .align(Alignment.TopStart)
            ) {
                TopControls(
                    onBack = onBack,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            // Center controls
            CenterControls(
                isPlaying = playbackState.isPlaying,
                onPlayPause = onPlayPause,
                onSeekForward = onSeekForward,
                onSeekBackward = onSeekBackward,
                modifier = Modifier.align(Alignment.Center)
            )
            
            // Bottom gradient bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .align(Alignment.BottomStart)
            ) {
                BottomControls(
                    playbackState = playbackState,
                    onSeek = onSeek,
                    onToggleFullscreen = onToggleFullscreen,
                    onShowSettings = onShowSettings,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                )
            }
            
            // Skip buttons
            SkipButtons(
                playbackState = playbackState,
                skipIntro = skipIntro,
                skipOutro = skipOutro,
                onSkip = onSeek,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun TopControls(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
}

@Composable
private fun CenterControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onSeekForward: () -> Unit,
    onSeekBackward: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onSeekBackward,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                Icons.Default.Replay10,
                contentDescription = "Seek backward",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
        
        IconButton(
            onClick = onPlayPause,
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.White,
                modifier = Modifier.size(56.dp)
            )
        }
        
        IconButton(
            onClick = onSeekForward,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                Icons.Default.Forward10,
                contentDescription = "Seek forward",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
private fun BottomControls(
    playbackState: PlaybackState,
    onSeek: (Float) -> Unit,
    onToggleFullscreen: () -> Unit,
    onShowSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Progress bar
        Slider(
            value = playbackState.currentTime,
            onValueChange = onSeek,
            valueRange = 0f..playbackState.duration.coerceAtLeast(1f),
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Red,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )
        
        // Time and buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${formatTime(playbackState.currentTime)} / ${formatTime(playbackState.duration)}",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
            
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onShowSettings) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
                
                IconButton(onClick = onToggleFullscreen) {
                    Icon(
                        Icons.Default.Fullscreen,
                        contentDescription = "Fullscreen",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun SkipButtons(
    playbackState: PlaybackState,
    skipIntro: SkipTime?,
    skipOutro: SkipTime?,
    onSkip: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(bottom = 140.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        skipIntro?.let {
            AnimatedVisibility(
                visible = playbackState.currentTime in it.start..it.end
            ) {
                Button(
                    onClick = { onSkip(it.end) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.9f),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Skip Intro")
                }
            }
        }
        
        skipOutro?.let {
            AnimatedVisibility(
                visible = playbackState.currentTime in it.start..it.end
            ) {
                Button(
                    onClick = { onSkip(it.end) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.9f),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Skip Outro")
                }
            }
        }
    }
}

private fun formatTime(seconds: Float): String {
    val totalSeconds = seconds.toInt().coerceAtLeast(0)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val secs = totalSeconds % 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format("%d:%02d", minutes, secs)
    }
}
