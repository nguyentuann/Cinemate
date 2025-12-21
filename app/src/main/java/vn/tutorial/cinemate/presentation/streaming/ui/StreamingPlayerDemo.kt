package vn.tutorial.cinemate.presentation.streaming.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView

@Composable
fun StreamingPlayerDemo(
    clientId: String,
    movieId: String = "9d4309fd-1196-47d7-b891-6419ca195ca8",
    viewModel: StreamingViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    var inputMovieId by remember { mutableStateOf(movieId) }
    
    // Initialize player when movieId changes
    DisposableEffect(inputMovieId) {
        viewModel.initializePlayer(context,inputMovieId)
        onDispose { }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Video Player
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black)
        ) {
            // ExoPlayer view - only create when player is ready
            if (uiState.isPlayerReady) {
                val exoPlayer = viewModel.getExoPlayer()
                if (exoPlayer != null) {
                    AndroidView(
                        factory = { ctx ->
                            Log.d("StreamingPlayerDemo", "Creating PlayerView, exoPlayer: $exoPlayer")
                            PlayerView(ctx).apply {
                                useController = true
                                controllerAutoShow = true
                                controllerHideOnTouch = true
                                this.player = exoPlayer
                                Log.d("StreamingPlayerDemo", "PlayerView created and attached to ExoPlayer")
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                // Show loading while player initializes
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
            
            // Buffering indicator
            if (uiState.isBuffering) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Controls
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Movie ID Input
                Text("Movie ID", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = inputMovieId,
                    onValueChange = { inputMovieId = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter movie ID") }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Play/Pause and Quality
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (uiState.isPlaying) {
                                viewModel.pause()
                            } else {
                                viewModel.play()
                            }
                        },
                        enabled = !uiState.isBuffering
                    ) {
                        Text(
                            when {
                                uiState.isBuffering -> "⏳ Buffering..."
                                uiState.isPlaying -> "⏸️ Pause"
                                else -> "▶️ Play"
                            }
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Quality:", modifier = Modifier.padding(end = 8.dp))
                        
                        var expanded by remember { mutableStateOf(false) }
                        
                        Box {
                            OutlinedButton(onClick = { expanded = true }) {
                                Text(uiState.currentQuality)
                            }
                            
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Auto") },
                                    onClick = {
                                        viewModel.enableAutoQuality()
                                        expanded = false
                                    }
                                )
                                
                                uiState.availableQualities.forEach { quality ->
                                    DropdownMenuItem(
                                        text = { Text(quality) },
                                        onClick = {
                                            viewModel.setManualQuality(quality)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Seek Bar
                Column {
                    Slider(
                        value = uiState.currentTime,
                        onValueChange = { time ->
                            viewModel.seek(time)
                        },
                        valueRange = 0f..uiState.duration.coerceAtLeast(1f),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            formatTime(uiState.currentTime),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            formatTime(uiState.duration),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                // Error display
                uiState.error?.let { errorMsg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(
                        onClick = { viewModel.clearError() },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Dismiss")
                    }
                }
                
                // Stats
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Stats",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Column {
                    StatsRow("Buffer Health", "${uiState.bufferHealth}%")
                    StatsRow("P2P Ratio", "${uiState.p2pRatio}%")
                    StatsRow("Active Peers", "${uiState.activeConnections}")
                    StatsRow("Cache Hit Rate", uiState.cacheHitRate)
                    StatsRow("P2P Fetches", "${uiState.p2pFetches}")
                    StatsRow("HTTP Fetches", "${uiState.httpFetches}")
                }
            }
        }
    }
}

@Composable
private fun StatsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodySmall)
    }
}

private fun formatTime(seconds: Float): String {
    val totalSeconds = seconds.toInt()
    val minutes = totalSeconds / 60
    val secs = totalSeconds % 60
    return String.format("%d:%02d", minutes, secs)
}
