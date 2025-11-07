package vn.tutorial.cinemate.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun timeFormatter(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatIsoDate(
    isoString: String,
    toLocal: Boolean = true
): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(isoString)
        val finalTime = if (toLocal) {
            zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
        } else {
            zonedDateTime
        }
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy")
        finalTime.format(formatter)
    } catch (e: Exception) {
        isoString
    }
}