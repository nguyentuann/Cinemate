package vn.tutorial.cinemate.presentation.authentication.screens

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AppBar
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.core.util.Validator
import vn.tutorial.cinemate.presentation.authentication.components.PasswordTextField
import vn.tutorial.cinemate.presentation.navigation.Route

@Composable
fun SetupPasswordScreen(
    modifier: Modifier = Modifier, navController: NavController
) {

    Scaffold(
        topBar = {
            AppBar(onBack = {
                navController.popBackStack()
            }, actions = {
                Text(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable(
                            onClick = {
                                navController.navigate(Route.SignIn.route) {
                                    popUpTo(Route.SignIn.route) {
                                        inclusive = true
                                    }
                                }
                            }),
                    text = stringResource(R.string.sign_in),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            })
        }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var isValidPassword: Boolean? by remember { mutableStateOf(null) }
            var isMatch: Boolean? by remember { mutableStateOf(null) }

            Text(
                text = stringResource(R.string.create_password),
                style = MaterialTheme.typography.titleLarge,
            )

            PasswordTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = password,
                onValueChange = {
                    password = it
                    isValidPassword = Validator.isValidPassword(it)
                },
                isValidPassword = isValidPassword,
                errorMessage = if (isValidPassword == false) stringResource(R.string.invalid_password) else null
            )

            PasswordTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    isMatch = it == password
                },
                isValidPassword = isMatch,
                errorMessage = if (isValidPassword == false) stringResource(R.string.not_match_password) else null
            )

            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.confirm),
                onClick = {
                    if (isValidPassword == true && isMatch == true) {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.CreatePassword.route) {
                                inclusive = true
                            }
                        }
                    }
                })
        }
    }
}