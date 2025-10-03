package vn.tutorial.cinemate.data.repositoryImpl

import coil.network.HttpException
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.local.TokenStorage
import vn.tutorial.cinemate.data.remote.requests.authentication.ResetPasswordRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignInRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignUpRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyEmailRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyOTPRequest
import vn.tutorial.cinemate.data.remote.responses.authentication.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.authentication.toUserModel
import vn.tutorial.cinemate.data.remote.services.AuthService
import vn.tutorial.cinemate.domain.model.UserModel
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    fun <T, R> Resource<T>.mapData(transform: (T?) -> R?): Resource<R?> {
        return when (this) {
            is Resource.Success -> Resource.Success(transform(data))
            is Resource.Error -> Resource.Error(message)
            is Resource.Loading -> Resource.Loading
        }
    }

    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> BaseResponse<T>
    ): Resource<T?> {
        return try {
            val response = apiCall()
            if (response.status == "success") {
                Resource.Success(response.data)
            } else {
                Resource.Error(response.detail ?: "An unexpected error occurred")
            }
        } catch (e: HttpException) {
            LogUtil("Lot vao safeApiCall 1")
            Resource.Error(e.message ?: "An unexpected error occurred")
        } catch (e: Exception) {
            LogUtil("Lot vao safeApiCall 2")
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun signUp(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
        passwordConfirm: String
    ): Resource<UserModel?> {
        return safeApiCall {
            authService.signUp(
                SignUpRequest(
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    password = password,
                    passwordConfirm = passwordConfirm
                )
            )
        }.mapData { wrapper ->
            wrapper?.user?.toUserModel()
        }
    }

    override suspend fun verifyOTP(email: String, otp: String): Resource<Boolean?> {
        return safeApiCall {
            authService.verifyOTP(VerifyOTPRequest(email = email, otp = otp))
        }
    }

    override suspend fun verifyEmail(email: String): Resource<String?> {
        return safeApiCall {
            authService.forgotPassword(VerifyEmailRequest(email = email))
        }
    }

    override suspend fun forgotPassword(
        email: String,
        otp: String,
        newPassword: String
    ): Resource<String?> {
        return safeApiCall {
            authService.resetPassword(
                ResetPasswordRequest(
                    email = email,
                    otp = otp,
                    newPassword = newPassword
                )
            )
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Resource<UserModel?> {
        return safeApiCall {
            authService.signIn(
                SignInRequest(
                    email = email,
                    password = password
                )
            )
        }.mapData { wrapper ->
            val accessToken = wrapper?.accessToken
            val refreshToken = wrapper?.refreshToken
            tokenStorage.saveAccessToken(accessToken ?: "")
            tokenStorage.saveRefreshToken(refreshToken ?: "")

            wrapper?.user?.toUserModel()
        }
    }
}