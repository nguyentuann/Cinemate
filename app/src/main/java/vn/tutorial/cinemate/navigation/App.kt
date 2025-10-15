package vn.tutorial.cinemate.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import vn.tutorial.cinemate.presentation.more.viewModels.SettingsViewModel

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(
    settingViewModel: SettingsViewModel,
    navController: NavHostController
) {
    val route = currentRoute(navController)

    val routeHasBottomBar = listOf(
        Route.Home.route,
        Route.ComingSoon.route,
        Route.Notification.route,
        Route.Search.route,
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
                startDestination = Route.SignUpGraph.route,
            ) {
                authenticationNavGraph(navController)
                mainNavGraph(navController, it)
                personalNavGraph(navController, settingViewModel)
            }
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
