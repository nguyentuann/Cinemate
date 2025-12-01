package vn.tutorial.cinemate.presentation.search.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.components.SearchBar
import vn.tutorial.cinemate.presentation.search.components.CategoryBar
import vn.tutorial.cinemate.presentation.search.components.ResultMovieList
import vn.tutorial.cinemate.presentation.search.viewModels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val state = searchViewModel.state.collectAsState().value

    val (movies, isSearch) = when {
        !state.movieByCategoryResults.isNullOrEmpty() ->
            Pair(state.movieByCategoryResults, false)

        else ->
            Pair(state.movieResults, true)
    }

    DisposableEffect(Unit) {
        onDispose {
            searchViewModel.clearResults()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        value = state.query,
                        onChange = { searchViewModel.updateQuery(it) },
                        onSearch = {
                            searchViewModel.startNewSearch()
                            searchViewModel.search()
                        }
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

        Column(
            modifier
                .padding(horizontal = 16.dp)
                .padding(it)
                .fillMaxSize()
        ) {
            CategoryBar(
                onCategorySelected = { category ->
                    searchViewModel.updateCategory(category.id)
                    searchViewModel.startNewSearch()
                    searchViewModel.getMoviesByCategory()
                }
            )

            ResultMovieList(
                movies = movies,
                condition = state.hasMore && !state.isLoading && isSearch,
            ) {
                searchViewModel.search()
            }

        }
    }

    LoadingAndError(
        isLoading = state.isLoading,
        error = state.error
    ) {
        searchViewModel.clearError()
    }
}
