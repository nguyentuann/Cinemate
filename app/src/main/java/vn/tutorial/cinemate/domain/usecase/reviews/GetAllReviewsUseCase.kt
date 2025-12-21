package vn.tutorial.cinemate.domain.usecase.reviews

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ReviewModel
import vn.tutorial.cinemate.domain.repository.ReviewRepository
import javax.inject.Inject

class GetAllReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) : BaseUseCase<String, Resource<List<ReviewModel>?>>() {
    override suspend fun execute(param: String): Resource<List<ReviewModel>?> {
        return reviewRepository.getAllReviews(param)
    }
}