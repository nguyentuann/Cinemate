package vn.tutorial.cinemate.data.remote.responses.authentication

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.domain.model.UserModel

//data class SignUpResponse(
//    @SerializedName("status") val status: String,
//    @SerializedName("data") val data: DataWrapperSignUp,
//    @SerializedName("message") val message: String,
//    @SerializedName("path") val path: String,
//    @SerializedName("method") val method: String
//)

data class DataWrapperSignUp(
    @SerializedName("user") val user: UserDto
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("is_enabled") val isEnabled: Boolean
)

fun UserDto.toUserModel() = UserModel(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    isEnabled = isEnabled
)

