package vn.tutorial.cinemate.domain.usecase.favorite

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : BaseUseCase<Unit, Resource<List<MovieDetailModel>?>>() {

    override suspend fun execute(param: Unit): Resource<List<MovieDetailModel>?> {
        return favoriteRepository.getFavoriteMovies()
    }
}