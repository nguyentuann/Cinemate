package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.FilmService
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.domain.model.filmMock
import vn.tutorial.cinemate.domain.repository.FilmRepository
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private val filmService: FilmService
) : FilmRepository, BaseService() {
    override suspend fun searchFilms(query: String): Resource<List<FilmDetailModel>?> {
        val mockData = listOf(
            filmMock,
            filmMock,
            filmMock,
            filmMock,
            filmMock,
        )
        return Resource.Success(mockData)
    }

    override suspend fun getTrendingFilms(): Resource<List<FilmDetailModel>?> {
        val mockData = listOf(
            filmMock,
            filmMock,
            filmMock,
        )
        return Resource.Success(mockData)
    }
}
