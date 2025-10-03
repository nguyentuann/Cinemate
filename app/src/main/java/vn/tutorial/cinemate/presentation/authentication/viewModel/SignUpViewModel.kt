package vn.tutorial.cinemate.presentation.authentication.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
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

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun signUp() {
        executeUseCase(
            state = _state,
            block = {
                signUpUseCase(
                    SignUpUseCase.Params(
                        email = _state.value.email,
                        firstName = _state.value.firstName,
                        lastName = _state.value.lastName,
                        password = _state.value.password,
                        passwordConfirm = _state.value.passwordConfirm
                    )
                )
            },
            onSuccess = { user ->
                _state.value.copy(
                    isLoading = false,
                    user = user,
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

}