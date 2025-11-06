package vn.tutorial.cinemate.presentation.more.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.ProfileModel
import vn.tutorial.cinemate.domain.usecase.profile.GetProfileUseCase
import vn.tutorial.cinemate.domain.usecase.profile.UpdateProfileUseCase
import javax.inject.Inject

data class ProfileUiState(
    val profile: ProfileModel = ProfileModel(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state: MutableStateFlow<ProfileUiState> = _state

    init {
        getProfile()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun updateFirstName(firstName: String) {
        _state.update {
            it.copy(profile = it.profile.copy(firstName = firstName))
        }
    }

    fun updateLastName(lastName: String) {
        _state.update {
            it.copy(profile = it.profile.copy(lastName = lastName))
        }
    }

    fun updateDateOfBirth(date: String) {
        _state.update {
            it.copy(profile = it.profile.copy(dateOfBirth = date))
        }
    }

    fun updateGender(gender: String) {
        _state.update {
            it.copy(profile = it.profile.copy(gender = gender))
        }
    }

    fun getProfile() {
        val oldProfile = _state.value.profile
        executeUseCase(
            state = _state,
            block = {
                getProfileUseCase.invoke(Unit)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    profile = it ?: _state.value.profile
                )
            },
            onError = { error ->
                _state.value.copy(
                    isLoading = false,
                    error = error
                )
            },
            onLoading = {
                _state.value.copy(
                    isLoading = true,
                    error = null
                )
            }
        )
    }

    fun saveProfile() {
        executeUseCase(
            state = _state,
            block = {
                updateProfileUseCase.invoke(
                    _state.value.profile
                )
            },
            onSuccess = {
                _state.value.copy(
                    profile = it ?: _state.value.profile,
                )
            },
            onError = { error ->
                _state.value.copy(
                    isLoading = false,
                    error = error
                )
            },
            onLoading = {
                _state.value.copy(
                    isLoading = true,
                    error = null
                )
            }
        )
    }
}