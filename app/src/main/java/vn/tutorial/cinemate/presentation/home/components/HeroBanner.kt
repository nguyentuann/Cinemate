package vn.tutorial.cinemate.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import vn.tutorial.cinemate.common.components.AsyncImageWithReplace
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.R
@Composable
fun HeroBanner(
    modifier: Modifier = Modifier,
    film: MovieDetailModel,
) {
    Box(modifier = modifier) {
        AsyncImageWithReplace(
            model = film.verticalPoster,
            contentDescription = null,
            modifier = Modifier.height(400.dp),
            contentScale = ContentScale.Crop,
            imgReplace = R.drawable.poster_error
        )
    }
}


