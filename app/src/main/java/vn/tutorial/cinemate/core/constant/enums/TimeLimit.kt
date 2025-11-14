package vn.tutorial.cinemate.core.constant.enums

enum class TimeLimit(val time: Int, val displayText: String) {
    TIME_30(30, "30 m"),
    TIME_60(60, "1 h"),
    TIME_90(90, "1,5 h"),;

    companion object {
        fun fromTime(time: Int): TimeLimit = entries.firstOrNull { it.time == time } ?: TIME_30
    }
}
