package vn.tutorial.cinemate.presentation.detail.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.presentation.detail.viewModels.CommentViewModel
import vn.tutorial.cinemate.ui.theme.yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    filmId: String,
    onDismiss: () -> Unit,
    commentViewModel: CommentViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        commentViewModel.getAllComments(filmId)
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
                text = stringResource(R.string.comment_rating),
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
                CommentList(state.comments) // LazyColumn cuộn độc lập
            }
                Review(
                    onSendReview = { rating, content ->
                        commentViewModel.addComment(filmId, rating, content)
                    }
                )
        }
    }
}

@Composable
private fun Review(
    onSendReview: (Int, String) -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var content by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp).imePadding()
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
                        modifier = Modifier.size(26.dp).weight(1f)
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
                )
            )

            // Nút gửi
            IconButton(
                onClick = {
                    onSendReview(rating, content)
                },
                enabled = content.isNotEmpty() && rating > 0) {
                Icon(
                    AppIcons.send(),
                    contentDescription = null,
                )
            }
        }
    }
}

