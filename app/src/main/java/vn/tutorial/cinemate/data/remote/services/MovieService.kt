package vn.tutorial.cinemate.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import vn.tutorial.cinemate.core.constant.api_endpoint.MovieEndpoint
import vn.tutorial.cinemate.data.remote.requests.film.ReviewRequest
import vn.tutorial.cinemate.data.remote.requests.film.SearchRequest
import vn.tutorial.cinemate.data.remote.responses.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.film.ReviewResponse

interface MovieService {
    suspend fun searchFilms(
        @Body query: SearchRequest
    ): Response<BaseResponse<String>>

    @GET(MovieEndpoint.GET_REVIEWS_OF_MOVIE)
    suspend fun getAllReviewsOfMovie(
        @Path("movieId") movieId: String
    ): Response<BaseResponse<List<ReviewResponse>>>

    @POST(MovieEndpoint.CREATE_REVIEW)
    suspend fun addReview(
        @Path("movieId") movieId: String,
        @Body reviewRequest: ReviewRequest
    ): Response<BaseResponse<ReviewResponse>>

    @GET(MovieEndpoint.GET_REVIEW_COUNT)
    suspend fun getReviewCount(
        @Path("movieId") movieId: String
    ): Response<BaseResponse<Int>>

}