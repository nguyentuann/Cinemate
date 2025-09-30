package vn.tutorial.cinemate.presentation.more.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import vn.tutorial.cinemate.common.components.ExpandableText
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.core.util.timeFormatter
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route

@Composable
fun CardFilmItem(
    film: FilmDetailModel,
    modifier: Modifier = Modifier,
    onDelete: (FilmDetailModel) -> Unit = { _ -> },
) {
    val navController = LocalNavController.current
    var showActions by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.StartToEnd || dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete(film)
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val color = if (dismissState.progress < 1f) Color.Red else Color.Transparent

            val alignment = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.Center
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .clip(Styles.ShapeStyles.mediumCorner)
                    .background(color),
                contentAlignment = alignment,

                ) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    ) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
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
                            navController.navigate(Route.PlayVideo.createRoute(film.id))
                        }
                ) {
                    // Poster
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = film.horizontalPoster,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = timeFormatter(film.durationMinutes * 60 * 1000L),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(
                                Color.Black.copy(alpha = 0.6f),
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
                            film.watchDurationMinutes.toFloat() / film.durationMinutes.toFloat()
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress.coerceIn(0f, 1f))
                                .background(Color.Red)
                        )
                    }
                }

                // Thông tin phim bên cạnh (tuỳ chỉnh)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(min = 120.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                    ) {
                        Text(
                            text = film.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1
                        )
                        ExpandableText(film.description, 3, MaterialTheme.typography.bodySmall)
                    }
                    IconButton(
                        onClick = {
                            showActions = true
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(32.dp)
                    ) {
                        Icon(
                            AppIcons.option(),
                            contentDescription = null,
                        )
                    }
                }
                if (showActions) {
                    ActionBottomSheet(
                        filmId = film.id,
                        onDismiss = { showActions = false }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionBottomSheet(
    filmId: String,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionItem("Delete") { }
            ActionItem("Share") { }
        }
    }
}

@Composable
private fun ActionItem(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 24.dp)
    )
    HorizontalDivider()
}