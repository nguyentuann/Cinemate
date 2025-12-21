package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel

interface HistoryRepository {
    suspend fun getDates(page: Int, size: Int): Resource<List<String>?>

    suspend fun getHistoryOfDate(
        date: String,
        page: Int,
        size: Int
    ): Resource<List<MovieDetailModel>?>

}