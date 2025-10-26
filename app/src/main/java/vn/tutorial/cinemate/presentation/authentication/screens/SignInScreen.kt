package vn.tutorial.cinemate.presentation.authentication.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AppBar
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.core.util.Validator
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.authentication.components.EmailTextField
import vn.tutorial.cinemate.presentation.authentication.components.PasswordTextField
import vn.tutorial.cinemate.presentation.authentication.viewModel.SignInViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    var isValidEmail: Boolean? by remember { mutableStateOf(null) }
    var isValidPassword: Boolean? by remember { mutableStateOf(null) }

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
                .padding(16.dp)
                .padding(top = 32.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 32.dp),
                text = stringResource(R.string.sign_in),
                style = MaterialTheme.typography.titleLarge,
            )

            EmailTextField(
                modifier = Modifier
                    .padding(top = 16.dp),
                value = state.email,
                testTag = "sign_in_email_text_field",
                onValueChange = {
                    viewModel.updateEmail(it)
                    isValidEmail = Validator.isValidEmail(it)
                },
                isValidEmail = isValidEmail,
            )

            PasswordTextField(
                modifier = Modifier
                    .padding(top = 16.dp),
                value = state.password,
                testTag = "sign_in_password_text_field",
                onValueChange = {
                    viewModel.updatePassword(it)
                    isValidPassword = it.isNotEmpty()
                },
                isValidPassword = isValidPassword,
                errorMessage = stringResource(R.string.password_empty)
            )

            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.sign_in),
                testTag = "sign_in_button",
                onClick = {
                    if (isValidEmail == true && isValidPassword == true) {
                        viewModel.signIn(
                            onSuccess = {
                                navController.navigate(Route.Home.route) {
                                    popUpTo(0)
                                    launchSingleTop = true
                                }
                            }
                        )
                    } else {
                        if (isValidEmail != true) {
                            isValidEmail = false
                        }
                        if (isValidPassword != true) {
                            isValidPassword = false
                        }
                    }
                })

            Text(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(Route.ForgotPassword.route)
                        }
                    ),
                text = stringResource(R.string.forgot_password),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                )
            )

            Text(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(Route.SignUp.route) {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }
                    ),
                text = stringResource(R.string.register_account),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                )
            )
        }
        LoadingAndError(
            testTag = "sign_in_api_message",
            isLoading = state.isLoading,
            error = state.error,
            onErrorDismiss = {
                viewModel.clearError()
            }
        )
    }
}