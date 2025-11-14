package vn.tutorial.cinemate.data.remote.requests.profile

import com.google.gson.annotations.SerializedName

data class ProfileRequest (
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("gender") val gender: String?,
)