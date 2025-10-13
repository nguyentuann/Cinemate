package vn.tutorial.cinemate.data.remote.responses.authentication

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.domain.model.UserModel


data class VerifyTokenResponse(
    @SerializedName("email") val email: String
)

