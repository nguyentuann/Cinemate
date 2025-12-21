package vn.tutorial.cinemate.data.remote.responses.authentication

import com.google.gson.annotations.SerializedName


data class VerifyTokenResponse(
    @SerializedName("email") val email: String
)

