package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.responses.movie.toMovieDetailModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.FavoriteService
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyService: FavoriteService
) : HistoryRepository, BaseService() {
    override suspend fun getDates(
        page: Int,
        size: Int
    ): Resource<List<String>?> {
        return safeApiCall {
            historyService.getDates(page = page, size = size)
        }.mapData {
            it?.map {
                it -> it.date
            }
        }
    }

    override suspend fun getHistoryOfDate(
        date: String,
        page: Int,
        size: Int
    ): Resource<List<MovieDetailModel>?> {
        return safeApiCall {
            historyService.getHistoryOfDate(date, page = page, size = size)
        }.mapData {
            it?.map { item ->
                item.toMovieDetailModel()
            }
        }
    }
}