package vn.tutorial.cinemate.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import vn.tutorial.cinemate.core.constant.api_endpoint.CategoryEndpoint
import vn.tutorial.cinemate.core.constant.api_endpoint.MovieEndpoint
import vn.tutorial.cinemate.data.remote.requests.film.ReviewRequest
import vn.tutorial.cinemate.data.remote.requests.film.SearchRequest
import vn.tutorial.cinemate.data.remote.responses.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.movie.CategoryResponse
import vn.tutorial.cinemate.data.remote.responses.movie.MovieResponse
import vn.tutorial.cinemate.data.remote.responses.movie.ReviewResponse

interface MovieService {
    // todo about search films

    @GET(MovieEndpoint.SEARCH_FILMS)
    suspend fun searchFilms(
        @Query("keyword") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
    ): Response<BaseResponse<List<MovieResponse>>>

    @GET(CategoryEndpoint.GET_ALL_CATEGORIES)
    suspend fun getCategory(): Response<BaseResponse<List<CategoryResponse>>>

    @GET(CategoryEndpoint.GET_MOVIES_BY_CATEGORY)
    suspend fun getMoviesByCategory(
        @Path("categoryId") categoryId: String
    ): Response<BaseResponse<List<MovieResponse>>>

    // todo about reviews
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

    @DELETE(MovieEndpoint.DELETE_REVIEW)
    suspend fun deleteReview(
        @Path("movieId") movieId: String,
        @Path("reviewId") reviewId: String,
        @Query("userId") userId: String
    ): Response<BaseResponse<Unit>>

    // todo about movies
    @GET(MovieEndpoint.GET_MOVIES)
    suspend fun getMovies(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("sortBy") sortBy: String = "year",
        @Query("sortDirection") sortDirection: String = "asc"
    ): Response<BaseResponse<List<MovieResponse>>>

    @GET(MovieEndpoint.GET_MOVIE_BY_ID)
    suspend fun getMovieById(
        @Path("movieId") movieId: String
    ): Response<BaseResponse<MovieResponse>>
}