package vn.tutorial.cinemate.domain.usecase.history

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.HistoryRepository
import javax.inject.Inject

class GetDatesUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) : BaseUseCase<GetDatesUseCase.Params, Resource<List<String>?>>() {
    data class Params(
        val page: Int,
        val size: Int,
    )

    override suspend fun execute(param: Params): Resource<List<String>?> {
        return historyRepository.getDates(param.page, param.size)
    }
}