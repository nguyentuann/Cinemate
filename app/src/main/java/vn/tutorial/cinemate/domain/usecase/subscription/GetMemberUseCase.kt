package vn.tutorial.cinemate.domain.usecase.subscription

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MemberModel
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import javax.inject.Inject

class GetMemberUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
): BaseUseCase<Unit, Resource<List<MemberModel>?>>() {
    override suspend fun execute(param: Unit): Resource<List<MemberModel>?> {
        return subscriptionRepository.getMembers()
    }
}