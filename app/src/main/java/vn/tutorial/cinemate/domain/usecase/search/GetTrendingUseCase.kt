package vn.tutorial.cinemate.domain.usecase.search

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.MovieRepository
import javax.inject.Inject

class GetTrendingUseCase @Inject constructor(
    private val filmRepository: MovieRepository
) : BaseUseCase<Unit, Resource<List<MovieDetailModel>?>>() {
    override suspend fun execute(param: Unit): Resource<List<MovieDetailModel>?> {
        return filmRepository.getMovies(1,1,"", "")
    }
}