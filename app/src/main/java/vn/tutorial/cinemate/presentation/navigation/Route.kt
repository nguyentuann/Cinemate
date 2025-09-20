package vn.tutorial.cinemate.presentation.navigation

sealed class Route(val route: String) {
    object SignIn: Route("sign_in")
    object SignUp: Route("sign_up")
}