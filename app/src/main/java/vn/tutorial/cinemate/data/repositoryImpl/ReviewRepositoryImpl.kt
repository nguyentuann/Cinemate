package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.responses.movie.toReviewModel
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.data.remote.services.MovieService
import vn.tutorial.cinemate.domain.model.ReviewModel
import vn.tutorial.cinemate.domain.model.toReviewRequest
import vn.tutorial.cinemate.domain.repository.ReviewRepository
import vn.tutorial.cinemate.mockdata.commentsData
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : ReviewRepository, BaseService() {
    override suspend fun getAllComments(filmId: String): Resource<List<ReviewModel>?> {
//        return safeApiCall {
//            movieService.getAllReviewsOfMovie(filmId)
//        }.mapData { wrapper ->
//            wrapper?.map {
//                it.toReviewModel()
//            }
//        }
        return Resource.Success(commentsData)
    }

    override suspend fun addComment(review: ReviewModel): Resource<ReviewModel?> {
        return safeApiCall {
            movieService.addReview(
                review.movieId,
                review.toReviewRequest()
            )
        }.mapData {
            wrapper -> wrapper?.toReviewModel()
        }
    }

    override suspend fun getReviewCount(filmId: String): Resource<Int> {
        return Resource.Success(10)
    }
}