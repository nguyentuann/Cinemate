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
import vn.tutorial.cinemate.core.helper.openMail
import vn.tutorial.cinemate.core.util.Validator
import vn.tutorial.cinemate.presentation.authentication.components.EmailTextField
import vn.tutorial.cinemate.navigation.Route
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VerifyEmailScreen(
    modifier: Modifier = Modifier,
    navController: NavController
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
                .padding(horizontal = 16.dp)
                .imePadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var email by remember { mutableStateOf("") }
            var isValidEmail: Boolean? by remember { mutableStateOf(null) }

            Text(
                modifier = Modifier.padding(bottom = 32.dp),
                text = stringResource(R.string.verify_email),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                modifier = Modifier.padding(bottom = 32.dp),
                text = stringResource(R.string.instruct_update_password),
                style = MaterialTheme.typography.bodyMedium,
            )
            EmailTextField(
                value = email,
                onValueChange = {
                    email = it
                    isValidEmail = Validator.isValidEmail(it)
                },
                isValidEmail = isValidEmail,
            )

            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.confirm),
                onClick = {
                    openMail(navController.context)
//                    if (isValidEmail == true) {
//                        navController.navigate(Route.ChangePassword.route)
//                    }
                }
            )
        }
    }
}