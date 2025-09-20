package vn.tutorial.cinemate.presentation.authentication.screens

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
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AppBar
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.CommonTextField
import vn.tutorial.cinemate.presentation.navigation.Route

@Composable
fun ChangePasswordScreen(
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
                .padding(it)
                .padding(horizontal = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var isError by remember { mutableStateOf(false) }

            Text(
                modifier = Modifier.padding(bottom = 32.dp),
                text = stringResource(R.string.change_password),
                style = MaterialTheme.typography.titleLarge,
            )

            CommonTextField(
                modifier = Modifier.padding(bottom = 16.dp),
                value = password,
                onValueChange = { password = it },
                placeholder = stringResource(R.string.password_placeholder),
            )
            CommonTextField(
                modifier = Modifier.padding(bottom = 16.dp),
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    isError = it != password
                },
                isError = isError,
                placeholder = stringResource(R.string.password_confirm_placeholder),
                errorMessage = "Password does not match",
            )

            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.change_password),
                onClick = {
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.ChangePassword.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
