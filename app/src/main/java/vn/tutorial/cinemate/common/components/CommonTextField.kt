package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.common.styles.Styles

@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit = {},
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .height(56.dp),
            value = value,
            onValueChange = onValueChange,
            shape = Styles.ShapeStyles.smallCorner,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                errorBorderColor = MaterialTheme.colorScheme.error
            ),
            isError = isError,
            singleLine = true,
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,

            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,

            )
        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}