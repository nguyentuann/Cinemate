package vn.tutorial.cinemate.data.remote.responses.authentication

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: T? = null,      // nullable
    @SerializedName("message") val message: String?,  // nullable
    @SerializedName("path") val path: String?,
    @SerializedName("method") val method: String?,


    // khi có lỗi từ server
    @SerializedName("title") val title: String? = null,
    @SerializedName("detail") val detail: String? = null,
    @SerializedName("errors") val errors: List<Error>? = null // nullable

)

data class Error(
    @SerializedName("message") val message: String?,
    @SerializedName("field") val field: String?,
    @SerializedName("code") val code: String?
)