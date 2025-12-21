package vn.tutorial.cinemate.domain.usecase.movies

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.MovieRepository
import javax.inject.Inject

class GetTop10MoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<Unit, Resource<List<MovieDetailModel>?>>() {
    override suspend fun execute(param: Unit): Resource<List<MovieDetailModel>?> {
        return movieRepository.getTop10Movies()
    }
}