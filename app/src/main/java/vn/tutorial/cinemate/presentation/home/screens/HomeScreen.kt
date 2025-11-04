package vn.tutorial.cinemate.presentation.home.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.home.components.HeaderBar
import vn.tutorial.cinemate.presentation.home.components.InfinityBanner
import vn.tutorial.cinemate.presentation.home.components.MovieSection
import vn.tutorial.cinemate.presentation.home.viewModels.HomeViewModel
import vn.tutorial.cinemate.presentation.home.viewModels.SectionType
import vn.tutorial.cinemate.presentation.home.viewModels.SectionViewModel

val headerItems = mapOf(
    "Movies" to Route.Home.route,
    "History" to Route.History.route,
    "My List" to Route.Favorite.route,
)

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel = hiltViewModel(),
    sectionViewModel: SectionViewModel = hiltViewModel(),
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val homeState = homeViewModel.state.collectAsState().value

    val sectionState = sectionViewModel.sectionsState.collectAsState().value

    Refreshable(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            homeViewModel.refresh()
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
                if (homeState.movies.isNotEmpty()) {
                    item {
                        InfinityBanner(
                            homeState.movies
                        )
                    }
                }

                sectionState.entries.forEach { (sectionType, sectionUIState) ->
                    if (sectionUIState.movies.isNotEmpty()) {
                        item {
                            MovieSection(
                                sectionTitle = sectionType.name,
                                movies = sectionUIState.movies,
                                onLoadMore = {
                                    val sortBy = when (sectionType) {
                                        SectionType.NEW -> "year"
                                        SectionType.TRENDING -> "rating"
                                        SectionType.VIETNAM -> "vietnam"
                                    }
                                    sectionViewModel.getSectionMovies(sectionType, sortBy)
                                }
                            )
                        }
                    }
                }
            }
        }
        LoadingAndError(
            isLoading = homeState.isLoading,
            error = homeState.error,
            onErrorDismiss = {
                homeViewModel.clearError()
            }
        )
    }
}
