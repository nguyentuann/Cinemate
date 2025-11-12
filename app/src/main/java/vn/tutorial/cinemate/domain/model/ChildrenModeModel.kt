package vn.tutorial.cinemate.domain.model

import vn.tutorial.cinemate.core.constant.enums.AgeLimit
import vn.tutorial.cinemate.core.constant.enums.TimeLimit

data class ChildrenModeModel(
    val isEnable: Boolean,
    val selectedAgeLimit: AgeLimit ,
    val selectedWatchTime: TimeLimit,
)