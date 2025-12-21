package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.common.styles.Styles

@Composable
fun CommonTextField(
    value: String,
    testTag: String,
    errorTestTag: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit = {},
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    label: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .semantics {
                    contentDescription = testTag
                }
//                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            value = value,
            readOnly = readOnly,
            onValueChange = onValueChange,
            shape = Styles.ShapeStyles.smallCorner,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error
            ),
            isError = isError,
            singleLine = true,
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            label = if (label != null) {
                {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            } else null
        )
        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .semantics {
                        contentDescription = errorTestTag
                    }
                    .padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}