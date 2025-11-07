package vn.tutorial.cinemate.domain.usecase.reviews

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.ReviewRepository
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) : BaseUseCase<DeleteReviewUseCase.Params, Resource<Unit?>>() {

    data class Params(
        val movieId: String,
        val reviewId: String,
        val userId: String
    )

    override suspend fun execute(param: Params): Resource<Unit?> {
        return reviewRepository.deleteReview(
            movieId = param.movieId,
            reviewId = param.reviewId,
            userId = param.userId
        )
    }
}