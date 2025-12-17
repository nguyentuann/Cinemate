package vn.tutorial.cinemate.core.constant.enums

enum class TimeLimit(val time: Int, val displayText: String) {
    TIME_30(30, "30 m"),
    TIME_60(60, "1 h"),
    TIME_90(90, "1,5 h"),
    TIME_120(120, "2 h"),
    TIME_180(180, "3 h");
}
