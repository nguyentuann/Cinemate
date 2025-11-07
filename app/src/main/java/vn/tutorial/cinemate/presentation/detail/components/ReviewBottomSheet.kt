package vn.tutorial.cinemate.presentation.detail.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.ReviewModel
import vn.tutorial.cinemate.presentation.detail.viewModels.ReviewViewModel
import vn.tutorial.cinemate.ui.theme.yellow

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    movieId: String,
    onDismiss: () -> Unit,
    commentViewModel: ReviewViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        commentViewModel.getAllReviews(movieId)
        commentViewModel.getReviewCount(movieId)
    }

    val state = commentViewModel.state.collectAsState().value

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )


    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        // Nội dung trong sheet
        Column(
            Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.reviews) + " (${state.reviewCount})",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            HorizontalDivider()
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ReviewList(state.reviews, onClick = { movieId, reviewId, customerId ->
                    commentViewModel.deleteComment(movieId, reviewId, customerId)
                }
                ) // LazyColumn cuộn độc lập
            }
            Review(
                onSendReview = { rating, content ->
                    commentViewModel.addReview(
                        ReviewModel(
                            movieId = movieId,
                            customerId = "current_user_id",
                            userName = "user_name",
                            userAvatar = "user_avatar",
                            stars = rating,
                            content = content,
                        )
                    )
                    LogUtil("Send review: rating=$rating, content=$content")

                }
            )
        }

        LoadingAndError(
            isLoading = state.loading,
            error = state.error,
            onErrorDismiss = {
                commentViewModel.clearError()
            }
        )
    }
}

@Composable
private fun Review(
    onSendReview: (Int, String) -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var content by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .imePadding()
    ) {
        // 5 ngôi sao
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            (1..5).forEach { index ->
                IconButton(
                    onClick = { rating = index },
                ) {
                    Icon(
                        AppIcons.star(),
                        contentDescription = null,
                        tint = if (index <= rating) yellow else Color.Gray,
                        modifier = Modifier
                            .size(26.dp)
                            .weight(1f)
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        Row {
            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                },
                placeholder = {
                    Text(
                        stringResource(R.string.add_comment),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                    focusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            // Nút gửi
            IconButton(
                onClick = {
                    onSendReview(rating, content)
                    content = ""

                    // Ẩn bàn phím
                    keyboardController?.hide()
                },
                enabled = content.isNotEmpty() && rating > 0
            ) {
                Icon(
                    AppIcons.send(),
                    contentDescription = null,
                )
            }
        }
    }
}

