package vn.tutorial.cinemate.navigation

sealed class Route(val route: String) {
    //todo route splash
    object Started: Route("started")
    object Splash: Route("splash")
    object SignUpGraph: Route("sign_up_graph")
    object ForgotPasswordGraph: Route("forgot_password_graph")

    // todo route auth
    object SignIn: Route("sign_in")
    object SignUp: Route("sign_up")
    object CheckMail: Route("check_mail/{email}") {
        fun createRoute(email: String) = "check_mail/$email"
    }
    object CreatePassword: Route("create_password")
    object ChangePassword: Route("change_password")
    object ForgotPassword: Route("forgot_password")
    object VerifyOTP: Route("verify_otp")
    object VerifyToken: Route("verify_token/{token}") {
        fun createRoute(token: String) = "verify_token/$token"
    }


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
    object ThemeAndLanguage: Route("theme_and_language")
    object Profile: Route("profile")
    object SettingNotification: Route("setting_notification")

}