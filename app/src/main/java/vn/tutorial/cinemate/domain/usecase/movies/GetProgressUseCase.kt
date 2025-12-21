package vn.tutorial.cinemate.domain.usecase.movies

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.MovieRepository
import javax.inject.Inject

class GetProgressUseCase @Inject constructor(
    private val movieRepository: MovieRepository
): BaseUseCase<String, Resource<Int?>>() {

    override suspend fun execute(param: String): Resource<Int?> {
        return movieRepository.getProgress(
            movieId = param,
        )
    }
}