package vn.tutorial.cinemate.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import vn.tutorial.cinemate.core.util.LogUtil
import javax.inject.Inject

class LocalStorage @Inject constructor(
    private val prefs: SharedPreferences
) {
    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val THEME = "theme"
        private const val LANGUAGE = "language"
    }

    fun saveAccessToken(token: String) {
        prefs.edit { putString(ACCESS_TOKEN, token) }
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun saveRefreshToken(token: String) {
        prefs.edit { putString(REFRESH_TOKEN, token) }
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    fun saveTheme(theme: String) {
        prefs.edit { putString(THEME, theme) }
    }

    fun getTheme(): String? {
        return prefs.getString(THEME, null)
    }

    fun saveLanguage(language: String) {
        LogUtil("call save language: $language")
        prefs.edit { putString(LANGUAGE, language) }
    }

    fun getLanguage(): String? {
        return prefs.getString(LANGUAGE, null)
    }

    fun clearTokens() {
        prefs.edit {
            remove(ACCESS_TOKEN)
            remove(REFRESH_TOKEN)
        }
    }
}