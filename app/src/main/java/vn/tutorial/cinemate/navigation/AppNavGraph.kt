package vn.tutorial.cinemate.navigation

import FavoriteScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import vn.tutorial.cinemate.presentation.authentication.screens.ChangePasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.CheckMailScreen
import vn.tutorial.cinemate.presentation.authentication.screens.CreatePasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignInScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignUpScreen
import vn.tutorial.cinemate.presentation.authentication.screens.VerifyEmailScreen
import vn.tutorial.cinemate.presentation.authentication.screens.VerifyOTPScreen
import vn.tutorial.cinemate.presentation.authentication.viewModel.ForgotPasswordViewModel
import vn.tutorial.cinemate.presentation.authentication.viewModel.SignUpViewModel
import vn.tutorial.cinemate.presentation.coming_soon.ComingSoonScreen
import vn.tutorial.cinemate.presentation.detail.screens.DetailScreen
import vn.tutorial.cinemate.presentation.detail.screens.PlayVideoScreen
import vn.tutorial.cinemate.presentation.detail.viewModels.PlayVideoViewModel
import vn.tutorial.cinemate.presentation.home.screens.HomeScreen
import vn.tutorial.cinemate.presentation.more.screens.HistoryScreen
import vn.tutorial.cinemate.presentation.more.screens.MoreScreen
import vn.tutorial.cinemate.presentation.notification.NotificationScreen
import vn.tutorial.cinemate.presentation.search.SearchScreen
import vn.tutorial.cinemate.presentation.settings.screens.ThemeAndLanguageScreen
import vn.tutorial.cinemate.presentation.settings.viewModel.SettingsViewModel
import vn.tutorial.cinemate.presentation.splash.screens.SplashScreen
import vn.tutorial.cinemate.presentation.splash.screens.StartedScreen

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.authenticationNavGraph(navController: NavHostController) {

    composable(route = Route.Started.route) {
        StartedScreen(
            onNavigateHome = {
                navController.navigate(Route.Home.route) {
                    popUpTo(Route.Started.route) {
                        inclusive = true
                    }
                }
            },
            onNavigateAuth = {
                navController.navigate(Route.Splash.route) {
                    popUpTo(Route.Started.route) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable(route = Route.Splash.route) {
        SplashScreen(navController = navController)
    }

    composable(route = Route.SignIn.route) {
        SignInScreen(navController = navController)
    }

    composable(
        route = Route.CheckMail.route,
        arguments = listOf(navArgument("email") { type = NavType.StringType })
    ) { backStackEntry ->
        val email = backStackEntry.arguments?.getString("email") ?: return@composable
        CheckMailScreen(email)
    }

    composable(Route.VerifyOTP.route) { VerifyOTPScreen() }

    // todo gom các màn forgot password thành 1 graph con
    navigation(
        startDestination = Route.VerifyEmail.route,
        route = Route.ForgotPasswordGraph.route
    ) {
        composable(route = Route.VerifyEmail.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.ForgotPasswordGraph.route)
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            VerifyEmailScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable(route = Route.ChangePassword.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.ForgotPasswordGraph.route)
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            ChangePasswordScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    // todo sign up graph
    navigation(
        startDestination = Route.SignUp.route,
        route = Route.SignInGraph.route
    ) {
        composable(route = Route.SignUp.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.SignInGraph.route)
            }
            val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
            SignUpScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = Route.CreatePassword.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.SignInGraph.route)
            }
            val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
            CreatePasswordScreen(navController = navController, viewModel = viewModel)
        }
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

fun NavGraphBuilder.personalNavGraph(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    composable(Route.Favorite.route) {
        FavoriteScreen()
    }

    composable(Route.History.route) {
        HistoryScreen()
    }

    composable(Route.ThemeAndLanguage.route) {
        ThemeAndLanguageScreen(settingsViewModel)
    }
}

