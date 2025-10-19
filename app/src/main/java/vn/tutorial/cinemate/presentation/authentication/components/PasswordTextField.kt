package vn.tutorial.cinemate.presentation.authentication.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.CommonTextField

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isValidPassword: Boolean? = null,
    errorMessage: String? = null,
    placeHolder: String = stringResource(R.string.password_placeholder)
) {
    var passwordVisible by remember { mutableStateOf(false) }

    CommonTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeHolder,
        isError = (isValidPassword == false),
        errorMessage = errorMessage,

        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible) {
                ImageVector.vectorResource(id = R.drawable.show_password)
            } else {
                ImageVector.vectorResource(id = R.drawable.hide_password)
            }

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = image,
                    contentDescription = null
                )
            }
        }
    )
}
