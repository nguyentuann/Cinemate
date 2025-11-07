package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.requests.film.FavoriteRequest
import vn.tutorial.cinemate.data.remote.responses.movie.toMovieDetailModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.FavoriteService
import vn.tutorial.cinemate.data.remote.services.MovieService
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.FavoriteRepository
import vn.tutorial.cinemate.mockdata.listFilm
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteService: FavoriteService
) : FavoriteRepository, BaseService() {
    override suspend fun getFavoriteMovies(): Resource<List<MovieDetailModel>?> {
//        return safeApiCall {
//            favoriteService.getFavoriteMovies()
//        }.mapData { wrapper ->
//            wrapper?.map { it ->
//                it.toMovieDetailModel()
//            }
//        }
        return Resource.Success(listFilm)
    }

    override suspend fun addFavorite(movieId: String): Resource<Unit?> {
        return safeApiCall {
            favoriteService.addFavoriteMovie(
                FavoriteRequest(movieId)
            )
        }
    }

    override suspend fun deleteFavorite(movieId: String): Resource<Unit?> {
        return safeApiCall {
            favoriteService.deleteFavoriteMovie(movieId)
        }
    }
}