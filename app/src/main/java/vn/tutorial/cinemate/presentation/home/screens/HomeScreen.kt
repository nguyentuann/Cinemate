package vn.tutorial.cinemate.presentation.home.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.presentation.home.components.FilmSection
import vn.tutorial.cinemate.presentation.home.components.HeroBanner
import vn.tutorial.cinemate.presentation.home.mock.bannerFilm
import vn.tutorial.cinemate.presentation.home.mock.sectionData

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        item {
            HeroBanner(film = bannerFilm, navController = navController)
        }

        sectionData.forEach { (title, films) ->
            item {
                FilmSection(
                    sectionTitle = title,
                    films = films,
                )
            }
        }
    }
}
