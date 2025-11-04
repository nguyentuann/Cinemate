package vn.tutorial.cinemate.domain.usecase.reviews

import vn.tutorial.cinemate.core.base_class.BaseUseCase
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.repository.ReviewRepository
import javax.inject.Inject

class GetReviewCountUseCase @Inject constructor(
    private val commentRepository: ReviewRepository)
    : BaseUseCase<String, Resource<Int>>() {
    override suspend fun execute(param: String): Resource<Int> {
        return commentRepository.getReviewCount(param)
    }
}