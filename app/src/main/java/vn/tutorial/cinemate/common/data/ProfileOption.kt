package vn.tutorial.cinemate.common.data

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.navigation.Route

data class ProfileOption(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val route: String = "",
)

val listOptions = listOf(
    ProfileOption(
        R.string.information,
        Icons.Default.Info,
        route = Route.Profile.route
    ),
    ProfileOption(
        R.string.change_password,
        Icons.Default.Lock,
        route = Route.ChangePassword.route
    ),
    ProfileOption(
        R.string.watching_history,
        Icons.Default.CheckCircle,
        route = Route.History.route
    ),
    ProfileOption(
        R.string.my_list,
        Icons.Default.List,
        route = Route.Favorite.route
    ),
//    ProfileOption(
//        R.string.children_mode,
//        Icons.Default.Face,
//        route =  Route.ChildrenMode.route
//    ),
    ProfileOption(
        R.string.package_management,
        Icons.Default.ShoppingCart,
        route =  Route.Subscription.route
    ),
    ProfileOption(
        R.string.notification_management,
        Icons.Default.Notifications,
        route = Route.SettingNotification.route
    ),
    ProfileOption(
        R.string.theme_language,
        Icons.Default.Settings,
        Route.ThemeAndLanguage.route
    )
)