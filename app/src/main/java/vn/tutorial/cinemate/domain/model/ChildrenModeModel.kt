package vn.tutorial.cinemate.domain.model

import vn.tutorial.cinemate.core.constant.enums.TimeLimit

data class ChildrenModeModel(
    val blockedCategoryIds: List<String> ,
    val watchTimeLimitMinutes: TimeLimit,
)