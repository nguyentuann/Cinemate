package vn.tutorial.cinemate.presentation.authentication.screens

import android.annotation.SuppressLint
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
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AppBar
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.components.SignInText
import vn.tutorial.cinemate.core.util.Validator
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.authentication.components.EmailTextField
import vn.tutorial.cinemate.presentation.authentication.components.PasswordTextField
import vn.tutorial.cinemate.presentation.authentication.viewModel.SignUpViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreatePasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(
        navController.getBackStackEntry(Route.SignUpGraph.route)
    )
) {
    val state = viewModel.state.collectAsState().value
    Scaffold(
        topBar = {
            AppBar(onBack = {
                navController.popBackStack()
            }, actions = {
                SignInText(navController)
            })
        }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var isValidPassword: Boolean? by remember { mutableStateOf(null) }
            var isMatch: Boolean? by remember { mutableStateOf(null) }

            Text(
                text = stringResource(R.string.create_password),
                style = MaterialTheme.typography.titleLarge,
            )

            EmailTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.email,
                onValueChange = {},
                isValidEmail = true,
                readOnly = true
            )

            PasswordTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.password,
                onValueChange = {
                    viewModel.updatePassword(it)
                    isValidPassword = Validator.isValidPassword(it)
                },
                isValidPassword = isValidPassword,
                errorMessage = if (isValidPassword == false) stringResource(R.string.invalid_password) else null
            )

            PasswordTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.passwordConfirm,
                onValueChange = {
                    viewModel.updatePasswordConfirm(it)
                    isMatch = it == state.password
                },
                isValidPassword = isMatch,
                errorMessage = if (isMatch == false) stringResource(R.string.not_match_password) else null
            )

            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.confirm),
                onClick = {
                    if (isValidPassword == true && isMatch == true) {
                        viewModel.signUp {
                            navController.navigate(Route.Home.route) {
                                popUpTo(Route.SignUpGraph.route) {
                                    inclusive = true
                                }
                            }
                        }
                    } else {
                        if (isValidPassword != true) {
                            isValidPassword = false
                        }
                        if (isMatch != true) {
                            isMatch = false
                        }
                    }
                })
        }

        LoadingAndError(
            isLoading = state.isLoading,
            error = state.error,
            onErrorDismiss = { viewModel.clearError() })

    }
}