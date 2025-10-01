package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LoadingAndError(
    isLoading: Boolean,
    error: String?,
    onErrorDismiss: () -> Unit
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (error != null) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    onErrorDismiss()
                }) {
                    Text("OK")
                }
            },
            title = {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Error",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            text = {
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        )
    }
}