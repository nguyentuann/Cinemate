package vn.tutorial.cinemate.domain.model

import vn.tutorial.cinemate.data.remote.requests.film.ReviewRequest

data class ReviewModel(
    val id: String? = null,
    val movieId: String,
    val customerId: String,
    val userName: String,
    val userAvatar: String,
    val stars: Int,
    val content: String,
    val createAt: String?=null,
    val updateAt: String?=null
)

fun ReviewModel.toReviewRequest(): ReviewRequest {
    return ReviewRequest(
        content = this.content,
        stars = this.stars,
        userName = this.userName,
        userAvatar = this.userAvatar,
        userId = this.customerId
    )
}
