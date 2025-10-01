package vn.tutorial.cinemate.domain.model

data class UserModel(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val isEnabled: Boolean
)
