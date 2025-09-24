package vn.tutorial.cinemate.common.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import vn.tutorial.cinemate.R

data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String
)

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.ic_home, "home"),
        BottomNavItem("Search", R.drawable.ic_search, "search"),
        BottomNavItem("Coming Soon", R.drawable.ic_coming_soon, "coming_soon"),
        BottomNavItem("Notification", R.drawable.ic_notification, "notification"),
        BottomNavItem("More", R.drawable.ic_more, "more")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.height(110.dp)
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route

            // Animation scale khi chọn icon
            val scale by animateFloatAsState(
                targetValue = if (selected) 1.3f else 1f,
                label = "iconScale"
            )

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.scale(scale)
                    )
                },
                label = {
                    Text(
                        item.label, style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}