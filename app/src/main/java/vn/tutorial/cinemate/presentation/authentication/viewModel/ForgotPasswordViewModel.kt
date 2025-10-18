package vn.tutorial.cinemate.presentation.authentication.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.usecase.authentication.ForgotPasswordUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.ResetPasswordUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.VerifyEmailUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.VerifyOTPUseCase
import javax.inject.Inject

data class ForgotPasswordUiState(
    val email: String = "",
    val otp: List<Int> = List(4) { -1 },
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val verifyOTPUseCase: VerifyOTPUseCase
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

    fun updateOTP(value: String, index: Int) {
        // chỉ nhận ký tự số 0-9, và chỉ 1 ký tự
        if (value.length == 1 && value[0].isDigit()) {
            val newOtp = _state.value.otp.toMutableList()
            newOtp[index] = value.toInt() // chuyển char '5' thành int 5
            _state.value = _state.value.copy(otp = newOtp)
        } else if (value.isEmpty()) {
            // nếu xoá ký tự
            val newOtp = _state.value.otp.toMutableList()
            newOtp[index] = -1
            _state.value = _state.value.copy(otp = newOtp)
        }
    }

    fun getOtpCode(): String {
        return _state.value.otp
            .filter { it != -1 } // bỏ mấy ô chưa nhập
            .joinToString("") { it.toString() }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun forgotPassword(onSuccess: () -> Unit) {
        LogUtil("ForgotPasswordViewModel: email = ${_state.value.email}")
        executeUseCase(
            state = _state,
            block = {
                forgotPasswordUseCase(
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

    fun verifyOTP(onSuccess: () -> Unit) {
        executeUseCase(
            state = _state,
            block = {
                verifyOTPUseCase(
                    VerifyOTPUseCase.Params(
                        email = _state.value.email,
                        otp = getOtpCode()
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
                        otp = getOtpCode(),
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