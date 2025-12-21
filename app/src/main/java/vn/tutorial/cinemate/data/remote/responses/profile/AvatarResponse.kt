package vn.tutorial.cinemate.data.remote.responses.profile

import com.google.gson.annotations.SerializedName

data class AvatarResponse(
    @SerializedName("image_url") val imageUrl: String?
)