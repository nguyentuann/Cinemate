package vn.tutorial.cinemate.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AsyncImageWithReplace
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.core.util.timeFormatter
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route

@Composable
fun MovieSection(
    modifier: Modifier = Modifier,
    sectionTitle: String,
    movies: List<MovieDetailModel>,
    onLoadMore: (() -> Unit)? = null // callback load more
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Text(
            text = sectionTitle
                .lowercase()
                .replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )

        LazyRow(
            contentPadding = PaddingValues(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(movies) { index, movie ->
                MoviePosterItem(movie = movie, rank = index + 1)
                // Khi scroll tới cuối, gọi load more
                if (movies.isNotEmpty() && index == movies.lastIndex) {
                    onLoadMore?.invoke()
                }
            }
        }
    }
}

@Composable
private fun MoviePosterItem(
    rank: Int,
    modifier: Modifier = Modifier,
    movie: MovieDetailModel,
) {
    val navController = LocalNavController.current
    Box(
        modifier = modifier
            .height(140.dp)
            .width(220.dp)
            .padding(vertical = 8.dp)
            .clip(Styles.ShapeStyles.mediumCorner)
            .clickable {
                navController.navigate(Route.Detail.createRoute(movie.id))
            }
    ) {
        AsyncImageWithReplace(
            modifier = Modifier.fillMaxSize(),
            model = movie.horizontalPoster ?: "",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            imgReplace = R.drawable.poster_error
        )

        if (movie.rank != null) {
            Text(
                text = "#${movie.rank}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(Color.Red, Styles.ShapeStyles.smallCorner)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }



        if (movie.year != null) {
            Text(
                text = movie.year.toString(),
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.Red, Styles.ShapeStyles.smallCorner)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }


        if (movie.durationMinutes != null) {
            Text(
                text = timeFormatter(movie.durationMinutes * 60_000L),
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(alpha = 0.6f), Styles.ShapeStyles.smallCorner)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

    }
}