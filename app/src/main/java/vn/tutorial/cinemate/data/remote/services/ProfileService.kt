package vn.tutorial.cinemate.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import vn.tutorial.cinemate.core.constant.api_endpoint.ProfileEndpoint
import vn.tutorial.cinemate.data.remote.requests.profile.ProfileRequest
import vn.tutorial.cinemate.data.remote.responses.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.profile.ProfileResponse

interface ProfileService {
    @GET(ProfileEndpoint.GET_PROFILE)
    suspend fun getProfile(): Response<BaseResponse<ProfileResponse>>

    @PATCH(ProfileEndpoint.UPDATE_PROFILE)
    suspend fun updateProfile(
        @Body profileRequest: ProfileRequest
    ): Response<BaseResponse<ProfileResponse>>
}