package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.remote.requests.film.ReportProgressRequest
import vn.tutorial.cinemate.data.remote.responses.movie.toMovieDetailModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.MovieService
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : MovieRepository, BaseService() {
    override suspend fun searchFilms(query: String, page: Int, size: Int): Resource<List<MovieDetailModel>?> {

        return safeApiCall {
            movieService.searchFilms(query, page, size)
        }.mapData { wrapper ->
            wrapper?.map { it ->
                it.toMovieDetailModel()
            }
        }

    }

    override suspend fun getMovies(
        page: Int,
        size: Int,
        sortBy: String,
        sortDirection: String
    ): Resource<List<MovieDetailModel>?> {
        return safeApiCall {
            movieService.getMovies(page, size, sortBy, sortDirection)
        }.mapData { wrapper ->
            wrapper?.map { it ->
                it.toMovieDetailModel()
            }
        }
    }

    override suspend fun getDetailMovie(movieId: String): Resource<MovieDetailModel?> {
        return safeApiCall {
            movieService.getMovieById(movieId)
        }.mapData { it ->
            LogUtil(it.toString())
            it?.toMovieDetailModel()
        }
    }

    override suspend fun getTop10Movies(): Resource<List<MovieDetailModel>?> {
        return safeApiCall {
            movieService.getTop10Movies()
        }.mapData { wrapper ->
            wrapper?.map { it ->
                it.toMovieDetailModel()
            }
        }
    }

    override suspend fun reportProgress(
        movieId: String,
        lastWatchedPosition: Int,
        totalDuration: Int
    ): Resource<Unit?> {
        val request = ReportProgressRequest(
            lastWatchedPosition = lastWatchedPosition,
            totalDuration = totalDuration
        )

        return safeApiCall {
            movieService.reportProgress(movieId, request)
        }
    }

    override suspend fun getProgress(movieId: String): Resource<Int?> {
        return safeApiCall {
            movieService.getProgress(movieId)
        }.mapData { it ->
            it?.lastWatchedPosition ?: 0
        }
    }
}
