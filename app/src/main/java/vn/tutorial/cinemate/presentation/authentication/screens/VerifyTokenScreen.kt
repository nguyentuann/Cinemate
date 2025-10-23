package vn.tutorial.cinemate.presentation.authentication.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.authentication.viewModel.SignUpViewModel

@Composable
fun VerifyTokenScreen(
    token: String,
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(
        navController.getBackStackEntry(Route.SignUpGraph.route)
    )
) {
    val state = viewModel.state.collectAsState().value
    Scaffold(
        topBar = {
            AppBar(
                onBack = {
                    navController.popBackStack()
                },
                actions = {
                    SignInText(navController)
                }
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                modifier = Modifier.padding(vertical = 32.dp),
                text = stringResource(R.string.success_verify_email),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(R.string.create_password_info),
                style = MaterialTheme.typography.bodyMedium,
            )
            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.create_password),
                onClick = {
                    viewModel.verifyToken(token) {
                        navController.navigate(Route.CreatePassword.route) {
                            popUpTo(Route.SignUpGraph.route) {
                                inclusive = false
                            }
                        }
                    }
                }
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