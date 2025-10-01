package vn.tutorial.cinemate.data.repositoryImpl

import android.util.Log
import coil.network.HttpException
import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.data.remote.requests.SignUpRequest
import vn.tutorial.cinemate.data.remote.requests.VerifyOTPRequest
import vn.tutorial.cinemate.data.remote.responses.toUserModel
import vn.tutorial.cinemate.data.remote.services.AuthService
import vn.tutorial.cinemate.domain.model.UserModel
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {
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
    ): Resource<Boolean> {
        try {
            val response = authService.verifyOTP(
                VerifyOTPRequest(
                    email = email,
                    otp = otp
                )
            )
            return if (response.status == "success") {
                Resource.Success(true)
            } else {
                Resource.Error(response.message)
            }
        } catch (e: HttpException) {
            // todo xử lý các lỗi từ server
            return Resource.Error(e.message ?: "An unexpected error occurred")
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }
}