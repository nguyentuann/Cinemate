package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.CategoryModel
import vn.tutorial.cinemate.domain.model.MovieDetailModel

interface CategoryRepository {
    suspend fun getCategories(): Resource<List<CategoryModel>?>

    suspend fun getMoviesByCategory(categoryId: String): Resource<List<MovieDetailModel>?>
}