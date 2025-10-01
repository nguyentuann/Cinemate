package vn.tutorial.cinemate.presentation.authentication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.UserModel
import vn.tutorial.cinemate.domain.usecase.authentication.SignUpUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.VerifyOTPUseCase
import javax.inject.Inject

data class SignUpUiState(
    val email: String = "nhattuan@gmail.com",
    val firstName: String = "Nhat Tuan",
    val lastName: String = "Nguyen",
    val password: String = "Nhat@tuan2402",
    val passwordConfirm: String = "Nhat@tuan2402",
    val otp: List<Int> = List(4) { -1 },
    val isLoading: Boolean = false,
    val user: UserModel? = null,
    val error: String? = null
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val verifyOTPUseCase: VerifyOTPUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    val state: StateFlow<SignUpUiState> = _state

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun updateFirstName(firstName: String) {
        _state.value = _state.value.copy(firstName = firstName)
    }

    fun updateLastName(lastName: String) {
        _state.value = _state.value.copy(lastName = lastName)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun updatePasswordConfirm(passwordConfirm: String) {
        _state.value = _state.value.copy(passwordConfirm = passwordConfirm)
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

    fun signUp() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = signUpUseCase(
                SignUpUseCase.Params(
                    email = _state.value.email,
                    firstName = _state.value.firstName,
                    lastName = _state.value.lastName,
                    password = _state.value.password,
                    passwordConfirm = _state.value.passwordConfirm
                )
            )) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = result.data,
                        error = null
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
//                        error = result.message
                        user = UserModel(
                            id = "1",
                            email = _state.value.email,
                            firstName = _state.value.firstName,
                            lastName = _state.value.lastName,
                            isEnabled = false
                        )
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    fun verifyOTP(otp: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = verifyOTPUseCase(
                VerifyOTPUseCase.Params(
                    email = _state.value.email,
                    otp = otp
                )
            )) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = null
                    )
                    onSuccess()
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }
}