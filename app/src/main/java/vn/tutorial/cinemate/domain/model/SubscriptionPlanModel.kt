package vn.tutorial.cinemate.domain.model

data class SubscriptionPlanModel(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val durationDays: Int,
    val maxDevice: Int,
    val featured: FeaturedSubscriptionPlanModel
)

data class FeaturedSubscriptionPlanModel(
   val addFree: Boolean,
    val offlineDownload: Boolean,
   val hdStreaming: Boolean,
   val multipleDevices: Boolean,
   val familySharing: Boolean,
)
