package vn.tutorial.cinemate.presentation.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.usecase.notification.GetImportantNotificationUseCase
import vn.tutorial.cinemate.domain.usecase.notification.GetOtherNotificationUseCase
import vn.tutorial.cinemate.mockdata.Notification
import javax.inject.Inject

data class NotificationUiState(
    val importantNotifications: List<Notification> = emptyList(),
    val otherNotifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getImportantNotificationUseCase: GetImportantNotificationUseCase,
    private val getOtherNotificationUseCase: GetOtherNotificationUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationUiState())
    val state: StateFlow<NotificationUiState> = _state

    init {
        refresh()
        _state.value = _state.value.copy(isLoading = true)
    }

    fun refresh() {
        getImportantNotifications()
        getOtherNotifications()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun getImportantNotifications() {
        executeUseCase(
            state = _state,
            block = {
                getImportantNotificationUseCase.invoke(Unit)
            },
            onSuccess = {
                _state.value.copy(
                    importantNotifications = it ?: emptyList(),
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

        )
    }

    fun getOtherNotifications() {
        executeUseCase(
            state = _state,
            block = {
                getOtherNotificationUseCase.invoke(Unit)
            },
            onSuccess = {
                _state.value.copy(
                    otherNotifications = it ?: emptyList(),
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
        )
    }
}