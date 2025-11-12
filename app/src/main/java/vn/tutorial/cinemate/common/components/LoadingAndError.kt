package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import vn.tutorial.cinemate.R

@Composable
fun LoadingAndError(
    testTag: String = "api_message",
    isLoading: Boolean,
    error: String?,
    onErrorDismiss: () -> Unit
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (error != null && error != "") {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    modifier = Modifier
                        .semantics {
                            contentDescription = "ok_error_button"
                        },
                    onClick = {
                        onErrorDismiss()
                    }
                ) {
                    Text("OK")
                }
            },
            title = {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.error),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            text = {
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .testTag(testTag)
                            .semantics {
                                contentDescription = testTag
                            },
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        )
    }
}