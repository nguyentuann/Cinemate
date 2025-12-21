package vn.tutorial.cinemate.domain.usecase.subscription

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import javax.inject.Inject

class GetSubscriptionByIdUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) : BaseUseCase<String, Resource<SubscriptionPlanModel?>>() {
    override suspend fun execute(param: String): Resource<SubscriptionPlanModel?> {
        return subscriptionRepository.getSubscriptionById(param)
    }
}