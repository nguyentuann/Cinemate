package vn.tutorial.cinemate.data.remote.requests

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("password_confirmation")
    val passwordConfirm: String,
)

