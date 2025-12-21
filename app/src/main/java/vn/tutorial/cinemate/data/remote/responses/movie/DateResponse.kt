package vn.tutorial.cinemate.data.remote.responses.movie

import com.google.gson.annotations.SerializedName

data class DateResponse(
    @SerializedName("date")
    val date: String,
    @SerializedName("count")
    val count: Int

)