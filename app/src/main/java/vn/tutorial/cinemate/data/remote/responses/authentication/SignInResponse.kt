package vn.tutorial.cinemate.data.remote.responses.authentication

import com.google.gson.annotations.SerializedName

data class DataWrapperSignIn(
    @SerializedName("user") val user: UserDto,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)