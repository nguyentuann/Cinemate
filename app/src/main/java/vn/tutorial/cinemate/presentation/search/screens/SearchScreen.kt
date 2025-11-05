package vn.tutorial.cinemate.presentation.search.screens

import androidx.compose.foundation.layout.Column
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
import vn.tutorial.cinemate.presentation.search.components.CategoryBar
import vn.tutorial.cinemate.presentation.search.viewModels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val state = searchViewModel.state.collectAsState().value
    val films = state.filmResults

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
                        onSearch = { searchViewModel.search() }
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
            Modifier.padding(horizontal = 16.dp).padding(it)
        ) {
            CategoryBar(
                onCategorySelected = { category ->
                    searchViewModel.updateCategory(category.name)
                }
            )


            LazyColumn(
                modifier = modifier
                    .padding(bottom = 80.dp)
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
}