package vn.tutorial.cinemate

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.navigation.App
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.more.viewModels.SettingsViewModel
import vn.tutorial.cinemate.ui.theme.CinemateTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val latestDeepLink = mutableStateOf<Uri?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("LocalContextConfigurationRead", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        latestDeepLink.value = intent?.data

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val theme by settingsViewModel.theme.collectAsState()
            val locale by settingsViewModel.locale.collectAsState()
            val navController = rememberNavController()
            val context = LocalContext.current

            // Tạo context mới khi locale thay đổi
            val localizedContext = remember(locale) {
                context.createConfigurationContext(
                    Configuration(context.resources.configuration).apply {
                        setLocale(locale)
                    }
                )
            }

            LaunchedEffect(latestDeepLink.value) {
                latestDeepLink.value?.getQueryParameter("token")?.let { token ->
                    navController.navigate(Route.VerifyToken.createRoute(token)) {
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            }

            CinemateTheme(themeType = theme) {

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(LocalResources provides localizedContext.resources) {
                        App(settingsViewModel, navController)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        latestDeepLink.value = intent.data
    }
}