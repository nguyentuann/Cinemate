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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .semantics {
                    contentDescription = testTag
                }
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
//                .height(56.dp),
            value = value,
            readOnly = readOnly,
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
//                    textAlign = TextAlign.Start
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
                modifier = Modifier
                    .semantics {
                        contentDescription = errorTestTag
                    }
                    .padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}