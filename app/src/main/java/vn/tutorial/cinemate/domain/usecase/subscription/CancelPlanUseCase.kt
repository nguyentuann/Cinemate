package vn.tutorial.cinemate.domain.usecase.subscription

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import javax.inject.Inject

class CancelPlanUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
): BaseUseCase<String, Resource<Unit?>>() {
    override suspend fun execute(param: String): Resource<Unit?> {
        return subscriptionRepository.cancelPlan(param)
    }
}