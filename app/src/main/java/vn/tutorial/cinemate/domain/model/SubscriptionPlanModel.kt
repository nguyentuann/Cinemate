package vn.tutorial.cinemate.domain.model

data class SubscriptionPlanModel(
    val id: String,
    val name: String,
    val resolution: String,
    val price: Int,
    val quality: String,
    val sound: String,
    val supportedDevices: String,
    val simultaneousDevices: Int,
    val downloadDevices: Int,
)
