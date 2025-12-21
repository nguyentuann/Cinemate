package vn.tutorial.cinemate.domain.usecase.movies

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.MovieRepository
import javax.inject.Inject

class ReportProgressUseCase @Inject constructor(
    private val movieRepository: MovieRepository
): BaseUseCase<ReportProgressUseCase.Param, Resource<Unit?>>() {
    data class Param(
        val movieId: String,
        val lastWatchedPosition: Int,
        val totalDuration: Int
    )

    override suspend fun execute(param: Param): Resource<Unit?> {
        return movieRepository.reportProgress(
            movieId = param.movieId,
            lastWatchedPosition = param.lastWatchedPosition,
            totalDuration = param.totalDuration
        )
    }
}