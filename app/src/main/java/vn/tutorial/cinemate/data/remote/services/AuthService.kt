package vn.tutorial.cinemate.data.remote.services

import retrofit2.http.Body
import retrofit2.http.POST
import vn.tutorial.cinemate.core.constant.ApiEndpoints
import vn.tutorial.cinemate.data.remote.requests.SignUpRequest
import vn.tutorial.cinemate.data.remote.requests.VerifyOTPRequest
import vn.tutorial.cinemate.data.remote.responses.SignUpResponse
import vn.tutorial.cinemate.data.remote.responses.VerifyOTPResponse

interface AuthService {
    @POST(ApiEndpoints.SIGN_UP)
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): SignUpResponse

    @POST(ApiEndpoints.VERIFY_OTP)
    suspend fun verifyOTP(
        @Body verifyOTPRequest: VerifyOTPRequest
    ): VerifyOTPResponse
}