package vn.tutorial.cinemate.presentation.detail.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.domain.model.filmMock
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.detail.components.FilmInformation
import vn.tutorial.cinemate.presentation.detail.components.InteractionBar
import vn.tutorial.cinemate.presentation.detail.components.TrailerPlayer
import vn.tutorial.cinemate.presentation.home.components.FilmSection
import vn.tutorial.cinemate.presentation.home.mock.bannerFilm
import vn.tutorial.cinemate.presentation.home.mock.sectionData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    filmId: String,
) {
    val navController = LocalNavController.current
    val movie = bannerFilm
    val scrollState = rememberScrollState()

    Scaffold {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState)
        ) {

            // todo trailer
            TrailerPlayer(filmMock.trailerUrl)

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        navController.navigate(Route.PlayVideo.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.Black
                    ),
                    shape = Styles.ShapeStyles.mediumCorner,
                ) {
                    Icon(
                        AppIcons.play(),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Play", style = MaterialTheme.typography.bodyLarge)
                }

                FilmInformation()

                HorizontalDivider()

                InteractionBar()
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(16.dp)
            )
            // recommend movies
            val moviesList = sectionData.map { (_, movies) -> movies }
            FilmSection(
                sectionTitle = "More Like This",
                films = moviesList.flatten(),
            )
        }
    }
}

