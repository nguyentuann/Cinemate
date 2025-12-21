package vn.tutorial.cinemate.domain.usecase.subscription

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import javax.inject.Inject

class SearchEmailUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
): BaseUseCase<String, Resource<List<String>?>>() {
    override suspend fun execute(param: String): Resource<List<String>?> {
        return subscriptionRepository.searchEmail(param)
    }
}