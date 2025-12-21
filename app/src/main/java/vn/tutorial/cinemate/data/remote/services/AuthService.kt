package vn.tutorial.cinemate.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import vn.tutorial.cinemate.core.constant.api_endpoint.AuthEndpoint
import vn.tutorial.cinemate.data.remote.requests.authentication.ChangePasswordRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.RefreshTokenRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.ResetPasswordRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignInRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignOutRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignUpRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyEmailRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyOTPRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyTokenRequest
import vn.tutorial.cinemate.data.remote.responses.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.authentication.DataWrapperSignIn
import vn.tutorial.cinemate.data.remote.responses.authentication.DataWrapperSignUp
import vn.tutorial.cinemate.data.remote.responses.authentication.RefreshTokenResponse
import vn.tutorial.cinemate.data.remote.responses.authentication.VerifyTokenResponse

interface AuthService {

    @POST(AuthEndpoint.VERIFY_EMAIL)
    suspend fun verifyEmail(
        @Body verifyEmailRequest: VerifyEmailRequest
    ): Response<BaseResponse<Unit>>

    @POST(AuthEndpoint.VERIFY_TOKEN)
    suspend fun verifyToken(
        @Body token: VerifyTokenRequest
    ): Response<BaseResponse<VerifyTokenResponse>>

    @POST(AuthEndpoint.SIGN_UP)
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Response<BaseResponse<DataWrapperSignUp>>

    @POST(AuthEndpoint.FORGOT_PASSWORD)
    suspend fun forgotPassword(
        @Body forgotPasswordRequest: VerifyEmailRequest
    ): Response<BaseResponse<String>>

    @POST(AuthEndpoint.VERIFY_OTP)
    suspend fun verifyOTP(
        @Body verifyOTPRequest: VerifyOTPRequest
    ): Response<BaseResponse<Boolean>>

    @POST(AuthEndpoint.RESET_PASSWORD)
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<BaseResponse<String>>

    @POST(AuthEndpoint.LOGIN)
    suspend fun signIn(
        @Body signInRequest: SignInRequest
    ): Response<BaseResponse<DataWrapperSignIn>>

    @POST(AuthEndpoint.LOGOUT)
    suspend fun signOut(
        @Body refreshToken: SignOutRequest
    ): Response<BaseResponse<String>>

    @PATCH(AuthEndpoint.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<BaseResponse<String>>


    @POST(AuthEndpoint.REFRESH_TOKEN)
    suspend fun refreshToken(
        @Body refreshToken: RefreshTokenRequest
    ): Response<BaseResponse<RefreshTokenResponse>>

}