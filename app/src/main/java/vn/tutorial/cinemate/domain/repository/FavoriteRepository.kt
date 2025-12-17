package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel

interface FavoriteRepository {
    suspend fun getFavoriteMovies(page: Int, size: Int): Resource<List<MovieDetailModel>?>

    suspend fun addFavorite(movieId: String): Resource<Unit?>

    suspend fun deleteFavorite(movieId: String): Resource<Unit?>
}