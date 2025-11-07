package vn.tutorial.cinemate.domain.usecase.reviews

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ReviewModel
import vn.tutorial.cinemate.domain.repository.ReviewRepository
import javax.inject.Inject

class AddReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) : BaseUseCase<ReviewModel, Resource<ReviewModel?>>() {

    override suspend fun execute(param: ReviewModel): Resource<ReviewModel?> {
        return reviewRepository.addReview(param)
    }
}