package vn.tutorial.cinemate.data.remote.requests.authentication

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("otp")
    val otp: String
)