package vn.tutorial.cinemate.data.remote.responses.payment

import com.google.gson.annotations.SerializedName

data class CurrentPlanResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val userId: String,
    @SerializedName("plan")
    val plan: PlanResponse,
)