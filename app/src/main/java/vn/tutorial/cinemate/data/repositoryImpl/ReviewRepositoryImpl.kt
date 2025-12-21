package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.local.LocalStorage
import vn.tutorial.cinemate.data.remote.responses.movie.toReviewModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.MovieService
import vn.tutorial.cinemate.domain.model.ReviewModel
import vn.tutorial.cinemate.domain.model.toReviewRequest
import vn.tutorial.cinemate.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val localStorage: LocalStorage
) : ReviewRepository, BaseService() {

    override suspend fun getAllReviews(movieId: String): Resource<List<ReviewModel>?> {
        return safeApiCall {
            movieService.getAllReviewsOfMovie(movieId)
        }.mapData { wrapper ->
            wrapper?.map {
                val isUser = it.customerId == localStorage.getUserId()
                it.toReviewModel().copy(
                    isUser = isUser
                )
            }
        }
    }

    override suspend fun addReview(review: ReviewModel): Resource<ReviewModel?> {
        val rq = review.toReviewRequest().copy(
            userId = localStorage.getUserId() ?: "",
            userName = localStorage.getUserName() ?: "Anonymous User 2",
            userAvatar = localStorage.getUserAvatar() ?: "Anonymous Avatar 2" ,
        )

        LogUtil(rq.toString())
        return safeApiCall {
            movieService.addReview(
                review.movieId,
                rq

            )
        }.mapData { wrapper ->
            val isUser = wrapper?.customerId == localStorage.getUserId()
            wrapper?.toReviewModel()?.copy(
                isUser = isUser
            )
        }
    }

    override suspend fun getReviewCount(movieId: String): Resource<Int?> {
        return safeApiCall {
            movieService.getReviewCount(movieId)
        }.mapData {
            it ?: 0
        }
    }

    override suspend fun deleteReview(
        reviewId: String,
        movieId: String,
        userId: String
    ): Resource<Unit?> {
        return safeApiCall {
            movieService.deleteReview(movieId, reviewId, userId)
        }
    }
}