package vn.tutorial.cinemate.presentation.more.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.data.local.LocalStorage
import vn.tutorial.cinemate.domain.usecase.authentication.SignOutUseCase
import javax.inject.Inject

data class SignOutUIState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val localStorage: LocalStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(SignOutUIState())
    val state: StateFlow<SignOutUIState> = _state

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun signOut(onSuccess: () -> Unit) {
        executeUseCase(
            state = _state,
            block = {
                signOutUseCase(
                    SignOutUseCase.Params(
                        refreshToken = localStorage.getRefreshToken() ?: ""
                    )
                )
            },
            onSuccess = {
                // xoá data local
                localStorage.clearTokens()
                onSuccess()
                _state.value.copy(isLoading = false, error = null)
            },
            onError = { errorMsg ->
                _state.value.copy(isLoading = false, error = errorMsg)
            },
            onLoading = {
                _state.value.copy(isLoading = true, error = null)
            }
        )
    }

}