package vn.tutorial.cinemate.data.remote.responses.payment

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.data.remote.responses.movie.CategoryResponse

data class ChildrenModeResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("parentId")
    val parentId: String,

    @SerializedName("kidId")
    val kidId: String,

    @SerializedName("subscriptionId")
    val subscriptionId: String,

    @SerializedName("blockedCategories")
    val blockedCategories: List<CategoryResponse>,

    @SerializedName("watchTimeLimitMinutes")
    val watchTimeLimitMinutes: Int,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String
)
