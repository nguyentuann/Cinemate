package vn.tutorial.cinemate.domain.usecase.subscription

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.constant.enums.TimeLimit
import vn.tutorial.cinemate.domain.model.ChildrenModeModel
import vn.tutorial.cinemate.domain.repository.SubscriptionRepository
import javax.inject.Inject

class SetChildrenModeUseCase @Inject constructor(
    private val repository: SubscriptionRepository
): BaseUseCase<SetChildrenModeUseCase.Params, Resource<Unit?>>() {
    override suspend fun execute(params: Params) : Resource<Unit?>{
        return repository.setChildrenMode(
            kidId = params.kidId,
            data = ChildrenModeModel(
                blockedCategoryIds = params.blockedCategoryIds,
                watchTimeLimitMinutes = params.watchTimeLimitMinutes
            )
        )
    }
    data class Params(
        val kidId: String,
        val blockedCategoryIds: List<String>,
        val watchTimeLimitMinutes: TimeLimit
    )
}