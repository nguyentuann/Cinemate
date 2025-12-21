package vn.tutorial.cinemate.presentation.splash.viewModel

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.local.LocalStorage
import vn.tutorial.cinemate.domain.usecase.authentication.RefreshTokenUseCase
import javax.inject.Inject

sealed class SplashState {
    object Loading : SplashState()
    object GoToHome : SplashState()
    object GoToAuth : SplashState()
}

@HiltViewModel
class StartedViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val refreshToken = localStorage.getRefreshToken()

            if (!refreshToken.isNullOrEmpty() && isJwtValid(refreshToken)) {
                // refresh token còn hạn → thử refresh
                localStorage.deleteAccessToken()

                val result = refreshTokenUseCase(refreshToken)

                if (result is Resource.Success) {
                    _state.value = SplashState.GoToHome
                } else {
                    // refresh token còn hạn nhưng server reject
                    localStorage.clearTokens()
                    _state.value = SplashState.GoToAuth
                }
            } else {
                // refresh token null hoặc hết hạn
                localStorage.clearTokens()
                _state.value = SplashState.GoToAuth
            }
        }
    }


    private fun isJwtValid(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return false
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)
            val exp = json.optLong("exp", 0L)
            val now = System.currentTimeMillis() / 1000
            now < exp
        } catch (e: Exception) {
            false
        }
    }
}

