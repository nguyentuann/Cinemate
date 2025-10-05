package vn.tutorial.cinemate.data.remote.responses.authentication

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: T? = null,      // nullable
    @SerializedName("message") val message: String? = null,  // nullable
    @SerializedName("path") val path: String? = null,
    @SerializedName("method") val method: String? = null,

    // khi có lỗi từ server
    @SerializedName("title") val title: String? = null,
    @SerializedName("detail") val detail: String? = null,
    @SerializedName("errors") val errors: List<ApiError>? = null // nullable

)

data class ApiError(
    @SerializedName("message") val message: String?,
    @SerializedName("field") val field: String?,
    @SerializedName("code") val code: String?
)

fun <T> BaseResponse<T>.toMyString(): String {
    return "BaseResponse(status='$status', data=$data, message=$message, path=$path, method=$method, title=$title, detail=$detail, errors=$errors)"
}
