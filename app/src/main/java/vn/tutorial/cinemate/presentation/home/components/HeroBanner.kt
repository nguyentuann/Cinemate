package vn.tutorial.cinemate.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.RatingBar
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.presentation.home.mock.Movie

val headerItems = mapOf(
    "TV Shows" to {},
    "Movies" to {},
    "My List" to {}
)

@Composable
fun HeroBanner(
    modifier: Modifier = Modifier,
    movie: Movie,
    addToMyList: () -> Unit = { },
    play: () -> Unit = { }
) {
    var showInfoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(0.dp)
    ) {
        Box {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = null,
                modifier = Modifier.alpha(0.6f)
            )
            HeaderBar(
                headerItems = headerItems
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
            InteractionButton(
                modifier = Modifier.weight(3f),
                icon = R.drawable.ic_add,
                title = stringResource(R.string.my_list),
                onClick = addToMyList
            )
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(4f),
                onClick = play,
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
                icon = R.drawable.ic_info,
                title = stringResource(R.string.movie_info),
                onClick = {
                    showInfoDialog = true
                }
            )

            if (showInfoDialog) {
                ShowInfo(
                    movie = movie,
                    onDismiss = { showInfoDialog = false }
                )
            }
        }
    }
}

@Composable
private fun InteractionButton(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    onClick: () -> Unit = { }
) {
    Column(
        modifier = modifier.clickable(onClick = { onClick() }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ShowInfo(movie: Movie, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = movie.title,
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
                    text = movie.description,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))
                RatingBar(rating = movie.rating, starSize = 28.dp)
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

