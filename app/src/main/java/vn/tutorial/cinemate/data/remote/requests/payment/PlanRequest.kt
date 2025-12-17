package vn.tutorial.cinemate.data.remote.requests.payment

import com.google.gson.annotations.SerializedName

data class PlanRequest (
    @SerializedName("planId") val planId: String,
    @SerializedName("autoRenew") val autoRenew: Boolean
)