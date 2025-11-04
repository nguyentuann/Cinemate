package vn.tutorial.cinemate.domain.usecase.movies

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.MovieDetailModel
import vn.tutorial.cinemate.domain.repository.MovieRepository
import javax.inject.Inject

class GetDetailMovieUseCase @Inject constructor(
    private val filmRepository: MovieRepository
): BaseUseCase<String, Resource<MovieDetailModel?>>() {
    override suspend fun execute(param: String): Resource<MovieDetailModel?> {
        return filmRepository.getDetailMovie(param)
    }
}