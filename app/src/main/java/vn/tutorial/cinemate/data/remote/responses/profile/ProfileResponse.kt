package vn.tutorial.cinemate.data.remote.responses.profile

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.domain.model.ProfileModel

data class ProfileResponse(
    @SerializedName("id") val id: String,
    @SerializedName("account_id") val accountId: String,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("gender") val gender: String?,
)

fun ProfileResponse.toProfileModel() = ProfileModel(
    id = this.id,
    accountId = this.accountId,
    firstName = this.firstName,
    lastName = this.lastName,
    dateOfBirth = this.dateOfBirth,
    avatarUrl = this.avatarUrl,
    gender = gender
)