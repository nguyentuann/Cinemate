package vn.tutorial.cinemate.presentation.splash.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import vn.tutorial.cinemate.presentation.splash.viewModel.SplashState
import vn.tutorial.cinemate.presentation.splash.viewModel.StartedViewModel

@Composable
fun StartedScreen(
    onNavigateHome: () -> Unit,
    onNavigateAuth: () -> Unit,
    viewModel: StartedViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            SplashState.GoToHome -> onNavigateHome()
            SplashState.GoToAuth -> onNavigateAuth()
            else -> {}
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}