package vn.tutorial.cinemate.navigation

import FavoriteScreen
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.presentation.authentication.screens.CheckMailScreen
import vn.tutorial.cinemate.presentation.authentication.screens.CreatePasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.ForgotPasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignInScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignUpScreen
import vn.tutorial.cinemate.presentation.authentication.screens.UpdatePasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.VerifyOTPScreen
import vn.tutorial.cinemate.presentation.authentication.screens.VerifyTokenScreen
import vn.tutorial.cinemate.presentation.authentication.viewModel.ForgotPasswordViewModel
import vn.tutorial.cinemate.presentation.authentication.viewModel.SignUpViewModel
import vn.tutorial.cinemate.presentation.coming_soon.ComingSoonScreen
import vn.tutorial.cinemate.presentation.detail.screens.DetailScreen
import vn.tutorial.cinemate.presentation.detail.screens.PlayVideoScreen
import vn.tutorial.cinemate.presentation.home.screens.HomeScreen
import vn.tutorial.cinemate.presentation.more.screens.ChangePasswordScreen
import vn.tutorial.cinemate.presentation.more.screens.HistoryScreen
import vn.tutorial.cinemate.presentation.more.screens.MoreScreen
import vn.tutorial.cinemate.presentation.more.screens.PersonalInformationScreen
import vn.tutorial.cinemate.presentation.more.screens.SettingNotificationScreen
import vn.tutorial.cinemate.presentation.more.screens.ThemeAndLanguageScreen
import vn.tutorial.cinemate.presentation.more.viewModels.SettingsViewModel
import vn.tutorial.cinemate.presentation.notification.NotificationScreen
import vn.tutorial.cinemate.presentation.search.SearchScreen
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

//    composable(
//        Route.VerifyToken.route,
//        arguments = listOf(navArgument("token") { type = NavType.StringType }),
////            deepLinks = listOf(
////                navDeepLink {
////                    uriPattern = "http://localhost:3000/register/confirm?token={token}"
////                    action = Intent.ACTION_VIEW
////                }
////            )
//    ) { backStackEntry ->
//        val parentEntry = remember(navController) {
//            navController.getBackStackEntry(Route.SignUpGraph.route)
//        }
//        val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
//        val token = backStackEntry.arguments?.getString("token") ?: return@composable
//        LogUtil("Deep link token: $token")
//        VerifyTokenScreen(token, navController, viewModel)
//    }

    // todo gom các màn forgot password thành 1 graph con
    navigation(
        startDestination = Route.ForgotPassword.route,
        route = Route.ForgotPasswordGraph.route
    ) {
        composable(route = Route.ForgotPassword.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.ForgotPasswordGraph.route)
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            ForgotPasswordScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(route = Route.VerifyOTP.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.ForgotPasswordGraph.route)
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            VerifyOTPScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable(route = Route.UpdatePassword.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.ForgotPasswordGraph.route)
            }
            val viewModel: ForgotPasswordViewModel = hiltViewModel(parentEntry)

            UpdatePasswordScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    // todo sign up graph
    navigation(
        startDestination = Route.SignUp.route,
        route = Route.SignUpGraph.route
    ) {

        composable(route = Route.SignUp.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.SignUpGraph.route)
            }
            val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
            SignUpScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = Route.CreatePassword.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.SignUpGraph.route)
            }
            val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
            CreatePasswordScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            Route.VerifyToken.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType }),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "http://localhost:3000/register/confirm?token={token}"
                    action = Intent.ACTION_VIEW
                }
            )
        ) { backStackEntry ->
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Route.SignUpGraph.route)
            }
            val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
            val token = backStackEntry.arguments?.getString("token") ?: return@composable
            LogUtil("Deep link token: $token")
            VerifyTokenScreen(token, navController, viewModel)
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.mainNavGraph(
    innerPadding: PaddingValues,
) {
    composable(Route.Home.route) {
        HomeScreen(
            innerPadding = innerPadding,
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
            movieId = filmId,
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

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.personalNavGraph(
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

    composable(Route.Profile.route) {
        PersonalInformationScreen()
    }

    composable(Route.SettingNotification.route) {
        SettingNotificationScreen()
    }

    composable(Route.ChangePassword.route) {
        ChangePasswordScreen()
    }
}

