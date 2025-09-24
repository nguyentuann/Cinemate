package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.navigation.Route

@Composable
fun SignInText(
    navController: NavHostController
) {
    Text(
        text = stringResource(R.string.sign_in),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(end = 16.dp)
            .clickable {
                navController.navigate(Route.SignIn.route) {
                    popUpTo(Route.SignIn.route) { inclusive = true }
                }
            }
    )

}