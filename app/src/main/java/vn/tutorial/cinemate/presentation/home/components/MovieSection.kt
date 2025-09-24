package vn.tutorial.cinemate.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.presentation.home.mock.Movie

@Composable
fun MovieSection(
    sectionTitle: String,
    movies: List<Movie>
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.titleSmall
        )
        LazyRow {
            items(movies) { movie ->
                SectionItem(
                    movie = movie
                )
            }
        }
    }
}

@Composable
private fun SectionItem(
    modifier: Modifier = Modifier,
    movie: Movie,
) {
    AsyncImage(
        modifier = modifier
            .height(200.dp)
            .clickable(onClick = {})
            .padding(8.dp)
            .clip(
                shape = Styles.ShapeStyles.mediumCorner,
            )
            .aspectRatio(2f / 3f),
        model = movie.posterUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}