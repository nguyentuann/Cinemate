package vn.tutorial.cinemate.data.remote.responses

import com.google.gson.annotations.SerializedName

data class VerifyOTPResponse (
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("path") val path: String,
    @SerializedName("method") val method: String
)