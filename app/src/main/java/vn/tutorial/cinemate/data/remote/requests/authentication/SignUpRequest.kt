package vn.tutorial.cinemate.data.remote.requests.authentication

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("token")
    val token: String,
)