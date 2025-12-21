package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.constant.enums.TimeLimit
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.remote.requests.payment.AcceptInvitationRequest
import vn.tutorial.cinemate.data.remote.requests.payment.ChildrenModeRequest
import vn.tutorial.cinemate.data.remote.requests.payment.InviteMemberRequest
import vn.tutorial.cinemate.data.remote.requests.payment.PlanRequest
import vn.tutorial.cinemate.data.remote.responses.payment.toMemberModel
import vn.tutorial.cinemate.data.remote.responses.payment.toSubscriptionPlanModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.PaymentService
import vn.tutorial.cinemate.domain.model.ChildrenModeModel
import vn.tutorial.cinemate.domain.model.MemberModel
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val paymentService: PaymentService
) : SubscriptionRepository, BaseService() {
    override suspend fun getSubscriptions(): Resource<List<SubscriptionPlanModel>?> {
        return safeApiCall {
            paymentService.getAllSubscriptions()
        }.mapData { planResponses ->
            planResponses?.map { it.toSubscriptionPlanModel() }
        }
    }

    override suspend fun paySubscription(
        planId: String,
        autoRenew: Boolean,
    ): Resource<String?> {
        return safeApiCall {
            paymentService.createSubscription(
                PlanRequest(
                    planId = planId,
                    autoRenew = autoRenew
                )
            )
        }.mapData {
            it?.paymentUrl
        }
    }

    override suspend fun getCurrentSubscription(): Resource<SubscriptionPlanModel?> {
        return safeApiCall {
            paymentService.getCurrentSubscription()
        }.mapData {
            LogUtil("nhan current plan response: $it")
            var planModel = it?.plan?.toSubscriptionPlanModel()
            planModel?.subscriptionId = it?.id
            LogUtil("plan model: $planModel")
            planModel
        }
    }

    override suspend fun setChildrenMode(
        kidId: String,
        data: ChildrenModeModel
    ): Resource<Unit?> {
        return safeApiCall {
            paymentService.setChildrenMode(
                kidId = kidId,
                childrenModeRequest = ChildrenModeRequest(
                    blockedCategoryIds = data.blockedCategoryIds,
                    watchTimeLimitMinutes = data.watchTimeLimitMinutes?.time ?: 60
                )
            )
        }
    }

    override suspend fun getSubscriptionById(planId: String): Resource<SubscriptionPlanModel?> {
        return safeApiCall {
            paymentService.getSubscriptionById(planId)
        }.mapData {
            it?.toSubscriptionPlanModel()
        }
    }

    override suspend fun getMembers(): Resource<List<MemberModel>?> {
        return safeApiCall {
            paymentService.getMembers()
        }.mapData {
            it?.map { memberResponse ->
                memberResponse.toMemberModel()
            }
        }
    }

    override suspend fun inviteMember(
        email: String,
        mode: String
    ): Resource<Unit?> {
        return safeApiCall {
            paymentService.inviteMember(
                InviteMemberRequest(
                    email = email,
                    mode = mode,
                    sendEmail = true
                )
            )
        }
    }

    override suspend fun acceptInvitation(token: String): Resource<Unit?> {
        return safeApiCall {
            paymentService.acceptInvitation(
                AcceptInvitationRequest(
                    invitationToken = token
                )
            )
        }
    }

    override suspend fun searchEmail(query: String): Resource<List<String>?> {
        return safeApiCall {
            paymentService.searchEmail(query)
        }.mapData {
            it?.map { it ->
                it.email
            }
        }
    }

    override suspend fun cancelPlan(subscriptionId: String): Resource<Unit?> {
        return safeApiCall {
            paymentService.cancelPlan(subscriptionId)
        }
    }

    override suspend fun getChildrenMode(kidId: String): Resource<ChildrenModeModel?> {
        return safeApiCall {
            paymentService.getChildrenMode(kidId)
        }.mapData {
            LogUtil("nhan children mode response: $it")
            it?.let {
                ChildrenModeModel(
                    blockedCategoryIds = it.blockedCategories.map { category -> category.id },
                    watchTimeLimitMinutes = TimeLimit.entries.firstOrNull { enum -> enum.time == it.watchTimeLimitMinutes }
                )
            }
        }
    }
}