package vn.tutorial.cinemate.navigation

sealed class Route(val route: String) {
    object SignIn: Route("sign_in")
    object SignUp: Route("sign_up")
    object CheckMail: Route("check_mail")
    object CreatePassword: Route("create_password")
    object ChangePassword: Route("change_password")
    object VerifyEmail: Route("verify_email")

    object Profile: Route("profile")

    object Home: Route("home")

    object Splash: Route("splash")
}