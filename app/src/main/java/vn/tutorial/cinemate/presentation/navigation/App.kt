package vn.tutorial.cinemate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import vn.tutorial.cinemate.presentation.authentication.screens.VerifyEmailScreen

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.SignIn.route) {
        authenticationNavGraph(navController)
    }
}