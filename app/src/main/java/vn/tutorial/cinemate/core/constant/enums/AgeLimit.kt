package vn.tutorial.cinemate.core.constant.enums

enum class AgeLimit(val age: Int, val displayText: String) {
    AGE_3(3, "3+"),
    AGE_7(7, "7+"),
    AGE_12(12, "12+"),
    AGE_16(16, "16+"),
    AGE_18(18, "18+");

    companion object {
        fun fromAge(age: Int): AgeLimit = entries.firstOrNull { it.age == age } ?: AGE_3
    }
}
