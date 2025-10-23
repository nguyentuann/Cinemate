package vn.tutorial.cinemate.presentation.authentication.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
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
import vn.tutorial.cinemate.presentation.authentication.viewModel.SignUpViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(
        navController.getBackStackEntry(Route.SignUpGraph.route)
    )
) {
    val state = viewModel.state.collectAsState().value
    var isChecked by remember { mutableStateOf(false) }
    var isValidEmail: Boolean? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        val token = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("verify_token_pending")

        if (!token.isNullOrEmpty()) {
            navController.navigate(Route.VerifyToken.createRoute(token))
        }
    }


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
                .padding(horizontal = 16.dp).padding(top = 32.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = stringResource(R.string.sign_up),
                style = MaterialTheme.typography.titleLarge,
            )

            EmailTextField(
                modifier = Modifier
                    .padding(top = 16.dp),
                value = state.email,
                onValueChange = {
                    viewModel.updateEmail(it)
                    isValidEmail = Validator.isValidEmail(it)
                },
                isValidEmail = isValidEmail,
            )

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                )

                Text(
                    text = stringResource(R.string.term),
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                )
            }

            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.get_started),
                onClick = {
                    if (isValidEmail == true && isChecked) {
                        viewModel.verifyEmailSignUp {
                            navController.navigate(
                                Route.CheckMail.createRoute(
                                    email = state.email
                                )
                            )
                        }
                    } else {
                        isValidEmail = false
                    }
                }
            )

            Text(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(Route.SignIn.route) {
                                popUpTo(Route.SignUp.route) {
                                    inclusive = true
                                }
                            }
                        }
                    ),
                text = stringResource(R.string.have_account),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                )
            )
        }
        LoadingAndError(
            isLoading = state.isLoading,
            error = state.error,
            onErrorDismiss = {
                viewModel.clearError()
            }
        )
    }
}