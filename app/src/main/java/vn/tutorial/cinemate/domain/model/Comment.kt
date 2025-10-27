package vn.tutorial.cinemate.domain.model

data class Comment(
    val id: Int? = null,
    val userId: String,
    val userName: String,
    val stars: Int,
    val content: String,
    val timestamp: String
)