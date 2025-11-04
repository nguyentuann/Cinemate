package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.MovieService
import vn.tutorial.cinemate.domain.model.FilmDetailModel
import vn.tutorial.cinemate.domain.repository.FilmRepository
import vn.tutorial.cinemate.mockdata.filmMock1
import vn.tutorial.cinemate.mockdata.filmMock2
import vn.tutorial.cinemate.mockdata.filmMock3
import vn.tutorial.cinemate.mockdata.filmMock4
import vn.tutorial.cinemate.mockdata.getFilmById
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private val filmService: MovieService
) : FilmRepository, BaseService() {
    override suspend fun searchFilms(query: String): Resource<List<FilmDetailModel>?> {
        val mockData = listOf(
            filmMock1,
            filmMock2,
            filmMock3,
            filmMock4,
        )
        return Resource.Success(mockData)
    }

    override suspend fun getTrendingFilms(): Resource<List<FilmDetailModel>?> {
        val mockData = listOf(
            filmMock1,
            filmMock2,
            filmMock3,
        )
        return Resource.Success(mockData)
    }

    override suspend fun getBannerFilms(): Resource<List<FilmDetailModel>?> {
        val mockData = listOf(
            filmMock4,
            filmMock2,
            filmMock3,
        )
        return Resource.Success(mockData)
    }

    override suspend fun getSectionFilms(): Resource<Map<String, List<FilmDetailModel>>?> {
        val mockData = mapOf(
            "Popular on Cinemate" to listOf(
                filmMock1,
                filmMock2,
                filmMock3,
            ),
            "Trending Now" to listOf(
                filmMock4,
                filmMock2,
                filmMock3,
            ),

            "New Releases" to listOf(
                filmMock1,
                filmMock4,
                filmMock3,
            )
        )
        return Resource.Success(mockData)
    }

    override suspend fun getDetailFilm(filmId: String): Resource<FilmDetailModel?> {
        val film = getFilmById(filmId)
        return Resource.Success(film)
    }


}
