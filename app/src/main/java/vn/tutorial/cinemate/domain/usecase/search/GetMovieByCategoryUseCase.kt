package vn.tutorial.cinemate.domain.usecase.search

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.CategoryRepository
import javax.inject.Inject

class GetMovieByCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) : BaseUseCase<String, Resource<List<MovieDetailModel>?>>() {
    override suspend fun execute(param: String): Resource<List<MovieDetailModel>?> {
        return categoryRepository.getMoviesByCategory(param)
    }
}
