package vn.tutorial.cinemate.domain.usecase.search

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.domain.model.CategoryModel
import vn.tutorial.cinemate.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) : BaseUseCase<Unit, Resource<List<CategoryModel>?>>() {
    override suspend fun execute(param: Unit): Resource<List<CategoryModel>?> {
        return categoryRepository.getCategories()
    }
}