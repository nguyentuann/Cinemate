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
import androidx.compose.runtime.LaunchedEffect
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
import vn.tutorial.cinemate.common.components.CommonTextField
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.components.SignInText
import vn.tutorial.cinemate.core.util.Validator
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.authentication.components.PasswordTextField
import vn.tutorial.cinemate.presentation.authentication.viewModel.SignUpViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetupPasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(
        navController.getBackStackEntry("forgot_password_graph")
    )
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value.user) {
        if (state.value.user != null) {
            navController.navigate(Route.CheckMail.createRoute(email = state.value.email)) {
                popUpTo(Route.SignUp.route) {
                    inclusive = true
                }
            }
        }
    }

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
            var isFirstNameValid: Boolean? by remember { mutableStateOf(null) }
            var isLastNameValid: Boolean? by remember { mutableStateOf(null) }

            Text(
                text = stringResource(R.string.account_info),
                style = MaterialTheme.typography.titleLarge,
            )

            CommonTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.value.firstName,
                onValueChange = {
                    viewModel.updateFirstName(it)
                    isFirstNameValid = it.isNotEmpty()
                },
                isError = isFirstNameValid == false,
                placeholder = stringResource(R.string.first_name),
                errorMessage = if (isFirstNameValid == false) stringResource(R.string.not_empty) else null,
            )

            CommonTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.value.lastName,
                onValueChange = {
                    viewModel.updateLastName(it)
                    isLastNameValid = it.isNotEmpty()
                },
                isError = isLastNameValid == false,
                placeholder = stringResource(R.string.last_name),
                errorMessage = if (isFirstNameValid == false) stringResource(R.string.not_empty) else null,
            )


            PasswordTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.value.password,
                onValueChange = {
                    viewModel.updatePassword(it)
                    isValidPassword = Validator.isValidPassword(it)
                },
                isValidPassword = isValidPassword,
                errorMessage = if (isValidPassword == false) stringResource(R.string.invalid_password) else null
            )

            PasswordTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.value.passwordConfirm,
                onValueChange = {
                    viewModel.updatePasswordConfirm(it)
                    isMatch = it == state.value.password
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
                    if (isValidPassword == true && isMatch == true && isFirstNameValid == true && isLastNameValid == true) {
                        viewModel.signUp()
                    } else {
                        if (isValidPassword != true) {
                            isValidPassword = false
                        }
                        if (isMatch != true) {
                            isMatch = false
                        }

                        isFirstNameValid = state.value.firstName.isNotEmpty()
                        isLastNameValid = state.value.lastName.isNotEmpty()

                    }
                })
        }

        LoadingAndError(
            isLoading = state.value.isLoading,
            error = state.value.error,
            onErrorDismiss = { viewModel.clearError() })

    }
}