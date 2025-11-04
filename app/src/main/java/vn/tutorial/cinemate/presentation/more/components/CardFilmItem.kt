package vn.tutorial.cinemate.presentation.more.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.RatingBar
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.core.util.timeFormatter
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route

@Composable
fun CardMovieItem(
    movie: MovieDetailModel,
    modifier: Modifier = Modifier,
    onDelete: (String) -> Unit = {},
    onAddToFavorite: (String) -> Unit = {},
    isHistory: Boolean = false,
    isSearch: Boolean = false,
) {
    val navController = LocalNavController.current
    var showActions by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        shape = Styles.ShapeStyles.mediumCorner,

        ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .height(120.dp)
                    .width(200.dp)
                    .clip(Styles.ShapeStyles.mediumCorner)
                    .clickable {
                        navController.navigate(Route.PlayVideo.createRoute(movie.id))
                    }
            ) {
                // Poster
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = movie.horizontalPoster,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = timeFormatter(movie.durationMinutes!! * 60 * 1000L),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            Styles.ShapeStyles.smallCorner
                        )
                        .padding(horizontal = 6.dp, vertical = 6.dp)
                )
                // Thanh progress (đặt dưới cùng)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.White.copy(alpha = 0.7f))
                ) {
                    val progress =
                        movie.watchDurationMinutes!!.toFloat() / movie.durationMinutes.toFloat()
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .background(Color.Red)
                    )
                }
            }

            // Thông tin phim bên cạnh (tuỳ chỉnh)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(min = 120.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1
                    )

                    Text(
                        text = movie.description.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    RatingBar(rating = movie.rating!!, starSize = 16.dp)

                }

                if (!isSearch) {
                    IconButton(
                        modifier = Modifier.size(36.dp),
                        onClick = {
                            showActions = true
                        },
                    ) {
                        Icon(

                            AppIcons.option(),
                            contentDescription = null,
                        )
                    }
                }

            }

            if (showActions) {
                ActionBottomSheet(
                    movieId =  movie.id,
                    onDelete = onDelete,
                    onAddToFavorite = onAddToFavorite,
                    onDismiss = { showActions = false },
                    isHistory = isHistory
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionBottomSheet(
    movieId: String,
    onDelete: (String) -> Unit,
    onAddToFavorite: (String) -> Unit,
    onDismiss: () -> Unit,
    isHistory: Boolean = false,
) {
    ModalBottomSheet(
        modifier = Modifier.navigationBarsPadding()
            .padding(horizontal = 12.dp),
        onDismissRequest = onDismiss,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier
                    .size(48.dp, 4.dp)
                    .clip(
                        Styles.ShapeStyles.largeCorner
                    )
                    .background(
                        MaterialTheme.colorScheme.onSurfaceVariant
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionItem(AppIcons.delete(), stringResource(R.string.delete)) {
                onDelete(movieId)
                onDismiss()
            }
            if (isHistory) {
                ActionItem(AppIcons.add(), stringResource(R.string.add_to_favorite)) {
                    onAddToFavorite(movieId)
                    onDismiss()
                }
            }
            ActionItem(AppIcons.share(), stringResource(R.string.share)) { }
        }
    }
}

@Composable
private fun ActionItem(
    icon: Painter,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Icon(
            icon,
            contentDescription = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .padding(vertical = 16.dp)
        )
    }
}