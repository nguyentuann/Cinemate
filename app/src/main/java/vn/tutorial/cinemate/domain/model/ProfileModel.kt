package vn.tutorial.cinemate.domain.model

import vn.tutorial.cinemate.data.remote.requests.profile.ProfileRequest

data class ProfileModel(
    val id: String? = null,
    val accountId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: String?=null,
    val avatarUrl: String?=null,
    val gender: String?=null,
)

fun ProfileModel.toProfileRequest() = ProfileRequest(
    firstName = this.firstName,
    lastName = this.lastName,
    avatarUrl = this.avatarUrl,
    gender = this.gender,
    dateOfBirth = this.dateOfBirth
)