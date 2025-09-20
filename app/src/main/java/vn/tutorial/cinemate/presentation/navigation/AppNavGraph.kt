package vn.tutorial.cinemate.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import vn.tutorial.cinemate.presentation.authentication.screens.CheckMailScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SetupPasswordScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignInScreen
import vn.tutorial.cinemate.presentation.authentication.screens.SignUpScreen

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

    composable(route = Route.ForgetPassword.route) {

    }
}