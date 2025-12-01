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

    fun checkPassword(password: String): Map<String, Boolean> {
        val hasMinLength = password.length >= 8
        val hasUppercase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val specialCharRegex = Regex("[@\$!%*?&]")
        val hasSpecialChar = specialCharRegex.containsMatchIn(password)

        return mapOf(
            "minLength" to hasMinLength,
            "uppercase" to hasUppercase,
            "digit" to hasDigit,
            "specialChar" to hasSpecialChar
        )
    }

    fun validatePassword(password: String): String? {
        return when {
            password.length < 8 -> "Password must be at least 8 characters"
            !password.any { it.isUpperCase() } -> "Password must be at least 1 uppercase letter"
            !password.any { it.isDigit() } -> "Password must be at least 1 digit"
            !Regex("[@\$!%*?&]").containsMatchIn(password) -> "Password must be at least 1 special character"
            else -> null // Hợp lệ
        }
    }

}