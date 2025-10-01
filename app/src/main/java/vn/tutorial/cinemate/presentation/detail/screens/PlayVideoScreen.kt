package vn.tutorial.cinemate.presentation.detail.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.presentation.detail.components.Comment
import vn.tutorial.cinemate.presentation.detail.components.CommentBottomSheet
import vn.tutorial.cinemate.presentation.detail.components.FilmInformation
import vn.tutorial.cinemate.presentation.detail.components.VideoPlayer

@Composable
fun PlayVideoScreen(movieId: String) {

    var showComments by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            VideoPlayer()
            HorizontalDivider()
            Row(
                Modifier.padding(16.dp),
            ) {
                FilmInformation()
            }

            HorizontalDivider()
            Text(
                text = stringResource(R.string.comment),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(
                        onClick = { showComments = true }
                    )
            )

            HorizontalDivider()

            if (showComments) {
                CommentBottomSheet(
                    comments = comments,
                    onDismiss = { showComments = false }
                )
            }
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