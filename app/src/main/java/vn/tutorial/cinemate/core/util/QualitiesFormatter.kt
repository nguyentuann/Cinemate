package vn.tutorial.cinemate.core.util

fun getHighestQuality(qualities: List<String>?): String {
    if (qualities.isNullOrEmpty()) return ""

    return qualities
        .filter { it.lowercase() != "master" } // loại bỏ "master"
        .maxByOrNull { it.filter { ch -> ch.isDigit() }.toIntOrNull() ?: 0 } // lấy chất lượng cao nhất
        ?: ""
}
