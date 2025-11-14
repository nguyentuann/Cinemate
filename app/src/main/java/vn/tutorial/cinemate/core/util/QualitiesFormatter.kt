package vn.tutorial.cinemate.core.util

fun getQualityListAsString(qualities: List<String>?): String {
    if (qualities.isNullOrEmpty()) return ""

    val filteredAndSorted = qualities
        .filter { it.lowercase() != "master" } // loại bỏ "master"
        .sortedBy { it.filter { ch -> ch.isDigit() }.toIntOrNull() ?: 0 }

    return filteredAndSorted.joinToString(" - ")
}
