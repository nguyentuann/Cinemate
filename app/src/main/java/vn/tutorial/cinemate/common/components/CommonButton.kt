package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.common.styles.Styles

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    title: String
) {
    Button(
        modifier = modifier.height(50.dp),
        shape = Styles.ShapeStyles.smallCorner,
        onClick = onClick
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
    }
}