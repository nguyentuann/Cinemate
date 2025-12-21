package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ChildrenModeModel
import vn.tutorial.cinemate.domain.model.MemberModel
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel

interface SubscriptionRepository {
    suspend fun getSubscriptions(): Resource<List<SubscriptionPlanModel>?>
    suspend fun paySubscription(planId: String, autoRenew: Boolean = true): Resource<String?>

    suspend fun getCurrentSubscription(): Resource<SubscriptionPlanModel?>
    suspend fun getSubscriptionById(planId: String): Resource<SubscriptionPlanModel?>
    suspend fun getMembers(): Resource<List<MemberModel>?>
    suspend fun removeMember(memberUserId: String): Resource<Unit?>
    suspend fun inviteMember(email: String, mode: String): Resource<Unit?>
    suspend fun acceptInvitation(token: String): Resource<Unit?>

    suspend fun setChildrenMode(kidId: String, data: ChildrenModeModel): Resource<Unit?>


    suspend fun searchEmail(query: String): Resource<List<String>?>

    suspend fun cancelPlan(subscriptionId: String): Resource<Unit?>

    suspend fun getChildrenMode(kidId: String): Resource<ChildrenModeModel?>
}