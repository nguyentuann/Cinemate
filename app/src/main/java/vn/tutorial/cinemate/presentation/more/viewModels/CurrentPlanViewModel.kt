package vn.tutorial.cinemate.presentation.more.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import vn.tutorial.cinemate.core.base_class.executeUseCase
import vn.tutorial.cinemate.domain.model.MemberModel
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel
import vn.tutorial.cinemate.domain.usecase.subscription.AcceptInvitationUseCase
import vn.tutorial.cinemate.domain.usecase.subscription.GetMemberUseCase
import vn.tutorial.cinemate.domain.usecase.subscription.GetSubscriptionByIdUseCase
import vn.tutorial.cinemate.domain.usecase.subscription.InviteMemberUseCase
import javax.inject.Inject

data class CurrentPlanUIState(
    val currentPlan: SubscriptionPlanModel? = null,
    val currentMembers: List<MemberModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CurrentPlanViewModel @Inject constructor(
    private val getSubscriptionPlanByIdUseCase: GetSubscriptionByIdUseCase,
    private val getMemberUseCase: GetMemberUseCase,
    private val inviteMemberUseCase: InviteMemberUseCase,
    private val acceptInvitationUseCase: AcceptInvitationUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CurrentPlanUIState())
    val state: StateFlow<CurrentPlanUIState> = _state


    private var loadedPlanIds = mutableStateOf<String>("") // track planId đã load

    fun loadData(planId: String) {
        if (loadedPlanIds.value == planId) {
            return
        }
        loadedPlanIds.value = planId

        getPlanById(planId)
        getMember()
    }

    fun getPlanById(planId: String) {
        executeUseCase(
            state = _state,
            block = {
                getSubscriptionPlanByIdUseCase.invoke(planId)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    currentPlan = it
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
                    error = null,
                    currentPlan = null,
                    isLoading = true
                )
            }
        )
    }

    fun getMember() {
        executeUseCase(
            state = _state,
            block = {
                getMemberUseCase.invoke(Unit)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false,
                    currentMembers = it ?: emptyList()
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
                    error = null,
                    // currentMembers = emptyList(),
                    isLoading = true
                )
            }
        )
    }

    fun inviteMember(email: String, type: String) {
        executeUseCase(
            state = _state,
            block = {
                inviteMemberUseCase.invoke(
                    InviteMemberUseCase.Params(
                        email = email,
                        mode = type
                    )
                )
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false
                )
            },
            onError = {
                _state.value.copy(
                    isLoading = false,
                    error = it
                )
            },
        )
    }

    fun acceptInvitation(token: String) {
        executeUseCase(
            state = _state,
            block = {
                acceptInvitationUseCase.invoke(token)
            },
            onSuccess = {
                _state.value.copy(
                    isLoading = false
                )
            },
            onError = {
                _state.value.copy(
                    isLoading = false,
                    error = it
                )
            },
        )
    }
}