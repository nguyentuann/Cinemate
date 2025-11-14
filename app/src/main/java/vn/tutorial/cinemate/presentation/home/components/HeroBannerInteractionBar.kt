package vn.tutorial.cinemate.presentation.home.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.InteractionButton
import vn.tutorial.cinemate.common.components.showToast
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.detail.viewModels.DetailViewModel
import vn.tutorial.cinemate.presentation.more.viewModels.FavoriteViewModel

@Composable
fun HeroBannerInteractionBar(
    modifier: Modifier = Modifier,
    movie: MovieDetailModel,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    detailViewModel: DetailViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val toastMessage = stringResource(R.string.added)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,

        ) {
        InteractionButton(
            modifier = Modifier.weight(3f),
            icon = AppIcons.add(),
            title = stringResource(R.string.my_list),
            onClick = {
                favoriteViewModel.addFavorite(movie.id) {
                    context.showToast(toastMessage)
                }
            }
        )
        Button(
            modifier = Modifier
                .fillMaxHeight()
                .weight(4f),
            onClick = {
                LogUtil(movie.qualities.toString())
                detailViewModel.getDetailMovie(movie.id) {
                    navController.navigate(Route.PlayVideo.createRoute(movie.id))
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.Black
            ),
            shape = Styles.ShapeStyles.mediumCorner,
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Play", style = MaterialTheme.typography.bodyLarge)
        }

        InteractionButton(
            modifier = Modifier.weight(3f),
            icon = AppIcons.info(),
            title = stringResource(R.string.movie_info),
            onClick = {
                navController.navigate(Route.Detail.createRoute(movie.id))
            }
        )
    }
}
