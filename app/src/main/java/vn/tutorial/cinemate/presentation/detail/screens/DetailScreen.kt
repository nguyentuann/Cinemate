package vn.tutorial.cinemate.presentation.detail.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.ExpandableText
import vn.tutorial.cinemate.common.components.InteractionButton
import vn.tutorial.cinemate.common.components.RatingBar
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.presentation.home.components.MovieSection
import vn.tutorial.cinemate.presentation.home.mock.bannerMovie
import vn.tutorial.cinemate.presentation.home.mock.sectionData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    movieId: Int,
    navController: NavHostController
) {

    val movie = bannerMovie
    val scrollState = rememberScrollState()
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // title
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    RatingBar(
                        rating = movie.rating
                    )
                }

                // button play
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {},
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

                // description
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                // overview
                ExpandableText(
                    text = movie.overview
                )
                // interaction bar

                HorizontalDivider()

                InteractionBar()
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(16.dp)
            )
            // recommend movies
            val moviesList = sectionData.map { (_, movies) -> movies }
            MovieSection(
                sectionTitle = "More Like This",
                movies = moviesList.flatten()
            )
        }
    }
}

@Composable
fun InteractionBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        InteractionButton(
            icon = R.drawable.ic_add,
            title = stringResource(R.string.my_list),
            onClick = {}
        )
        InteractionButton(
            icon = R.drawable.ic_rate,
            title = stringResource(R.string.rate),
            onClick = {}
        )
        InteractionButton(
            icon = R.drawable.ic_share,
            title = stringResource(R.string.share),
            onClick = {}
        )
    }
}