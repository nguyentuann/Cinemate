package vn.tutorial.cinemate.presentation.detail.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AsyncImageWithReplace
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.core.util.formatIsoDate
import vn.tutorial.cinemate.domain.model.ReviewModel
import vn.tutorial.cinemate.ui.theme.yellow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewList(
    reviews: List<ReviewModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(reviews) {
            ReviewItem(review = it, modifier = modifier)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewItem(review: ReviewModel, modifier: Modifier = Modifier) {
    Card (
        modifier = Modifier.padding(8.dp)
    ){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImageWithReplace(
                model = review.userAvatar,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                imgReplace = R.drawable.avatar,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        review.userName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        formatIsoDate(review.updateAt!!),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Row {
                    repeat(5) {
                        val tint = if (it < review.stars) yellow else Color.Gray
                        Icon(
                            AppIcons.star(),
                            contentDescription = null,
                            tint = tint
                        )
                    }
                }

                Text(
                    review.content,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}



