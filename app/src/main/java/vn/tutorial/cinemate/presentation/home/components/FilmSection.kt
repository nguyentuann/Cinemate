package vn.tutorial.cinemate.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.core.util.timeFormatter
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route

@Composable
fun FilmSection(
    modifier: Modifier = Modifier,
    sectionTitle: String,
    films: List<FilmDetailModel>,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.titleSmall
        )
        LazyRow {
            items(films) { film ->
                FilmPosterItem(
                    film = film,
                )
            }
        }
    }
}

@Composable
private fun FilmPosterItem(
    modifier: Modifier = Modifier,
    film: FilmDetailModel,
) {
    val navController = LocalNavController.current
    Box(
        modifier = modifier
            .height(120.dp)
            .padding(8.dp)
            .clip(Styles.ShapeStyles.mediumCorner)
            .clickable {
                navController.navigate(Route.Detail.createRoute(film.id))
            }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = film.horizontalPoster,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )

        Text(
            text = "#${film.rank}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(Color.Red.copy(alpha = 0.6f), Styles.ShapeStyles.smallCorner)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )

        Text(
            text = film.year.toString(),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(Color.Red.copy(alpha = 0.6f), Styles.ShapeStyles.smallCorner)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )

        Text(
            text = timeFormatter(film.durationMinutes * 60_000L),
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(Color.Black.copy(alpha = 0.6f), Styles.ShapeStyles.smallCorner)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}