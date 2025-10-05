package vn.tutorial.cinemate.presentation.authentication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.VerifyOTPUseCase
import javax.inject.Inject

data class VerifyOTPUiState(
    val email: String = "",
    val otp: List<Int> = List(4) { -1 },
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class VerifyOTPViewModel @Inject constructor(
    private val verifyOTPUseCase: VerifyOTPUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VerifyOTPUiState())
    val state: StateFlow<VerifyOTPUiState> = _state

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

    fun verifyOTP(otp: String, onSuccess: () -> Unit) {
        executeUseCase(
            state = _state,
            block = {
                verifyOTPUseCase(
                    VerifyOTPUseCase.Params(
                        email = _state.value.email,
                        otp = otp
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