package vn.tutorial.cinemate.presentation.authentication.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.UserModel
import vn.tutorial.cinemate.domain.usecase.authentication.SignInUseCase
import javax.inject.Inject

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignInUiState())
    val state: StateFlow<SignInUiState> = _state

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun signIn(onSuccess: () -> Unit) {
        executeUseCase(
            state = _state,
            block = {
                signInUseCase(
                    SignInUseCase.Params(
                        email = _state.value.email,
                        password = _state.value.password
                    )
                )
            },
            onSuccess = { user ->
                LogUtil("Vao thanh cong")
                _state.value.copy(
                    isLoading = false,
                    user = user,
                    error = null
                ).also { onSuccess() }
            },
            onError = { error ->
                LogUtil("Vao that bai")
                _state.value.copy(
                    isLoading = false,
                    error = error
                )
            },
            onLoading = {
                LogUtil("Dang loading")
                _state.value.copy(
                    isLoading = true,
                    error = null
                )
            }
        )
    }
}