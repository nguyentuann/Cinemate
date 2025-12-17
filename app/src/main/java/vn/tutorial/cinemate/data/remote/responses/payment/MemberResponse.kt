package vn.tutorial.cinemate.data.remote.responses.payment

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.domain.model.MemberModel

data class MemberResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("isOwner")
    val isOwner: Boolean,
    @SerializedName("isKid")
    val isKid: Boolean
)

fun MemberResponse.toMemberModel(): MemberModel {
    return MemberModel(
        id = this.id,
        userId = this.userId,
        email = this.email,
        isOwner = this.isOwner,
        isKid = this.isKid
    )
}