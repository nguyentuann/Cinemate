package vn.tutorial.cinemate.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.domain.model.Comment
import vn.tutorial.cinemate.ui.theme.yellow

@Composable
fun CommentList(
    comments: List<Comment>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 56.dp)
    ) {
        items(comments) {
            CommentItem(comment = it, modifier = modifier)
        }
    }

}

@Composable
fun CommentItem(comment: Comment, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                comment.userName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                comment.timestamp,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Row {
            repeat(5) {
                val tint = if (it < comment.stars) yellow else Color.Gray
                Icon(
                    AppIcons.star(),
                    contentDescription = null,
                    tint = tint
                )
            }
        }

        Text(
            comment.content,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Light
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}



