package vn.tutorial.cinemate.domain.usecase.subscription

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ChildrenModeModel
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import javax.inject.Inject

class GetChildrenModeUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) : BaseUseCase<String, Resource<ChildrenModeModel?>>() {
    override suspend fun execute(param: String): Resource<ChildrenModeModel?> {
        return subscriptionRepository.getChildrenModeStatus(param)
    }
}