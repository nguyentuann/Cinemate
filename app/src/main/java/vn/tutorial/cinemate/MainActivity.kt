package vn.tutorial.cinemate

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.collection.isNotEmpty
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorial.cinemate.navigation.App
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.more.viewModels.SettingsViewModel
import vn.tutorial.cinemate.ui.theme.CinemateTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("LocalContextConfigurationRead", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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

            var pendingIntent by remember { mutableStateOf(intent) }

            CinemateTheme(themeType = theme) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(LocalResources provides localizedContext.resources) {
                        App(settingsViewModel, navController)
                        LaunchedEffect(pendingIntent, navController.currentBackStackEntry) {
                            val data = pendingIntent?.data
                            if (data != null && data.path?.startsWith("/register/confirm") == true) {
                                val token = data.getQueryParameter("token")
                                if (!token.isNullOrEmpty() && navController.graph.nodes.isNotEmpty()) {
                                    navController.navigate(Route.VerifyToken.createRoute(token)) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                    pendingIntent = null
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}

