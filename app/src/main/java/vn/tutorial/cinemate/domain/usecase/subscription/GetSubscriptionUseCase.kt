package vn.tutorial.cinemate.domain.usecase.subscription

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import javax.inject.Inject

class GetSubscriptionUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
): BaseUseCase<Unit, Resource<List<SubscriptionPlanModel>?>>() {
    override suspend fun execute(param: Unit): Resource<List<SubscriptionPlanModel>?> {
        return subscriptionRepository.getSubscriptions()
    }

}