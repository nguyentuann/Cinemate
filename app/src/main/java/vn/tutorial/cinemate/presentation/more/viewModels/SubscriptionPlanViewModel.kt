package vn.tutorial.cinemate.presentation.more.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel
import vn.tutorial.cinemate.domain.usecase.subscription.GetCurrentSubscriptionUseCase
import vn.tutorial.cinemate.domain.usecase.subscription.GetSubscriptionUseCase
import vn.tutorial.cinemate.domain.usecase.subscription.PaySubscriptionUseCase
import javax.inject.Inject

data class SubscriptionPlanUIState(
    val plans: List<SubscriptionPlanModel> = emptyList(),
    val selectedPlanId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class SubscriptionPlanViewModel @Inject constructor(
    private val getSubscriptionUseCase: GetSubscriptionUseCase,
    private val paySubscriptionUseCase: PaySubscriptionUseCase,
    private val getCurrentSubscriptionUseCase: GetCurrentSubscriptionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SubscriptionPlanUIState())
    val state: StateFlow<SubscriptionPlanUIState> = _state

    fun getSubscriptionPlans() {
        executeUseCase(
            state = _state,
            block = {
                getSubscriptionUseCase(Unit)
            },
            onSuccess = { plans ->
                _state.value.copy(
                    isLoading = false,
                    error = null,
                    plans = plans ?: emptyList()
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

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun selectPlan(planId: String) {
        _state.value = _state.value.copy(selectedPlanId = planId)
        LogUtil("Selecting plan: ${_state.value.selectedPlanId}")
    }

    fun paySubscription(onSuccess: (String) -> Unit) {
        _state.value.selectedPlanId?.let {

            executeUseCase(
                state = _state,
                block = {
                    paySubscriptionUseCase.invoke(it)
                },
                onSuccess = { paymentUrl ->
                    paymentUrl?.let {
                        onSuccess(paymentUrl)
                    }
                    _state.value.copy(
                        isLoading = false,
                        error = null
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
    }

    fun getCurrentSubscription(onResult: (SubscriptionPlanModel?) -> Unit) {
        executeUseCase(
            state = _state,
            block = {
                getCurrentSubscriptionUseCase(Unit)
            },
            onSuccess = { currentPlan ->
                onResult(currentPlan)
                _state.value.copy(
                    isLoading = false,
                    error = null
                )
            },
            onError = {
                onResult(null)
                _state.value.copy(
                    isLoading = false,
                    error = null
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