package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.constant.enums.AgeLimit
import vn.tutorial.cinemate.core.constant.enums.TimeLimit
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.domain.model.ChildrenModeModel
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import vn.tutorial.cinemate.mockdata.samplePlans
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor() : SubscriptionRepository, BaseService() {
    override suspend fun getSubscriptions(): Resource<List<SubscriptionPlanModel>?> {
        LogUtil("call ở implementation")
        return Resource.Success(samplePlans)
    }

    override suspend fun paySubscription(
        userId: String,
        planId: String
    ): Resource<Boolean?> {
        return Resource.Success(true)
    }

    override suspend fun getChildrenModeStatus(userId: String): Resource<ChildrenModeModel?> {
        return Resource.Success(
            ChildrenModeModel(
                isEnable = true,
                selectedAgeLimit = AgeLimit.AGE_3,
                selectedWatchTime = TimeLimit.TIME_30

            )
        )
    }

    override suspend fun setChildrenModeStatus(
        userId: String,
        data: ChildrenModeModel
    ): Resource<Unit?> {
        return Resource.Success(Unit)
    }
}