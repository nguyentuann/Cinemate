import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.presentation.more.components.CardMovieItem
import vn.tutorial.cinemate.presentation.more.components.TopAppBarWithBack
import vn.tutorial.cinemate.presentation.more.viewModels.FavoriteViewModel

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = hiltViewModel()

) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getFavoriteMovies()
    }

    Scaffold(
        topBar = {
            TopAppBarWithBack(stringResource(R.string.favorite))
        }
    ) {
        Column(
            modifier
                .padding(it)
                .padding(
                    horizontal = 16.dp
                )
        ) {
            if (state.movies.isNotEmpty()) {
                LazyColumn {
                    items(state.movies) { movie ->
                        CardMovieItem(
                            movie = movie,
                            onDelete = { movieId ->
                                viewModel.deleteFavorite(movieId)
                            },
                        )
                    }
                }
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No favorite movies found.",
                    textAlign = TextAlign.Center
                )
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