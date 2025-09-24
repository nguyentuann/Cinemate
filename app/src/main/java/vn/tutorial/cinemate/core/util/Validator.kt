package vn.tutorial.cinemate.core.util

import android.util.Patterns

object Validator {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex(
            pattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
        )
        return passwordRegex.matches(password)
    }

}