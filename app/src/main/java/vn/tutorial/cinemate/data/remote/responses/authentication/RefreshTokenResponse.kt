package vn.tutorial.cinemate.data.remote.responses.authentication

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("access_token") val accessToken: String,
)
