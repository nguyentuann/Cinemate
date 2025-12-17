package vn.tutorial.cinemate.domain.model

data class MemberModel(
    val id: String,
    val userId: String,
    val email: String,
    val isOwner: Boolean,
    val isKid: Boolean
)