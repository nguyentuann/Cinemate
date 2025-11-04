package vn.tutorial.cinemate.presentation.home.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import kotlin.math.absoluteValue

@Composable
fun InfinityBanner(
    movies: List<MovieDetailModel>,
    modifier: Modifier = Modifier,
) {
    var currentMovie = movies[1]
    Column {
        val totalPages = Int.MAX_VALUE
        val pagerState = rememberPagerState(
            initialPage = 1,
            pageCount = { totalPages }
        )

        LaunchedEffect(Unit) {
            while (true) {
                delay(4000)
                pagerState.animateScrollToPage(
                    pagerState.currentPage + 1,
                    animationSpec = tween(
                        durationMillis = 900,
                        easing = LinearOutSlowInEasing
                    )
                )

            }
        }
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 80.dp),
            pageSpacing = 0.dp,
            modifier = modifier.fillMaxWidth()
        ) { pageIndex ->
            val filmIndex = pageIndex % movies.size
            val film = movies[filmIndex]

            val currentFilmIndex = pagerState.currentPage % movies.size
            currentMovie = movies[currentFilmIndex]

            val pageOffset = (
                    (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction
                    ).absoluteValue

            val scale = lerp(0.8f, 1f, 1 - pageOffset.coerceIn(0f, 1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeroBanner(
                    modifier = Modifier.graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                    film = film,
                )

                if (pageOffset < 0.5f) {
                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.CenterHorizontally),
                        text = film.genres!!.joinToString(" • "),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

            }

        }

        HeroBannerInteractionBar(movie = currentMovie)
    }
}