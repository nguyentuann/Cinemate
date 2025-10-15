package vn.tutorial.cinemate.data.repositoryImpl

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.core.util.LogUtil
import vn.tutorial.cinemate.data.local.LocalStorage
import vn.tutorial.cinemate.data.remote.requests.authentication.ResetPasswordRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignInRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignOutRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.SignUpRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyEmailRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyOTPRequest
import vn.tutorial.cinemate.data.remote.requests.authentication.VerifyTokenRequest
import vn.tutorial.cinemate.data.remote.responses.authentication.toUserModel
import vn.tutorial.cinemate.data.remote.services.AuthService
import vn.tutorial.cinemate.data.remote.services.BaseService
import vn.tutorial.cinemate.domain.model.UserModel
import vn.tutorial.cinemate.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val localStorage: LocalStorage
) : AuthRepository, BaseService() {

    override suspend fun verifyToken(token: String): Resource<String?> {
        return safeApiCall {
            authService.verifyToken(
                VerifyTokenRequest(token = token)
            )
        }.mapData { wrapper -> wrapper?.email }
    }

    override suspend fun verifyEmail(email: String): Resource<Unit?> {
        return safeApiCall {
            authService.verifyEmail(VerifyEmailRequest(email = email))
        }
    }


    override suspend fun signUp(
        email: String,
        password: String,
        token: String
    ): Resource<UserModel?> {
        return safeApiCall {
            authService.signUp(
                SignUpRequest(
                    email = email,
                    password = password,
                    token = token
                )
            )
        }.mapData { wrapper ->
            wrapper?.user?.toUserModel()
        }
    }

    override suspend fun forgotPassword(email: String): Resource<String?> {
        return safeApiCall {
            authService.forgotPassword(VerifyEmailRequest(email = email))
        }
    }

    override suspend fun verifyOTP(email: String, otp: String): Resource<Boolean?> {
        return safeApiCall {
            LogUtil("goi verify otp trong repo")
            authService.verifyOTP(VerifyOTPRequest(email = email, otp = otp))
        }
    }

    override suspend fun resetPassword(
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
            localStorage.saveAccessToken(accessToken ?: "")
            localStorage.saveRefreshToken(refreshToken ?: "")

            wrapper?.user?.toUserModel()
        }
    }

    override suspend fun signOut(refreshToken: String): Resource<String?> {
        return safeApiCall {
            authService.signOut(
                SignOutRequest(refreshToken = refreshToken)
            )
        }
    }
}