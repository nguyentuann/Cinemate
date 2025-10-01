package vn.tutorial.cinemate.presentation.authentication.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AppBar
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.LoadingAndError
import vn.tutorial.cinemate.common.components.SignInText
import vn.tutorial.cinemate.navigation.LocalNavController
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.authentication.viewModel.SignUpViewModel

@Composable
fun VerifyOTPScreen(
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val state = viewModel.state.collectAsState()
    val focusRequesters = remember { List(state.value.otp.size) { FocusRequester() } }
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
                .padding(it)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 32.dp),
                text = stringResource(R.string.verify_otp),
                style = MaterialTheme.typography.titleLarge,
            )

            // 4 ô verity OTP code

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                state.value.otp.forEachIndexed { index, digit ->
                    OutlinedTextField(
                        value = if (digit == -1) "" else digit.toString(),
                        onValueChange = { value ->
                            if (value.length <= 1 && value.all { it.isDigit() }) {
                                viewModel.updateOTP(value, index)

                                if (value.isNotEmpty() && index < focusRequesters.lastIndex) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .width(48.dp)
                            .height(56.dp)
                            .focusRequester(focusRequesters[index]),
                        textStyle = MaterialTheme.typography.titleSmall.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            CommonButton(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(),
                title = stringResource(R.string.confirm),
                onClick = {

                    if (!state.value.otp.contains(-1)) {
                        Log.d("SignUp", "onClick: ${viewModel.getOtpCode()}")
                        viewModel.verifyOTP(
                            otp = viewModel.getOtpCode(),
                            onSuccess = {
                                navController.navigate(Route.SignIn.route) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                }
            )
        }

        LoadingAndError(
            isLoading = state.value.isLoading,
            error = state.value.error,
            onErrorDismiss = {
                viewModel.clearError()
            }
        )
    }
}