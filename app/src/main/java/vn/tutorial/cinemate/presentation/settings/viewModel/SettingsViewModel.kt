package vn.tutorial.cinemate.presentation.settings.viewModel

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.local.LocalStorage
import vn.tutorial.cinemate.domain.model.ThemeType
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localStorage: LocalStorage
) : ViewModel() {
    private var _locale = MutableStateFlow(Locale(localStorage.getLanguage() ?: "vi"))
    var locale: StateFlow<Locale> = _locale

    private var _theme = MutableStateFlow(
        ThemeType.valueOf(
            localStorage.getTheme() ?: ThemeType.SYSTEM_DEFAULT.name
        )
    )
    var theme: StateFlow<ThemeType> = _theme

    fun setLocale(languageCode: String) {
        val newLocale = Locale(languageCode)
        _locale.value = newLocale
        localStorage.saveLanguage(languageCode)
    }

    fun setTheme(newTheme: ThemeType) {
        LogUtil("call setTheme in viewModel: $newTheme")
        _theme.value = newTheme
        localStorage.saveTheme(newTheme.name)
    }
}