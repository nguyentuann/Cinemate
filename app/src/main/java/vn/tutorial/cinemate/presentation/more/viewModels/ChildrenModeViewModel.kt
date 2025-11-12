package vn.tutorial.cinemate.presentation.more.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.constant.enum.AgeLimit
import vn.tutorial.cinemate.core.constant.enum.TimeLimit
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.usecase.subscription.GetChildrenModeUseCase
import javax.inject.Inject

data class ChildrenModeUIState(
    val isEnable: Boolean = false,
    val selectedAgeLimit: AgeLimit = AgeLimit.AGE_7,
    val selectedWatchTime: TimeLimit = TimeLimit.TIME_30,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class ChildrenModeViewModel @Inject constructor(
    private val getChildrenModeUseCase: GetChildrenModeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ChildrenModeUIState())
    val state: StateFlow<ChildrenModeUIState> = _state

    fun enable() {
        _state.value = _state.value.copy(
            isEnable = !_state.value.isEnable
        )

    }

    fun selectAgeLimit(age: AgeLimit) {
        _state.value = _state.value.copy(
            selectedAgeLimit = age
        )
    }

    fun selectWatchTime(minutes: TimeLimit) {
        _state.value = _state.value.copy(
            selectedWatchTime = minutes
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun getChildrenMode(userId: String) {
        executeUseCase(
            state = _state,
            block = {
                getChildrenModeUseCase(userId)
            },
            onSuccess = { data ->
                _state.value.copy(
                    isLoading = false,
                    error = null,
                    isEnable = data?.isEnable == true,
                    selectedAgeLimit = data?.selectedAgeLimit ?: AgeLimit.AGE_18,
                    selectedWatchTime = data?.selectedWatchTime ?: TimeLimit.TIME_30
                )
            },
            onError = {
                _state.value.copy(
                    isLoading = false,
                    error = it
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

    fun setChildrenMode() {
        LogUtil("Children mode enabled: ${_state.value.isEnable}, Age limit: ${_state.value.selectedAgeLimit}, Watch time: ${_state.value.selectedWatchTime} minutes")
    }
}