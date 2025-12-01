package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel

interface MovieRepository {
    suspend fun searchFilms(query: String, page: Int, size: Int): Resource<List<MovieDetailModel>?>

    suspend fun getMovies(page: Int, size: Int, sortBy: String, sortDirection: String): Resource<List<MovieDetailModel>?>

    suspend fun getDetailMovie(movieId: String): Resource<MovieDetailModel?>
}