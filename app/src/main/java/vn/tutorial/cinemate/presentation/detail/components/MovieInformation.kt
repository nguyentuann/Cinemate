package vn.tutorial.cinemate.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.common.components.ExpandableText
import vn.tutorial.cinemate.common.components.RatingBar
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.core.util.getHighestQuality
import vn.tutorial.cinemate.domain.model.MovieDetailModel

@Composable
fun MovieInformation(
    movie: MovieDetailModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // todo title & genres
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = movie.title, style = MaterialTheme.typography.titleSmall
            )

            if (movie.category != null) {
                Box(
                    Modifier
                        .background(Color.Gray, Styles.ShapeStyles.smallCorner) // có shape
                        .padding(4.dp)
                ) {
                    Text(
                        movie.category.joinToString(", ") {
                            it.name
                        }, style = MaterialTheme.typography.bodySmall
                    )
                }
            }

        }
        // todo metadata: year, country, quality, duration
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MetadataMovie(movie.year.toString())
                    MetadataMovie(movie.country + "/" + movie.age + "+")
                    MetadataMovie(
                        getHighestQuality(movie.qualities),
                    )
                }

                if (movie.totalDuration != null) {
                    Text(
                        "${movie.totalDuration} s",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (movie.rank != null) {
                Box(
                    Modifier
                        .background(Color.Red, Styles.ShapeStyles.mediumCorner)
                        .size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#${movie.rank}",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // todo rating
        if (movie.rating != null) {
            RatingBar(movie.rating)
        }

        // todo description
        if (movie.description != null) {
            ExpandableText(movie.description)
        }

        // todo actors
        if (!movie.actors.isNullOrEmpty()) {
            Row {
                Text(
                    text = "Actors: ${
                        movie.actors.joinToString(", ") {
                            it.fullName.orEmpty()
                        }
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // todo directors
        if (!movie.directors.isNullOrEmpty()) {
            Row {
                Text(
                    text = "Directors: ${
                        movie.directors.joinToString(", ") {
                            it.fullName.orEmpty()
                        }
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun MetadataMovie(content: String) {
    Box(
        Modifier
            .background(Color.Gray, Styles.ShapeStyles.smallCorner)
            .padding(6.dp)
    ) {
        Text(content, style = MaterialTheme.typography.bodySmall)
    }
}