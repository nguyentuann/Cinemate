package vn.tutorial.cinemate.presentation.authentication.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.AppBar
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.SignInText
import vn.tutorial.cinemate.core.util.underLineText
import vn.tutorial.cinemate.navigation.Route

@Composable
fun CheckMailScreen(
    email: String,
    navController: NavHostController
) {
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
                text = stringResource(R.string.complete_register),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = underLineText(
                    stringResource(R.string.almost_done, "%s"),
                    email,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.typography.bodyMedium.fontWeight!!
                ),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = stringResource(R.string.few_steps),
                style = MaterialTheme.typography.bodyMedium,
            )
            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                title = stringResource(R.string.forward_email),
                onClick = {
                    navController.navigate(Route.CreatePassword.route)
                }
            )
        }
    }
}

