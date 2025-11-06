package vn.tutorial.cinemate.data.remote.requests.film

import com.google.gson.annotations.SerializedName

data class FavoriteRequest(
    @SerializedName("movieId") val movieId: String,
)