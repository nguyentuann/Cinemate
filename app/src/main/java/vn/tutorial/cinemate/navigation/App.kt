package vn.tutorial.cinemate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.Splash.route) {
        authenticationNavGraph(navController)
        mainNavGraph(navController)
    }

}