package vn.tutorial.cinemate.data.remote.responses.movie

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.domain.model.ReviewModel

data class ReviewResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("movieId")
    val movieId: String,

    @SerializedName("customerId")
    val customerId: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("stars")
    val stars: Int,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("userAvatar")
    val userAvatar: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String
)

fun ReviewResponse.toReviewModel() = ReviewModel(
    id = id,
    movieId = movieId,
    content = content,
    stars = stars,
    userName = userName,
    userAvatar = userAvatar,
    customerId = customerId,
    createAt = createdAt,
    updateAt = updatedAt
)