package vn.tutorial.cinemate.data.remote.requests.authentication

import com.google.gson.annotations.SerializedName

data class VerifyEmailRequest (
    @SerializedName("email")
    val email: String
)