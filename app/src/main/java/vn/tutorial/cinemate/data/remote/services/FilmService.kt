package vn.tutorial.cinemate.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import vn.tutorial.cinemate.data.remote.requests.film.SearchRequest
import vn.tutorial.cinemate.data.remote.responses.BaseResponse

interface FilmService {
    suspend fun searchFilms(
       @Body query: SearchRequest
    ): Response<BaseResponse<String>>

    suspend fun getHomeFilms(): Response<BaseResponse<String>>


}