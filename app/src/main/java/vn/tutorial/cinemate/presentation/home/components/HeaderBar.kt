package vn.tutorial.cinemate.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import vn.tutorial.cinemate.R

@Composable
fun HeaderBar(
    modifier: Modifier = Modifier,
    headerItems: Map<String, () -> Unit>,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.weight(1f),
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null
        )

        headerItems.forEach { (title, onClick) ->
            HeaderItem(
                modifier = Modifier.weight(1f),
                title = title,
                onClick = onClick
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
            .clickable(onClick = onClick),
        text = title,
        style = MaterialTheme.typography.bodyMedium,
    )
}