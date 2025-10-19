package vn.tutorial.cinemate.presentation.authentication.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.CommonTextField

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isValidEmail: Boolean?,
    readOnly: Boolean = false
) {

    CommonTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = stringResource(R.string.email_placeholder),
        isError = (isValidEmail == false),
        errorMessage = if (isValidEmail == false) stringResource(R.string.invalid_email) else null,
        readOnly = readOnly
    )
}