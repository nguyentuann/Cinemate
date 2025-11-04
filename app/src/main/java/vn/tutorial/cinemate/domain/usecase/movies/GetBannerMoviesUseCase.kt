package vn.tutorial.cinemate.domain.usecase.movies

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.MovieRepository
import javax.inject.Inject

class GetBannerMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) :
    BaseUseCase<GetBannerMoviesUseCase.Params, Resource<List<MovieDetailModel>?>>() {

    data class Params(
        val page: Int,
        val size: Int,
        val sortBy: String
    )

    override suspend fun execute(param: Params): Resource<List<MovieDetailModel>?> {
        return movieRepository.getMovies(
            page = param.page,
            size = param.size,
            sortBy = param.sortBy
        )
    }
}