package vn.tutorial.cinemate.presentation.authentication.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.usecase.authentication.ResetPasswordUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.VerifyEmailUseCase
import javax.inject.Inject

data class ForgotPasswordUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state: StateFlow<ForgotPasswordUiState> = _state

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _state.value = _state.value.copy(confirmPassword = confirmPassword)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun resetState() {
        _state.value = ForgotPasswordUiState()
    }

    fun verifyEmail(onSuccess: () -> Unit) {
        executeUseCase(
            state = _state,
            block = {
                verifyEmailUseCase(
                    VerifyEmailUseCase.Params(
                        email = _state.value.email,
                    )
                )
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    error = null
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

    fun resetPassword(onSuccess: () -> Unit) {
        LogUtil(state.value.toString())
        executeUseCase(
            state = _state,
            block = {
                resetPasswordUseCase(
                    ResetPasswordUseCase.Params(
                        email = _state.value.email,
                        otp = "",
                        newPassword = _state.value.password
                    )
                )
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    error = null
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