package vn.tutorial.cinemate.data.remote.requests.film

import com.google.gson.annotations.SerializedName

data class ReportProgressRequest(
    @SerializedName("lastWatchedPosition")
    val lastWatchedPosition: Long,
    @SerializedName("totalDuration")
    val totalDuration: Long
)