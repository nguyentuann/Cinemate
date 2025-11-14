package vn.tutorial.cinemate.domain.usecase.movies

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.MovieRepository
import javax.inject.Inject

class GetSectionMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetSectionMoviesUseCase.Params, Resource<List<MovieDetailModel>?>>() {

    data class Params(
        val section: String,
        val page: Int,
        val size: Int,
        val sortBy: String
    )

    override suspend fun execute(param: Params): Resource<List<MovieDetailModel>?> {
        return movieRepository.getMovies(param.page, param.size, param.sortBy)
    }
}