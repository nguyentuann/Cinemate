package vn.tutorial.cinemate.core.constant.api_endpoint

object AuthEndpoint {
    private const val PREFIX = "auth/api/v1/"

    // todo Authentication
    const val VERIFY_EMAIL = "${PREFIX}verify-email"
    const val VERIFY_TOKEN = "${PREFIX}verify-token"
    const val SIGN_UP = "${PREFIX}sign-up"

    const val FORGOT_PASSWORD = "${PREFIX}forgot-password"
    const val VERIFY_OTP = "${PREFIX}verify-otp"
    const val RESET_PASSWORD = "${PREFIX}reset-password"

    const val LOGIN = "${PREFIX}login"
    const val LOGOUT = "${PREFIX}log-out"

    const val VERIFY_ACCOUNT = "${PREFIX}verify-account"
    const val CHANGE_PASSWORD = "${PREFIX}change-password"
    const val REFRESH_TOKEN = "${PREFIX}refresh-token"
}
