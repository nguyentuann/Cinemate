package vn.tutorial.cinemate.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import vn.tutorial.cinemate.core.constant.api_endpoint.FavoriteEndpoint
import vn.tutorial.cinemate.data.remote.requests.film.FavoriteRequest
import vn.tutorial.cinemate.data.remote.responses.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.movie.MovieResponse

interface FavoriteService {
    // todo about favorites
    @GET(FavoriteEndpoint.GET_FAVORITES_OF_USER)
    suspend fun getFavoriteMovies(): Response<BaseResponse<List<MovieResponse>>>

    @POST(FavoriteEndpoint.ADD_FAVORITE)
    suspend fun addFavoriteMovie(
        @Body movieId: FavoriteRequest
    ): Response<BaseResponse<Unit>>

    @DELETE(FavoriteEndpoint.DELETE_FAVORITE)
    suspend fun deleteFavoriteMovie(
        @Path("movieId") movieId: String
    ): Response<BaseResponse<Unit>>
}