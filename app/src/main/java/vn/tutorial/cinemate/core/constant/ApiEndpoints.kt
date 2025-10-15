package vn.tutorial.cinemate.core.constant

object ApiEndpoints {
    const val BASE_URL = "http://192.168.1.3:8585/auth/api/v1/"

    // todo Authentication
    const val VERIFY_EMAIL = "verify-email"
    const val VERIFY_TOKEN = "verify-token"
    const val SIGN_UP = "sign-up"

    const val FORGOT_PASSWORD = "forgot-password"
    const val VERIFY_OTP = "verify-otp"
    const val RESET_PASSWORD = "reset-password"

    const val LOGIN = "login"
    const val LOGOUT = "log-out"


    const val VERIFY_ACCOUNT = "verify-account"
    const val CHANGE_PASSWORD = "change-password"
    const val REFRESH_TOKEN = "refresh-token"
}