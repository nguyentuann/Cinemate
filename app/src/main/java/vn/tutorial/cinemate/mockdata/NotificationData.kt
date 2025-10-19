package vn.tutorial.cinemate.mockdata

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Notification(
    val title: String,
    val subtitle: String,
    val time: Long,
    val imageUrl: String,
    val isUnread: Boolean = false
)

fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "Vừa xong"
        minutes < 60 -> "$minutes phút trước"
        hours < 24 -> "$hours giờ trước"
        days < 7 -> "$days ngày trước"
        days < 30 -> "${days / 7} tuần trước"
        else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}


object NotificationData {
    val importantNotification = listOf(
        Notification(
            title = "New Episode Released",
            subtitle = "The latest episode of 'Popular Show' is now available.",
            time = System.currentTimeMillis() - 60 * 60 * 1000, // todo 1 hour ago
            imageUrl = "https://i.pinimg.com/564x/e3/7e/fd/e37efd9ac1a36b2b38928f54717d9627.jpg",
            isUnread = true
        ),
        Notification(
            title = "Subscription Expiring Soon",
            subtitle = "Your premium subscription will expire in 3 days.",
            time = System.currentTimeMillis() - 60*1000, // todo 1 minute ago
            imageUrl = "https://i.pinimg.com/564x/e3/7e/fd/e37efd9ac1a36b2b38928f54717d9627.jpg",
            isUnread = true
        ),
        Notification(
            title = "New Movie Added",
            subtitle = "'Exciting Movie' has been added to your watchlist.",
            time = System.currentTimeMillis() -3*60*1000, // todo 3 minute ago
            imageUrl = "https://i.pinimg.com/564x/e3/7e/fd/e37efd9ac1a36b2b38928f54717d9627.jpg",
            isUnread = false
        )
    )

    val otherNotification = listOf(
        Notification(
            title = "Weekly Digest",
            subtitle = "Your weekly digest is ready to view.",
            time = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000, // todo 2 days ago
            imageUrl = "https://admin.vov.gov.vn/UploadFolder/KhoTin/Images/UploadFolder/VOVVN/Images/sites/default/files/styles/large/public/2023-12/2_16.jpeg.jpg",
            isUnread = false
        ),
        Notification(
            title = "New Feature Available",
            subtitle = "Check out the new features in the latest app update.",
            time = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000, // todo 5 days ago
            imageUrl = "https://admin.vov.gov.vn/UploadFolder/KhoTin/Images/UploadFolder/VOVVN/Images/sites/default/files/styles/large/public/2023-12/2_16.jpeg.jpg",
            isUnread = false
        ),
        Notification(
            title = "Survey Invitation",
            subtitle = "We value your feedback! Participate in our survey.",
            time = System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000, // todo 10 days ago
            imageUrl = "https://admin.vov.gov.vn/UploadFolder/KhoTin/Images/UploadFolder/VOVVN/Images/sites/default/files/styles/large/public/2023-12/2_16.jpeg.jpg",
            isUnread = false
        )
    )
}