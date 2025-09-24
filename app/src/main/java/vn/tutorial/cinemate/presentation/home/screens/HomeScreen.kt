package vn.tutorial.cinemate.presentation.home.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vn.tutorial.cinemate.presentation.home.components.HeroBanner
import vn.tutorial.cinemate.presentation.home.components.MovieSection
import vn.tutorial.cinemate.presentation.home.mock.bannerMovie
import vn.tutorial.cinemate.presentation.home.mock.sectionData

@Composable
fun HomeScreen(
) {
    Scaffold(
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            item {
                HeroBanner(movie = bannerMovie)
            }

            sectionData.forEach { (title, movies) ->
                item {
                    MovieSection(
                        sectionTitle = title,
                        movies = movies
                    )
                }
            }
        }
    }
}