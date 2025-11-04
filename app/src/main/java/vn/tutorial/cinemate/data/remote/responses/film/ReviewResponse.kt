package vn.tutorial.cinemate.data.remote.responses.film

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.domain.model.ReviewModel

data class ReviewResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("movieId")
    val movieId: String,

    @SerializedName("userId")
    val userId: String,

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
    customerId = userId,
    createAt = createdAt,
    updateAt = updatedAt
)