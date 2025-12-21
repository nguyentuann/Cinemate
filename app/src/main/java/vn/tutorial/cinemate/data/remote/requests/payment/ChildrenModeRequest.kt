package vn.tutorial.cinemate.data.remote.requests.payment

import com.google.gson.annotations.SerializedName

data class ChildrenModeRequest(
    @SerializedName("blockedCategoryIds")
    val blockedCategoryIds: List<String>,
    @SerializedName("watchTimeLimitMinutes")
    val watchTimeLimitMinutes: Int
)
