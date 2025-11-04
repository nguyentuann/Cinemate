package vn.tutorial.cinemate.presentation.home.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.components.Refreshable
import vn.tutorial.cinemate.presentation.home.components.FilmSection
import vn.tutorial.cinemate.presentation.home.components.HeaderBar
import vn.tutorial.cinemate.presentation.home.components.InfinityBanner
import vn.tutorial.cinemate.presentation.home.viewModels.HomeViewModel

val headerItems = mapOf(
    "TV Shows" to {},
    "Movies" to {},
    "My List" to {},
)

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val state = viewModel.state.collectAsState().value

    Refreshable(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.refresh()
            delay(1000)
            isRefreshing = false
        },
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Box {
            HeaderBar(headerItems, modifier = Modifier.zIndex(1f))
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.padding(top = 80.dp))
                }
                if (state.heroBannerFilms.isNotEmpty()) {
                    item {
                        InfinityBanner(
                            state.heroBannerFilms
                        )
                    }
                }

                if (state.sectionFilms.isNotEmpty()) {
                    items(state.sectionFilms.entries.toList()) { (title, films) ->
                        FilmSection(
                            sectionTitle = title,
                            films = films,
                        )
                    }
                }
            }
        }
        LoadingAndError(
            isLoading = state.isLoading,
            error = state.error,
            onErrorDismiss = {
                viewModel.clearError()
            }
        )
    }
}
