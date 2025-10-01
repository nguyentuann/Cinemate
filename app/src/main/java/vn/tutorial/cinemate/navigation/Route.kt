package vn.tutorial.cinemate.navigation

sealed class Route(val route: String) {
    //todo route splash
    object Splash: Route("splash")

    // todo route auth
    object SignIn: Route("sign_in")
    object SignUp: Route("sign_up")
    object CheckMail: Route("check_mail")
    object CreatePassword: Route("create_password")
    object ChangePassword: Route("change_password")
    object VerifyEmail: Route("verify_email")
    object VerifyOTP: Route("verify_otp")


    // todo route main
    object Home: Route("home")
    object More: Route("more")
    object Notification: Route("notification")
    object ComingSoon: Route("coming_soon")
    object Search: Route("search")

    object Detail: Route("detail/{filmId}") {
        fun createRoute(filmId: String) = "detail/$filmId"
    }

    object PlayVideo: Route("play_video/{filmId}") {
        fun createRoute(filmId: String) = "play_video/$filmId"
    }

    // todo route setting
    object Favorite: Route("favorite")
    object History: Route("history")

}