package vn.tutorial.cinemate.presentation.search

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.components.SearchBar
import vn.tutorial.cinemate.presentation.more.components.CardMovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val films = state.filmResults
    val trendingFilms = state.trendingFilms

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearResults()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        value = state.query,
                        onChange = { viewModel.updateQuery(it) },
                        onSearch = { viewModel.search() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .padding(it)
                .padding(horizontal = 16.dp).padding(bottom = 80.dp)
        ) {
            if (films.isNotEmpty()) {
                item {
                    Text(
                        stringResource(R.string.result),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(films) { film ->
                    CardMovieItem(film, isSearch = true)
                }
            } else if (trendingFilms.isNotEmpty() && state.isLoading == false) {

                item {
                    Text(
                        stringResource(R.string.trending),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(trendingFilms) { film ->
                    CardMovieItem(film, isSearch = true)

                }
            }
        }

        LoadingAndError(
            isLoading = state.isLoading,
            error = state.error
        ) {
            viewModel.clearError()
        }
    }
}