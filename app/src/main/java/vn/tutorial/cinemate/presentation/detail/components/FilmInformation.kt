package vn.tutorial.cinemate.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.mockdata.filmMock1

@Composable
fun FilmInformation(
    film: FilmDetailModel,
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
                text = film.title, style = MaterialTheme.typography.titleSmall
            )


            Box(
                Modifier
                    .background(Color.Gray, Styles.ShapeStyles.smallCorner) // có shape
                    .padding(4.dp)
            ) {
                Text(
                    film.genres.joinToString(", "), style = MaterialTheme.typography.bodySmall
                )
            }

        }
        // todo metadata: year, country, quality, duration
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
//                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(film.year.toString(), style = MaterialTheme.typography.bodySmall)
                    Box(
                        Modifier
                            .background(Color.Gray, Styles.ShapeStyles.smallCorner)
                            .padding(4.dp)
                    ) {
                        Text(
                            film.country + "/" + film.age,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Text(film.quality, style = MaterialTheme.typography.bodySmall)
                    Text("${film.durationMinutes} min", style = MaterialTheme.typography.bodySmall)
                }

                // todo rating
                RatingBar(film.rating)
            }

            Box(
                Modifier
                    .background(Color.Red, Styles.ShapeStyles.mediumCorner)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${film.rank}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        // todo description
        ExpandableText(film.description)

        // todo actors
        Row {
            Text(
                "Actors: ${film.actors.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        // todo directors
        Row {
            Text(
                "Directors: ${film.directors.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}