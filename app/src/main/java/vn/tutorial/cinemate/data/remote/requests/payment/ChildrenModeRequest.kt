package vn.tutorial.cinemate.data.remote.requests.payment

data class ChildrenModeRequest(
    val blockedCategoryIds: List<String>,
    val watchTimeLimitMinutes: Int
)
