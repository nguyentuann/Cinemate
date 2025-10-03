package vn.tutorial.cinemate.data.remote.services

import retrofit2.http.Body
import retrofit2.http.POST
import vn.tutorial.cinemate.core.constant.ApiEndpoints
import vn.tutorial.cinemate.data.remote.requests.authentication.ResetPasswordRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignUpRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyEmailRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyOTPRequest
import vn.tutorial.cinemate.data.remote.responses.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.authentication.SignUpResponse

interface AuthService {
    @POST(ApiEndpoints.SIGN_UP)
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): SignUpResponse

    @POST(ApiEndpoints.VERIFY_OTP)
    suspend fun verifyOTP(
        @Body verifyOTPRequest: VerifyOTPRequest
    ): BaseResponse<Boolean>


    @POST(ApiEndpoints.FORGOT_PASSWORD)
    suspend fun forgotPassword(
        @Body forgotPasswordRequest: VerifyEmailRequest
    ): BaseResponse<String>

    @POST(ApiEndpoints.RESET_PASSWORD)
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): BaseResponse<String>

}