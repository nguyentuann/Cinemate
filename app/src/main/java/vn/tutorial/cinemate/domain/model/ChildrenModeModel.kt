package vn.tutorial.cinemate.domain.model

import vn.tutorial.cinemate.core.constant.enum.AgeLimit
import vn.tutorial.cinemate.core.constant.enum.TimeLimit

data class ChildrenModeModel(
    val isEnable: Boolean,
    val selectedAgeLimit: AgeLimit ,
    val selectedWatchTime: TimeLimit,
)