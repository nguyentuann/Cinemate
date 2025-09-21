package vn.tutorial.cinemate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import vn.tutorial.cinemate.core.locale.ProvideAppLocale
import vn.tutorial.cinemate.navigation.App
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
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        App()
                    }
                }
            }
        }
    }
}

