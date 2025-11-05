package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.remote.responses.movie.toMovieDetailModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.MovieService
import vn.tutorial.cinemate.domain.model.CategoryModel
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : CategoryRepository, BaseService() {

    override suspend fun getCategories(): Resource<List<CategoryModel>?> {
//        return safeApiCall {
//            movieService.getCategory()
//        }.mapData { wrapper ->
//            wrapper?.map {
//                it.toCategoryModel()
//            }
//        }
        LogUtil("call get categories from fake data")
        return Resource.Success(
            listOf(
                CategoryModel("Action"),
                CategoryModel("Comedy"),
                CategoryModel("Drama"),
                CategoryModel("Horror"),
                CategoryModel("Romance"),
                CategoryModel("Sci-Fi"),
                CategoryModel("Documentary")
            )
        )
    }

    override suspend fun getMoviesByCategory(categoryId: String): Resource<List<MovieDetailModel>?> {
        return safeApiCall {
            movieService.getMoviesByCategory(categoryId)
        }.mapData { wrapper ->
            wrapper?.map {
                it.toMovieDetailModel()
            }
        }
    }
}