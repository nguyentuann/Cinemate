package vn.tutorial.cinemate.data.remote.services

import retrofit2.http.Body
import retrofit2.http.POST
import vn.tutorial.cinemate.core.constant.ApiEndpoints
import vn.tutorial.cinemate.data.remote.requests.authentication.ResetPasswordRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignInRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignUpRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyEmailRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyOTPRequest
import vn.tutorial.cinemate.data.remote.responses.authentication.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.authentication.DataWrapperSignIn
import vn.tutorial.cinemate.data.remote.responses.authentication.DataWrapperSignUp

interface AuthService {
    @POST(ApiEndpoints.SIGN_UP)
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): BaseResponse<DataWrapperSignUp>

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

    @POST(ApiEndpoints.LOGIN)
    suspend fun signIn(
        @Body signInRequest: SignInRequest
    ): BaseResponse<DataWrapperSignIn>
}