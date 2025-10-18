package vn.tutorial.cinemate.presentation.more.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.usecase.authentication.ChangePasswordUseCase
import javax.inject.Inject

data class ChangePasswordStateUI(
    val name: String = "",
    val email: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
): ViewModel() {
    private var _state = MutableStateFlow(ChangePasswordStateUI())
    val state = _state

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun updateOldPasswordField(oldPassword: String) {
        _state.value = _state.value.copy(oldPassword = oldPassword)
    }

    fun updateNewPasswordField(newPassword: String) {
        _state.value = _state.value.copy(newPassword = newPassword)
    }

    fun updateConfirmPasswordField(confirmPassword: String) {
        _state.value = _state.value.copy(confirmPassword = confirmPassword)
    }

    fun updatePassword(onSuccess: () -> Unit) {
        executeUseCase(
            state = _state,
            block = {
                changePasswordUseCase(
                    ChangePasswordUseCase.Params(
                        oldPassword = _state.value.oldPassword,
                        newPassword = _state.value.newPassword,
                        confirmPassword = _state.value.confirmPassword
                    )
                )
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
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

}