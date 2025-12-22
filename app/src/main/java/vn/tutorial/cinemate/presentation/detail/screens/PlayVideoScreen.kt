package vn.tutorial.cinemate.presentation.detail.screens

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import vn.tutorial.cinemate.presentation.detail.components.VideoPlayer
import vn.tutorial.cinemate.presentation.streaming.ui.VideoPlayer2

@OptIn(UnstableApi::class)
@Composable
fun PlayVideoScreen(movieId: String) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            VideoPlayer2(movieId)
        }
    }
}
