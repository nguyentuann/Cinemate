package vn.tutorial.cinemate.presentation.detail.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.detail.components.FilmInformation
import vn.tutorial.cinemate.presentation.detail.components.InteractionBar
import vn.tutorial.cinemate.presentation.detail.components.TrailerPlayer
import vn.tutorial.cinemate.presentation.home.components.FilmSection
import vn.tutorial.cinemate.mockdata.filmMock1
import vn.tutorial.cinemate.mockdata.sectionData
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.presentation.detail.components.Comment
import vn.tutorial.cinemate.presentation.detail.components.CommentBottomSheet

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    filmId: String,
) {
    val navController = LocalNavController.current
    val scrollState = rememberScrollState()
    var showComments by remember { mutableStateOf(false) }

    Scaffold {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState)
        ) {

            // todo trailer
            TrailerPlayer(filmMock1.trailerUrl)

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        navController.navigate(Route.PlayVideo.route)
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

                FilmInformation()

                HorizontalDivider()

                InteractionBar(
                    onComment = {
                        showComments = true
                    }
                )

                if (showComments) {
                    CommentBottomSheet(
                        comments = comments,
                        onDismiss = { showComments = false }
                    )
                }

                HorizontalDivider(
                    Modifier.padding(bottom = 16.dp)
                )

            }

            // recommend movies
            val moviesList = sectionData.map { (_, movies) -> movies }
            FilmSection(
                sectionTitle = "More Like This",
                films = moviesList.flatten(),
            )
        }
    }
}



val comments = listOf<Comment>(
    Comment(
        id = 1,
        author = "User1",
        rating = 4,
        content = "Great movie! Really enjoyed the plot and characters.",
        timestamp = "2 days ago"
    ),
    Comment(
        id = 2,
        author = "User2",
        rating = 5,
        content = "Amazing cinematography and soundtrack. A must-watch!",
        timestamp = "1 week ago"
    ),
    Comment(
        id = 3,
        author = "User3",
        rating = 3,
        content = "It was okay, but I felt the ending was a bit rushed.",
        timestamp = "3 days ago"
    ),
    Comment(
        id = 4,
        author = "User4",
        rating = 2,
        content = "Didn't live up to the hype. Found it quite boring.",
        timestamp = "5 days ago"
    ),
    Comment(
        id = 5,
        author = "User5",
        rating = 4,
        content = "Solid performances by the cast. Enjoyed it overall.",
        timestamp = "1 day ago"
    ),
)