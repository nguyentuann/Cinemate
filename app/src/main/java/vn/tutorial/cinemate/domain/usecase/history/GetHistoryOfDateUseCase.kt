package vn.tutorial.cinemate.domain.usecase.history

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.HistoryRepository
import javax.inject.Inject

class GetHistoryOfDateUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) : BaseUseCase<GetHistoryOfDateUseCase.Params, Resource<List<MovieDetailModel>?>>() {

    data class Params(
        val date: String,
        val page: Int,
        val size: Int,
    )

    override suspend fun execute(param: Params): Resource<List<MovieDetailModel>?> {
        return historyRepository.getHistoryOfDate(param.date, param.page, param.size)
    }
}