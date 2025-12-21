package vn.tutorial.cinemate.data.remote.requests.film

import com.google.gson.annotations.SerializedName

data class ReportProgressRequest(
    @SerializedName("lastWatchedPosition")
    val lastWatchedPosition: Int,
    @SerializedName("totalDuration")
    val totalDuration: Int
)