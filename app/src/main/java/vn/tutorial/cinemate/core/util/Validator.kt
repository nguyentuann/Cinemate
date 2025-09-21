package vn.tutorial.cinemate.core.util

import android.util.Patterns

object Validator {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length > 6
    }
}