package vn.tutorial.cinemate.presentation.more.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AppBar
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.core.util.Validator
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.presentation.authentication.components.PasswordTextField
import vn.tutorial.cinemate.presentation.more.viewModels.ChangePasswordViewModel

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = {
            AppBar(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var oldPasswordValid: Boolean? by remember { mutableStateOf(null) }

            var isValidNewPassword: Boolean? by remember { mutableStateOf(null) }
            var newPasswordError by remember { mutableStateOf<String?>(null) }
            var isMatch: Boolean? by remember { mutableStateOf(null) }

            Text(
                text = stringResource(R.string.change_password),
                style = MaterialTheme.typography.titleLarge,
            )

            PasswordTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.oldPassword,
                onValueChange = {
                    viewModel.updateOldPasswordField(it)
                    oldPasswordValid = !it.isBlank()
                },
                isValidPassword = oldPasswordValid,
                errorMessage = if (oldPasswordValid == false) stringResource(R.string.password_empty) else null,
                testTag = "old_password_text_field",
                errorTestTag = "old_password_error_text"
            )

            PasswordTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.newPassword,
                onValueChange = {
                    viewModel.updateNewPasswordField(it)
                    newPasswordError = Validator.validatePassword(it)
                    isValidNewPassword = newPasswordError == null
                },
                isValidPassword = isValidNewPassword,
                errorMessage = newPasswordError ?: stringResource(R.string.password_empty),
                placeHolder = stringResource(R.string.new_password_placeholder),
                testTag = "new_password_text_field",
                errorTestTag = "new_password_error_text"
            )

            PasswordTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.confirmPassword,
                onValueChange = {
                    viewModel.updateConfirmPasswordField(it)
                    isMatch = it == state.newPassword
                },
                isValidPassword = isMatch,
                errorMessage = if (isMatch == false) stringResource(R.string.not_match_password) else null,
                placeHolder = stringResource(R.string.password_confirm_placeholder),
                testTag = "confirm_password_text_field",
                errorTestTag = "confirm_password_error_text"
            )

            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.change_password),
                onClick = {
                    if (isValidNewPassword == true && isMatch == true && oldPasswordValid == true) {
                        viewModel.updatePassword {
                            navController.popBackStack()
                        }
                    } else {
                        if (state.newPassword.isBlank()) {
                            isValidNewPassword = false
                        }
                        if (state.oldPassword.isBlank()) {
                            oldPasswordValid = false
                        }
                        if (isMatch!= true) {
                            isMatch = false
                        }
                    }
                },
                testTag = "update_password_button"
            )
        }
        LoadingAndError(
            testTag = "api_change_password_error",
            isLoading = state.isLoading,
            error = state.error,
            onErrorDismiss = {
                viewModel.clearError()
            }
        )
    }
}