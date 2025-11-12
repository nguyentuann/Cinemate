package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ChildrenModeModel
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel

interface SubscriptionRepository {
    suspend fun getSubscriptions(): Resource<List<SubscriptionPlanModel>?>
    suspend fun paySubscription(userId: String, planId: String): Resource<Boolean?>

    suspend fun getChildrenModeStatus(userId: String): Resource<ChildrenModeModel?>
    suspend fun setChildrenModeStatus(userId: String, data: ChildrenModeModel): Resource<Unit?>
}