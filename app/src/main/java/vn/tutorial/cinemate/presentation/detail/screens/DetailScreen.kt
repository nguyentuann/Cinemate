package vn.tutorial.cinemate.presentation.detail.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.showToast
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.detail.components.CommentBottomSheet
import vn.tutorial.cinemate.presentation.detail.components.InteractionBar
import vn.tutorial.cinemate.presentation.detail.components.MovieInformation
import vn.tutorial.cinemate.presentation.detail.components.TrailerPlayer
import vn.tutorial.cinemate.presentation.detail.viewModels.DetailViewModel
import vn.tutorial.cinemate.presentation.home.components.MovieSection
import vn.tutorial.cinemate.presentation.more.viewModels.FavoriteViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    movieId: String,
    viewModel: DetailViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    LogUtil(movieId)
    val navController = LocalNavController.current
    val context = LocalContext.current

    val scrollState = rememberScrollState()
    var showComments by remember { mutableStateOf(false) }
    val state = viewModel.state.collectAsState().value
    val movieDetail = state.movieDetail
    val recommendMovies = state.recommendMovies

    val toastMessage = stringResource(R.string.added)
    LaunchedEffect(Unit) {
        viewModel.getDetailMovie(movieId)
        viewModel.getRecommendMovies()
    }

    Scaffold {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState)
        ) {

            // todo trailer
            if (movieDetail?.trailerUrl != null) {
                TrailerPlayer(movieDetail.trailerUrl)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No trailer available", color = Color.White)
                }
            }


            Spacer( modifier = Modifier.height(16.dp))

            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    modifier = Modifier
                        .fillMaxWidth().semantics {
                            contentDescription = "detail_play_button"
                        },
                    onClick = {
                        LogUtil(movieDetail.toString())
                        navController.navigate(
                            Route.PlayVideo.createRoute(
                                movieId
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.Black
                    ),
                    shape = Styles.ShapeStyles.mediumCorner,
                ) {
                    Icon(
                        AppIcons.play(),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(R.string.play), style = MaterialTheme.typography.bodyLarge)
                }

                movieDetail?.let {
                    MovieInformation(movieDetail)
                }

                HorizontalDivider()

                InteractionBar(
                    filmId = movieId,
                    onComment = {
                        showComments = true
                    },
                    onMyList = {
                        favoriteViewModel.addFavorite(movieId) {
                            context.showToast(toastMessage)
                        }
                    }
                )

                if (showComments) {
                    CommentBottomSheet(
                        movieId = movieId,
                        onDismiss = { showComments = false }
                    )
                }

                HorizontalDivider(
                    Modifier.padding(bottom = 16.dp)
                )

            }

            // recommend movies
            MovieSection(
                sectionTitle = stringResource(R.string.more_like_this),
                movies = recommendMovies,
                onLoadMore = {
                    if (!state.isLoading && state.hasMore) {
                        viewModel.getRecommendMovies()
                    }
                }
            )
        }
    }
}


