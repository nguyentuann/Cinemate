package vn.tutorial.cinemate.presentation.more.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.common.icons.AppIcons
import vn.tutorial.cinemate.navigation.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBack(
    title: String,
) {
    val navController = LocalNavController.current
    TopAppBar(
        title = {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    AppIcons.back(),
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}