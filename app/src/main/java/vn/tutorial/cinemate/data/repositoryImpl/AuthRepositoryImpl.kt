package vn.tutorial.cinemate.data.repositoryImpl

import android.util.Log
import coil.network.HttpException
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.requests.authentication.ResetPasswordRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignUpRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyEmailRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyOTPRequest
import vn.tutorial.cinemate.data.remote.responses.BaseResponse
import vn.tutorial.cinemate.data.remote.responses.authentication.toUserModel
import vn.tutorial.cinemate.data.remote.services.AuthService
import vn.tutorial.cinemate.domain.model.UserModel
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> BaseResponse<T>
    ): Resource<T?> {
        return try {
            val response = apiCall()
            if (response.status == "success") {
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message ?: "An unexpected error occurred")
            }
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun signUp(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
        passwordConfirm: String
    ): Resource<UserModel> {
        try {
            Log.d("SignUp", "Call sign up api")
            val response = authService.signUp(
                SignUpRequest(
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    password = password,
                    passwordConfirm = passwordConfirm
                )
            )

            val user = response.data.user.toUserModel()

            return Resource.Success(user)
        } catch (e: HttpException) {
            // todo xử lý các lỗi từ server
            Log.d("SignUp", "Rơi vào catch 1 ")
            return Resource.Error(e.message ?: "An unexpected error occurred")
        } catch (e: Exception) {
            Log.d("SignUp", "Rơi vào catch 2")
            return Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun verifyOTP(
        email: String,
        otp: String
    ): Resource<Boolean?> {
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
}