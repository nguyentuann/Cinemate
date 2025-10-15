package vn.tutorial.cinemate.presentation.more.viewModels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
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
    private val localStorage: LocalStorage,
    private val app: Application
) : ViewModel() {
    private var _locale = MutableStateFlow(Locale(localStorage.getLanguage() ?: "vi"))
    var locale: StateFlow<Locale> = _locale

    private var _theme = MutableStateFlow(
        ThemeType.valueOf(
            localStorage.getTheme() ?: ThemeType.SYSTEM_DEFAULT.name
        )
    )
    var theme: StateFlow<ThemeType> = _theme

    private var _isNotificationEnabled = MutableStateFlow(false)
    var isNotificationEnabled: StateFlow<Boolean> = _isNotificationEnabled

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

    fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            _isNotificationEnabled.value = isGranted
        } else {
            // Android < 13 luôn được phép thông báo
            _isNotificationEnabled.value = true
        }
    }

    fun requestPermission(context: Context, launcher: ManagedActivityResultLauncher<String, Boolean>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun updateNotificationEnabled(enabled: Boolean) {
        _isNotificationEnabled.value = enabled
    }
}