package vn.tutorial.cinemate.domain.model

data class UserModel(
    val id: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val isEnabled: Boolean
)
