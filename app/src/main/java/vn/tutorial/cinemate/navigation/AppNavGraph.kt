package vn.tutorial.cinemate.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import vn.tutorial.cinemate.presentation.authentication.screens.ChangePasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.CheckMailScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SetupPasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignInScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignUpScreen
import vn.tutorial.cinemate.presentation.authentication.screens.VerifyEmailScreen
import vn.tutorial.cinemate.presentation.comingSoon.ComingSoonScreen
import vn.tutorial.cinemate.presentation.home.screens.HomeScreen
import vn.tutorial.cinemate.presentation.more.MoreScreen
import vn.tutorial.cinemate.presentation.notification.NotificationScreen
import vn.tutorial.cinemate.presentation.search.SearchScreen
import vn.tutorial.cinemate.presentation.splash.screens.SplashScreen

fun NavGraphBuilder.authenticationNavGraph(navController: NavHostController) {

    composable(route = Route.Splash.route) {
        SplashScreen(
            navController = navController
        )
    }

    composable(route = Route.SignIn.route) {
        SignInScreen(
            navController = navController
        )
    }

    composable(route = Route.SignUp.route) {
        SignUpScreen(
            navController = navController
        )
    }

    composable(route = Route.CheckMail.route) {
        CheckMailScreen("NhatTuan@gmail.com", navController)
    }

    composable(route = Route.CreatePassword.route) {
        SetupPasswordScreen(navController = navController)
    }

    composable(route = Route.VerifyEmail.route) {
        VerifyEmailScreen(navController = navController)
    }

    composable(route = Route.ChangePassword.route) {
        ChangePasswordScreen(navController = navController)
    }

    composable(
        route = Route.CreatePassword.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "https://myapp.com/verify"
            }
        )
    ) {
        SetupPasswordScreen(navController = navController)
    }
}

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    composable(Route.Home.route) { HomeScreen() }
    composable(Route.Search.route) { SearchScreen() }
    composable(Route.ComingSoon.route) { ComingSoonScreen() }
    composable(Route.Notification.route) { NotificationScreen() }
    composable(Route.More.route) { MoreScreen() }
}

