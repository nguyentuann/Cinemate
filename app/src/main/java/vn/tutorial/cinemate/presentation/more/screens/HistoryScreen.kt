package vn.tutorial.cinemate.presentation.more.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.components.SearchBar
import vn.tutorial.cinemate.common.components.showToast
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.presentation.more.components.CardMovieItem
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack
import vn.tutorial.cinemate.presentation.more.viewModels.FavoriteViewModel
import vn.tutorial.cinemate.presentation.more.viewModels.HistoryViewModel

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    title: String = "History",
    viewModel: HistoryViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value
    LogUtil("state.dailyHistories: $state")

    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBarWithBack(title = title)
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            // Search bar
//            SearchBar(
//                value = query,
//                onChange = { newValue -> query = newValue }
//            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                state.dailyHistories.forEach { dailyHistory ->
                    val filteredMovies = if (query.isBlank()) {
                        dailyHistory.movies
                    } else {
                        dailyHistory.movies.filter {
                            it.title.contains(query, ignoreCase = true)
                        }
                    }

                    if (filteredMovies.isNotEmpty()) {
                        // Header for the day
                        item {
                            Text(
                                text = dailyHistory.date,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // List movies for that day
                        items(filteredMovies) { movie ->
                            CardMovieItem(
                                movie = movie,
                                isHistory = true,
                                onAddToFavorite = {
                                    favoriteViewModel.addFavorite(it) {
                                        context.showToast("Added to favorites")
                                    }
                                }
                            )
                        }

                        // Load more button if hasMore
                        if (dailyHistory.hasMore) {
                            item {
                                TextButton(
                                    onClick = { viewModel.loadNextPage(dailyHistory.date) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "Load more")
                                }
                            }
                        }
                    }
                }

                // Optional: show empty state
                if (state.dailyHistories.isEmpty() && !state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_movie),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Loading & error
        LoadingAndError(
            isLoading = state.isLoading,
            error = state.error
        ) {
            viewModel.clearError()
        }
    }
}



