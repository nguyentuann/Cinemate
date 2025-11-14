package vn.tutorial.cinemate.presentation.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.presentation.more.components.CardMovieItem

@Composable
fun ResultMovieList(
    movies: List<MovieDetailModel>?,
    condition: Boolean,
    onLoadMore: () -> Unit,
) {
    LogUtil("dieu kien load more $condition")
    if (movies?.isNotEmpty() == true) {
        Text(
            stringResource(R.string.result),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            itemsIndexed(
                items = movies,
                key = { _, movie -> movie.id }
            ) { index, movie ->
                CardMovieItem(movie, isSearch = true)
                if (index == movies.lastIndex && condition) {
                    LaunchedEffect(key1 = movies.lastIndex) {
                        onLoadMore()
                    }
                }
            }
        }

    } else if (movies?.isEmpty() == true) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_movie),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}