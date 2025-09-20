package vn.tutorial.cinemate.common.data

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import vn.tutorial.cinemate.R

data class ProfileOption(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val action: () -> Unit = {}
)

val listOptions = listOf(
    ProfileOption(
        R.string.information,
        Icons.Default.Info
    ) { },
    ProfileOption(
        R.string.watching_history,
        Icons.Default.CheckCircle
    ) { },
    ProfileOption(
        R.string.my_list,
        Icons.Default.List
    ) { },
    ProfileOption(
        R.string.children_mode,
        Icons.Default.Face
    ) { },
    ProfileOption(
        R.string.package_management,
        Icons.Default.ShoppingCart
    ) { },
    ProfileOption(
        R.string.notification_management,
        Icons.Default.Notifications
    ) { },
    ProfileOption(
        R.string.theme_language,
        Icons.Default.Settings
    ) { },
    ProfileOption(
        R.string.help_reply,
        Icons.Default.Phone
    ) { },
    ProfileOption(
        R.string.sign_out,
        Icons.AutoMirrored.Filled.ExitToApp
    ) { }
)