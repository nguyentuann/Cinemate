package vn.tutorial.cinemate.data.remote.responses.payment

import com.google.gson.annotations.SerializedName

data class CreatePlanResponse(
    @SerializedName("subscriptionId")
    val subscriptionId: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("plan")
    val plan: PlanResponse,

    @SerializedName("status")
    val status: String,

    @SerializedName("autoRenew")
    val autoRenew: Boolean,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("paymentId")
    val paymentId: String,

    @SerializedName("paymentUrl")
    val paymentUrl: String,

    @SerializedName("vnpTxnRef")
    val vnpTxnRef: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("message")
    val message: String
)
