package vn.tutorial.cinemate.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import vn.tutorial.cinemate.common.components.BottomBar

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val navController = rememberNavController()
    val route = currentRoute(navController)

    val routeHasBottomBar = listOf(
        Route.Home.route,
        Route.ComingSoon.route,
        Route.Notification.route,
        Route.More.route,
    )

    Scaffold(
        bottomBar = {
            if (route in routeHasBottomBar) {
                BottomBar(navController = navController)
            }
        }
    ) {
        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(
                navController = navController,
                startDestination = Route.Started.route,
            ) {
                authenticationNavGraph(navController)
                mainNavGraph(navController, it)
                personalNavGraph(navController)
            }
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
