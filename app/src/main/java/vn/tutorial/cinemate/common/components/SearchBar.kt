package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.icons.AppIcons

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onChange: (String) -> Unit = { _ -> },
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onChange(it)
        },
        leadingIcon = {
            Icon(
                AppIcons.search(),
                contentDescription = null
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.search_placeholder),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Light
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.5f
                ),
                textAlign = TextAlign.Center
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}