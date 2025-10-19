package vn.tutorial.cinemate.data.remote.responses.authentication

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.domain.model.UserModel

data class DataWrapperSignUp(
    @SerializedName("user") val user: UserDto
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("is_enabled") val isEnabled: Boolean
)

fun UserDto.toUserModel() = UserModel(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    isEnabled = isEnabled
)

