package vn.tutorial.cinemate.data.remote.responses.payment

import com.google.gson.annotations.SerializedName

data class EmailResponse (
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("email")
    val email: String
)