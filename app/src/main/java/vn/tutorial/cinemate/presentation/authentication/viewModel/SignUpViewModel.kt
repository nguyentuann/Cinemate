package vn.tutorial.cinemate.presentation.authentication.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.local.LocalStorage
import vn.tutorial.cinemate.domain.model.UserModel
import vn.tutorial.cinemate.domain.usecase.authentication.SignUpUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.VerifyEmailUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.VerifyTokenUseCase
import javax.inject.Inject

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val token: String = "",
    val isLoading: Boolean = false,
    val user: UserModel? = null,
    val error: String? = null
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val verifyTokenUseCase: VerifyTokenUseCase,
    private val localStorage: LocalStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    val state: StateFlow<SignUpUiState> = _state

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun updatePasswordConfirm(passwordConfirm: String) {
        _state.value = _state.value.copy(passwordConfirm = passwordConfirm)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun verifyEmailSignUp(onSuccess: () -> Unit) {
        executeUseCase(
            state = _state,
            block = {
                localStorage.clearTokens()
                verifyEmailUseCase(VerifyEmailUseCase.Params(_state.value.email))
            },
            onSuccess = {
                onSuccess()
                _state.value.copy(
                    isLoading = false,
                    error = null
                )
            },
            onError = { errorMsg ->
                _state.value.copy(
                    isLoading = false,
                    error = errorMsg
                )
            },
            onLoading = {
                _state.value.copy(isLoading = true, error = null)
            }
        )
    }

    fun signUp(onSuccess: () -> Unit) {
        LogUtil(
            "" +
                    "call sign up with email: ${_state.value.email}, password: ${_state.value.password}, token: ${_state.value.token}"
        )
        executeUseCase(
            state = _state,
            block = {
                signUpUseCase(
                    SignUpUseCase.Params(
                        email = _state.value.email,
                        password = _state.value.password,
                        token = _state.value.token
                    )
                )
            },
            onSuccess = { user ->
                _state.value.copy(
                    isLoading = false,
                    user = user,
                    error = null
                ).also {
                    onSuccess()
                }
            },
            onError = { errorMsg ->
                _state.value.copy(
                    isLoading = false,
                    error = errorMsg
                )
            },
            onLoading = {
                _state.value.copy(isLoading = true, error = null)
            }
        )
    }

    fun verifyToken(token: String, onSuccess: () -> Unit) {
        LogUtil("call verify token")
        executeUseCase(
            state = _state,
            block = {
                verifyTokenUseCase(token)
            },
            onSuccess = {
                LogUtil("verify token success: $it")

                _state.value.copy(
                    isLoading = false,
                    error = null,
                    token = token,
                    email = it ?: ""
                ).also { onSuccess() }
            },
            onError = { errorMsg ->
                _state.value.copy(
                    isLoading = false,
                    error = errorMsg
                )
            },
            onLoading = {
                _state.value.copy(isLoading = true, error = null)
            }
        )
    }

}