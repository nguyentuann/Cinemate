package vn.tutorial.cinemate.common.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.navigation.Route

data class BottomNavItem(
    val label: String,
    val icon: Painter,
    val route: String
)

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(stringResource(R.string.home), AppIcons.home(), "home"),
        BottomNavItem(stringResource(R.string.search), AppIcons.search(), "search"),
        BottomNavItem(stringResource(R.string._package), AppIcons.services(), Route.Subscription.route),
        BottomNavItem(stringResource(R.string.more), AppIcons.more(), "more")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route

            // Animation scale khi chọn icon
            val scale by animateFloatAsState(
                targetValue = if (selected) 1.5f else 1f,
                label = "iconScale"
            )

            NavigationBarItem(
                modifier = Modifier.semantics {
                    contentDescription = item.label
                },
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
                        item.icon,
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