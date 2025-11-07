package vn.tutorial.cinemate.data.remote.requests.film

import com.google.gson.annotations.SerializedName

data class ReviewRequest(
    @SerializedName("content") val content: String?,
    @SerializedName("stars") val stars: Int?,
    @SerializedName("userName") val userName: String?,
    @SerializedName("userAvatar") val userAvatar: String?,
    @SerializedName("userId") val userId: String?
)
