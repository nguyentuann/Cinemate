package vn.tutorial.cinemate.domain.usecase.favorite

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.FavoriteRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : BaseUseCase<String, Resource<Unit?>>() {
    override suspend fun execute(param: String): Resource<Unit?> {
        return favoriteRepository.deleteFavorite(param)
    }
}