package vn.tutorial.cinemate.core.constant.enums

enum class TimeLimit(val time: Int, val displayText: String) {
    TIME_30(30, "30 minutes"),
    TIME_60(60, "1 hour"),
    TIME_90(90, "1,5 hours"),;

    companion object {
        fun fromTime(time: Int): TimeLimit = entries.firstOrNull { it.time == time } ?: TIME_30
    }
}
