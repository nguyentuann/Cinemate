package vn.tutorial.cinemate.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import vn.tutorial.cinemate.presentation.authentication.screens.ChangePasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.CheckMailScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SetupPasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignInScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignUpScreen
import vn.tutorial.cinemate.presentation.authentication.screens.VerifyEmailScreen
import vn.tutorial.cinemate.presentation.authentication.screens.VerifyOTPScreen
import vn.tutorial.cinemate.presentation.authentication.viewModel.ForgotPasswordViewModel
import vn.tutorial.cinemate.presentation.coming_soon.ComingSoonScreen
import vn.tutorial.cinemate.presentation.detail.screens.DetailScreen
import vn.tutorial.cinemate.presentation.detail.screens.PlayVideoScreen
import vn.tutorial.cinemate.presentation.home.screens.HomeScreen
import vn.tutorial.cinemate.presentation.more.screens.FavoriteScreen
import vn.tutorial.cinemate.presentation.more.screens.MoreScreen
import vn.tutorial.cinemate.presentation.notification.NotificationScreen
import vn.tutorial.cinemate.presentation.search.SearchScreen
import vn.tutorial.cinemate.presentation.splash.screens.SplashScreen

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.authenticationNavGraph(navController: NavHostController) {

    composable(route = Route.Splash.route) {
        SplashScreen(navController = navController)
    }

    composable(route = Route.SignIn.route) {
        SignInScreen(navController = navController)
    }

    composable(route = Route.SignUp.route) {
        SignUpScreen(navController = navController)
    }

    composable(
        route = Route.CheckMail.route,
        arguments = listOf(navArgument("email") { type = NavType.StringType })
    ) { backStackEntry ->
        val email = backStackEntry.arguments?.getString("email") ?: return@composable
        CheckMailScreen(email)
    }

    composable(route = Route.CreatePassword.route) {
        SetupPasswordScreen(navController = navController)
    }

    composable(Route.VerifyOTP.route) { VerifyOTPScreen() }

    // 👇 gom các màn forgot password thành 1 graph con
    navigation(
        startDestination = Route.VerifyEmail.route,
        route = "forgot_password_graph"
    ) {
        composable(route = Route.VerifyEmail.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry("forgot_password_graph")
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            VerifyEmailScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable(route = Route.ChangePassword.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry("forgot_password_graph")
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            ChangePasswordScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
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

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    composable(Route.Home.route) {
        HomeScreen(
            innerPadding = innerPadding,
            navController = navController
        )
    }
    composable(Route.Search.route) { SearchScreen() }
    composable(Route.ComingSoon.route) { ComingSoonScreen() }
    composable(Route.Notification.route) { NotificationScreen() }
    composable(Route.More.route) { MoreScreen() }


    composable(
        Route.Detail.route,
        arguments = listOf(navArgument("filmId") { type = NavType.StringType })
    ) { backStackEntry ->
        val filmId = backStackEntry.arguments?.getString("filmId") ?: return@composable
        DetailScreen(
            filmId = filmId,
        )
    }

    composable(
        Route.PlayVideo.route,
        arguments = listOf(navArgument("filmId") { type = NavType.StringType })
    ) { backStackEntry ->
        val filmId = backStackEntry.arguments?.getString("filmId") ?: return@composable
        PlayVideoScreen(filmId)
    }
}

fun NavGraphBuilder.personalNavGraph(navController: NavHostController) {
    composable(Route.Favorite.route) {
        FavoriteScreen()
    }
}

