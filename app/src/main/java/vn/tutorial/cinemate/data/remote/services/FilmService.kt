package vn.tutorial.cinemate.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import vn.tutorial.cinemate.core.constant.ApiEndpoints
import vn.tutorial.cinemate.data.remote.requests.film.SearchRequest
import vn.tutorial.cinemate.data.remote.responses.authentication.BaseResponse

interface FilmService {
    suspend fun searchFilms(
       @Body query: SearchRequest
    ): Response<BaseResponse<String>>
}