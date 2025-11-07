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
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
        private const val USER_AVATAR = "user_avatar"

        private const val THEME = "theme"
        private const val LANGUAGE = "language"
    }

    fun saveUserId(userId: String) {
        prefs.edit { putString(USER_ID, userId) }
    }

    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun saveUserName(userName: String) {
        prefs.edit { putString(USER_NAME, userName) }
    }

    fun getUserName(): String? {
        return prefs.getString(USER_NAME, "Anonymous User")
    }

    fun saveUserAvatar(avatarUrl: String) {
        prefs.edit { putString(USER_AVATAR, avatarUrl) }
    }

    fun getUserAvatar(): String? {
        return prefs.getString(USER_AVATAR, "Anonymous Avatar")
    }

    fun saveAccessToken(token: String) {
        prefs.edit { putString(ACCESS_TOKEN, token) }
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun deleteAccessToken() {
        prefs.edit { remove(ACCESS_TOKEN) }
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