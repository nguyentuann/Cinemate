package vn.tutorial.cinemate.data.remote.requests.authentication

import com.google.gson.annotations.SerializedName

data class SignOutRequest(
    @SerializedName("refresh_token") val refreshToken: String
)