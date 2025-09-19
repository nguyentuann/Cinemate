package vn.tutorial.cinemate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import vn.tutorial.cinemate.core.locale.ProvideAppLocale
import vn.tutorial.cinemate.domain.model.ThemeType
import vn.tutorial.cinemate.presentation.settings.screens.HomeScreen
import vn.tutorial.cinemate.presentation.settings.viewModel.SettingsViewModel
import vn.tutorial.cinemate.ui.theme.CinemateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val locale = settingsViewModel.locale.collectAsState().value
            val theme = settingsViewModel.theme.collectAsState().value

            ProvideAppLocale(locale) {
                CinemateTheme(
                    darkTheme = theme == ThemeType.DARK
                ) {
                    HomeScreen(settingsViewModel)
                }
            }
        }
    }
}

