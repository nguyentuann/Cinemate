package vn.tutorial.cinemate.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import vn.tutorial.cinemate.presentation.authentication.screens.ChangePasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.CheckMailScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SetupPasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignInScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignUpScreen
import vn.tutorial.cinemate.presentation.authentication.screens.VerifyEmailScreen
import vn.tutorial.cinemate.presentation.home.HomeScreen
import vn.tutorial.cinemate.presentation.settings.screens.ProfileScreen

fun NavGraphBuilder.authenticationNavGraph(navController: NavHostController) {
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


    composable(route = Route.Profile.route) {
        ProfileScreen()
    }



    composable(route = Route.Home.route) {
        HomeScreen()
    }
}