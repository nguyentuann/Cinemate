package vn.tutorial.cinemate.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.InteractionButton
import vn.tutorial.cinemate.common.components.RatingBar
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route

@Composable
fun HeroBannerInteractionBar(
    modifier: Modifier = Modifier,
    film: FilmDetailModel
) {
    val navController = LocalNavController.current
    var showInfo = remember { mutableStateOf(false) }
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
            onClick = {}
        )
        Button(
            modifier = Modifier
                .fillMaxHeight()
                .weight(4f),
            onClick = {
                navController.navigate(Route.PlayVideo.createRoute(film.id))
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
                showInfo.value = true
            }
        )

        if (showInfo.value) {
            ShowFilmInfo(film) {
                showInfo.value = false
            }
        }
    }
}

@Composable
private fun ShowFilmInfo(film: FilmDetailModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = film.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = film.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))
                RatingBar(rating = film.rating, starSize = 28.dp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Close",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        shape = Styles.ShapeStyles.largeCorner,
    )
}
