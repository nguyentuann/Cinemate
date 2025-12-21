package vn.tutorial.cinemate.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.navigation.LocalNavController

@Composable
fun HeaderBar(
    headerItems: Map<String, String>,
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                MaterialTheme.colorScheme.background.copy(
                    alpha = 0.3f
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
        }

        items(headerItems.entries.toList()) { (title, route) ->
            HeaderItem(
                title = title,
                onClick = {
                    navController.navigate(route)
                }
            )
        }
    }
}

@Composable
private fun HeaderItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {}
) {
    Text(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        text = title,
        style = MaterialTheme.typography.bodyMedium,
    )
}