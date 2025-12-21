package vn.tutorial.cinemate.data.remote.responses.movie

import com.google.gson.annotations.SerializedName

data class ProgressResponse(
    @SerializedName("lastWatchedPosition") val lastWatchedPosition: Int,
    @SerializedName("totalDuration") val totalDuration: Int
)