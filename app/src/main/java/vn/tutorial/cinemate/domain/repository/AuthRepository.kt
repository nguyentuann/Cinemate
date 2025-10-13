package vn.tutorial.cinemate.domain.repository

import vn.tutorial.cinemate.core.base_class.Resource
import vn.tutorial.cinemate.domain.model.UserModel
import vn.tutorial.cinemate.navigation.Route

interface AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        token: String
    ): Resource<UserModel?>

    suspend fun verifyOTP(
        email: String,
        otp: String
    ): Resource<Boolean?>

    suspend fun verifyEmail(
        email: String
    ): Resource<Unit?>

    suspend fun forgotPassword(
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

    suspend fun verifyToken(
        token: String
    ): Resource<String?>
}