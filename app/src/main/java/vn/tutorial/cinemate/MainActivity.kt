package vn.tutorial.cinemate

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.navigation.App
import vn.tutorial.cinemate.presentation.more.viewModels.SettingsViewModel
import vn.tutorial.cinemate.ui.theme.CinemateTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("LocalContextConfigurationRead")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val theme by settingsViewModel.theme.collectAsState()
            val locale by settingsViewModel.locale.collectAsState()

            val context = LocalContext.current

            // Tạo context mới khi locale thay đổi
            val localizedContext = remember(locale) {
                context.createConfigurationContext(
                    Configuration(context.resources.configuration).apply {
                        setLocale(locale)
                    }
                )
            }

            CinemateTheme(themeType = theme) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(LocalResources provides localizedContext.resources) {
                        App(
                            settingsViewModel
                        )
                    }
                }
            }
        }
    }
}

