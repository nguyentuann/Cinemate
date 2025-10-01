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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AppBar
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.core.util.Validator
import vn.tutorial.cinemate.presentation.authentication.components.EmailTextField
import vn.tutorial.cinemate.presentation.authentication.components.PasswordTextField
import vn.tutorial.cinemate.navigation.Route

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
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
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var isValidEmail: Boolean? by remember { mutableStateOf(null) }

            EmailTextField(
                modifier = Modifier
                    .padding(top = 16.dp),
                value = email,
                onValueChange = {
                    email = it
                    isValidEmail = Validator.isValidEmail(it)
                },
                isValidEmail = isValidEmail,
            )

            PasswordTextField(
                modifier = Modifier
                    .padding(top = 16.dp),
                value = password,
                onValueChange = {
                    password = it
                },
            )

            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.sign_in),
                onClick = {
                    if (isValidEmail == true ) {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.SignIn.route) {
                                inclusive = true
                            }
                        }
                    }
                })

            Text(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(Route.VerifyEmail.route)
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
    }
}