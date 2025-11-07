package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Text(
                text = confirmText,
                modifier = Modifier
                    .clickable(onClick = onConfirm)
                    .padding(horizontal = 16.dp)
            )
        },
        dismissButton = {
            Text(
                text = dismissText,
                modifier = Modifier
                    .clickable(onClick = onDismiss)
                    .padding(horizontal = 16.dp)
            )
        }
    )
}
