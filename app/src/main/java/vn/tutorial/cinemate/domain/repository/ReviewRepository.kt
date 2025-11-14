package vn.tutorial.cinemate.domain.repository

import kotlinx.serialization.StringFormat
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ReviewModel

interface ReviewRepository {
    suspend fun getAllReviews(movieId: String): Resource<List<ReviewModel>?>
    suspend fun addReview(
        review: ReviewModel
    ): Resource<ReviewModel?>

    suspend fun getReviewCount(movieId: String): Resource<Int?>

    suspend fun deleteReview(reviewId: String, movieId: String, userId: String): Resource<Unit?>

}