package vn.tutorial.cinemate.presentation.detail.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.presentation.detail.components.FilmInformation
import vn.tutorial.cinemate.presentation.detail.components.VideoPlayer

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
            VideoPlayer()
            HorizontalDivider()
            Row(
                Modifier.padding(16.dp),
            ) {
                FilmInformation()
            }
        }
    }
}
