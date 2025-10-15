package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.UserModel

interface AuthRepository {

    suspend fun verifyEmail(
        email: String
    ): Resource<Unit?>

    suspend fun verifyToken(
        token: String
    ): Resource<String?>

    suspend fun signUp(
        email: String,
        password: String,
        token: String
    ): Resource<UserModel?>

    suspend fun forgotPassword(
        email: String
    ): Resource<String?>

    suspend fun verifyOTP(
        email: String,
        otp: String
    ): Resource<Boolean?>

    suspend fun resetPassword(
        email: String,
        otp: String,
        newPassword: String
    ): Resource<String?>

    suspend fun signIn(
        email: String,
        password: String
    ): Resource<UserModel?>

    suspend fun signOut(
        refreshToken: String
    ): Resource<String?>

}