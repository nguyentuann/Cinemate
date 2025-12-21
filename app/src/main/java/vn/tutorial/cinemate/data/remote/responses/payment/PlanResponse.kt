package vn.tutorial.cinemate.data.remote.responses.payment

import com.google.gson.annotations.SerializedName
import vn.tutorial.cinemate.domain.model.FeaturedSubscriptionPlanModel
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel

data class PlanResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("durationDays")
    val durationDays: Int,

    @SerializedName("maxDevices")
    val maxDevices: Int,

    @SerializedName("features")
    val features: FeaturesResponse,

    @SerializedName("isActive")
    val isActive: Boolean,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String
)

data class FeaturesResponse(
    @SerializedName("ad_free")
    val adFree: Boolean,

    @SerializedName("offline_download")
    val offlineDownload: Boolean,

    @SerializedName("hd_streaming")
    val hdStreaming: Boolean,

    @SerializedName("multiple_devices")
    val multipleDevices: Boolean,

    @SerializedName("family_sharing")
    val familySharing: Boolean = false
)

fun PlanResponse.toSubscriptionPlanModel(): SubscriptionPlanModel {
    return SubscriptionPlanModel(
        id = id,
        name = name,
        description = description,
        price = price,
        durationDays = durationDays,
        maxDevice = maxDevices,
        featured = FeaturedSubscriptionPlanModel(
            addFree = features.adFree,
            offlineDownload = features.offlineDownload,
            hdStreaming = features.hdStreaming,
            multipleDevices = features.multipleDevices,
            familySharing = features.familySharing
        )
    )
}



