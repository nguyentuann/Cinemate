package vn.tutorial.cinemate.presentation.settings.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.domain.model.ThemeType
import vn.tutorial.cinemate.presentation.settings.viewModel.SettingsViewModel
import java.util.Locale

@Composable
fun HomeScreen(
    settingsViewModel: SettingsViewModel = viewModel()
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.welcome_message))

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { settingsViewModel.setLocale(Locale("en")) }) {
                Text("English")
            }

            Button(onClick = { settingsViewModel.setLocale(Locale("vi")) }) {
                Text("Tiếng Việt")
            }

            Button(onClick = { settingsViewModel.setTheme(ThemeType.LIGHT) }) {
                Text("Light Theme")
            }

            Button(onClick = { settingsViewModel.setTheme(ThemeType.DARK) }) {
                Text("Dark Theme")
            }
        }
    }
}
