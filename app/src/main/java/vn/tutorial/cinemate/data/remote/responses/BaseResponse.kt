package vn.tutorial.cinemate.data.remote.responses

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status") val status: String,
    @SerializedName("path") val path: String?,
    @SerializedName("message") val message: String?,  // nullable
    @SerializedName("method") val method: String?,
    @SerializedName("data") val data: T? = null,      // nullable
    @SerializedName("title") val title: String? = null,
    @SerializedName("detail") val detail: String
)
