package vn.tutorial.cinemate.presentation.authentication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vn.tutorial.cinemate.domain.usecase.authentication.SignUpUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.VerifyEmailUseCase
import javax.inject.Inject

data class SignUpState(
    var email: String = "",
    var password: String = "",

    val isLoading: Boolean = false,
    var error: String? = null,
    var success: Boolean = false
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun validateEmail() {
        // todo validate email to forward to
        viewModelScope.launch {
            verifyEmailUseCase(_state.value.email)
        }
    }

    fun signUp() {
        // todo call sign up use case
        viewModelScope.launch {
            signUpUseCase(SignUpUseCase.Params(_state.value.email, _state.value.password))
        }
    }
}