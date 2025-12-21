package vn.tutorial.cinemate.domain.usecase.subscription

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import javax.inject.Inject

class InviteMemberUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
): BaseUseCase<InviteMemberUseCase.Params, Resource<Unit?>>() {
    data class Params(
        val email: String,
        val mode: String
    )

    override suspend fun execute(param: Params): Resource<Unit?> {
        return subscriptionRepository.inviteMember(param.email, param.mode)
    }
}