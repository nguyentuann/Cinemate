package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.ReviewModel

interface ReviewRepository {
    suspend fun getAllComments(filmId: String): Resource<List<ReviewModel>?>
    suspend fun addComment(
        review: ReviewModel
    ): Resource<ReviewModel?>

    suspend fun getReviewCount(filmId: String): Resource<Int>

}