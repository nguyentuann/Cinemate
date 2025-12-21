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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AsyncImageWithReplace
import vn.tutorial.cinemate.common.components.ConfirmationDialog
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.core.constant.api_endpoint.BaseEndpoint
import vn.tutorial.cinemate.core.helper.getFullAvatarUrl
import vn.tutorial.cinemate.core.util.formatIsoDate
import vn.tutorial.cinemate.domain.model.ReviewModel
import vn.tutorial.cinemate.ui.theme.yellow
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewList(
    reviews: List<ReviewModel>,
    modifier: Modifier = Modifier,
    onClick: (String, String, String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(reviews) {
            ReviewItem(review = it, modifier = modifier, onClick = onClick)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewItem(
    review: ReviewModel,
    modifier: Modifier = Modifier,
    onClick: (String, String, String) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }


    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImageWithReplace(
                model = getFullAvatarUrl(review.userAvatar),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                imgReplace = R.drawable.adult,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                    Text(
                        review.userName
                            ?.substringBefore("@")
                            ?.ifBlank { "User" }
                            ?: "User",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        formatIsoDate(review.updateAt ?: LocalDate.now().toString()),
                        style = MaterialTheme.typography.bodySmall,
                    )
                Row {
                    repeat(5) {
                        val tint = if (it < review.stars) yellow else Color.Gray
                        Icon(
                            AppIcons.star(),
                            contentDescription = null,
                            tint = tint,
                            modifier = Modifier.size(16.dp),
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
            if (review.isUser) {
                Spacer(modifier = Modifier.size(16.dp))
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        showDialog.value = true
                    },
                ) {
                    Icon(
                        AppIcons.delete(),
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }

            if (showDialog.value) {
                ConfirmationDialog(
                    title = stringResource(R.string.delete),
                    message = stringResource(R.string.confirm_delete),
                    onConfirm = {
                        onClick(review.movieId, review.id!!, review.customerId!!)
                        showDialog.value = false

                    },
                    onDismiss = {
                        showDialog.value = false
                    }
                )
            }
        }
    }
}



