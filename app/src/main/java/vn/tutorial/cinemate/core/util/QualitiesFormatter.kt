package vn.tutorial.cinemate.core.util

fun getQualityListAsString(qualities: Map<String, String>?): String {
    if (qualities.isNullOrEmpty()) return ""

    val qualityList = qualities.keys
        .filter { it.lowercase() != "master" } // bỏ "master"
        .sortedBy { it.filter { ch -> ch.isDigit() }.toIntOrNull() ?: 0 }

    return qualityList.joinToString(", ")
}
