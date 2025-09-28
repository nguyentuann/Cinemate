package vn.tutorial.cinemate.presentation.settings.viewModel


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.domain.model.ThemeType
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _locale = MutableStateFlow(Locale.getDefault())
    val locale: StateFlow<Locale> = _locale

    private val _theme = MutableStateFlow(ThemeType.SYSTEM_DEFAULT)
    val theme: StateFlow<ThemeType> = _theme



    fun setLocale(locale: Locale) {
        _locale.value = locale
    }

    fun setTheme(newTheme: ThemeType) {
        _theme.value = newTheme
    }
}