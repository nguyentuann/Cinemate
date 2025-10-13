package vn.tutorial.cinemate.data.remote.requests.authentication

import com.google.gson.annotations.SerializedName

data class VerifyTokenRequest(
    @SerializedName("token") val token: String
)