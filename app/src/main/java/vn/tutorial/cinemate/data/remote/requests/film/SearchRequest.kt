package vn.tutorial.cinemate.data.remote.requests.film

import com.google.gson.annotations.SerializedName

data class SearchRequest(
    @SerializedName("query") val query: String,
)