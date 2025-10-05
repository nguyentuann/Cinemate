package vn.tutorial.cinemate.presentation.settings.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.MainActivity
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.ThemeType
import vn.tutorial.cinemate.presentation.settings.viewModel.SettingsViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun ThemeAndLanguageScreen(
    settingsViewModel: SettingsViewModel
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.welcome_message))

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                LogUtil("call setLocale en")
                settingsViewModel.setLocale("en")
            }) {
                Text("English")
            }

            Button(onClick = {
                LogUtil("call setLocale vi")
                settingsViewModel.setLocale("vi")
            }) {
                Text("Tiếng Việt")
            }

            Button(onClick = {
                LogUtil("call set light")
                settingsViewModel.setTheme(ThemeType.LIGHT)
            }) {
                Text("Light Theme")
            }

            Button(onClick = {
                LogUtil("call set dark")
                settingsViewModel.setTheme(ThemeType.DARK)
            }) {
                Text("Dark Theme")
            }
        }
    }
}
