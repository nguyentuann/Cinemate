package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.responses.movie.toMovieDetailModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.MovieService
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.MovieRepository
import vn.tutorial.cinemate.mockdata.listFilm
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : MovieRepository, BaseService() {
    override suspend fun searchFilms(query: String): Resource<List<MovieDetailModel>?> {
        return Resource.Success(listFilm)
    }

    override suspend fun getMovies(
        page: Int,
        size: Int,
        sortBy: String
    ): Resource<List<MovieDetailModel>?> {
        return safeApiCall {
            movieService.getMovies(page, size, sortBy)
        }.mapData { wrapper ->
            wrapper?.map { it ->
                it.toMovieDetailModel()
            }
        }

//        return Resource.Success(listFilm)
    }

    override suspend fun getDetailMovie(movieId: String): Resource<MovieDetailModel?> {
        return safeApiCall {
            movieService.getMovieById(movieId)
        }.mapData { it ->
            it?.toMovieDetailModel()
        }

//        return Resource.Success(getFilmById(movieId))
    }

}
