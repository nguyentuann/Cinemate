package vn.tutorial.cinemate.data.remote.requests.payment

import com.google.gson.annotations.SerializedName

data class InviteMemberRequest(
    @SerializedName("recipientEmail")
    val email: String,
    @SerializedName("mode")
    val mode: String,
    @SerializedName("sendEmail")
    val sendEmail: Boolean = true
)

data class AcceptInvitationRequest(
    val invitationToken: String
)
