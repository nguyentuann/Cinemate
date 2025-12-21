package vn.tutorial.cinemate.presentation.more.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.core.constant.enums.AgeLimit

@Composable
fun AgeLimitSelector(selected: AgeLimit, onSelect: (AgeLimit) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        AgeLimit.entries.forEach { ageLimit ->
            val isSelected = ageLimit == selected
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected)
                            Color.Red
                        else
                            Color.DarkGray
                    )
                    .clickable { onSelect(ageLimit) },
                contentAlignment = Alignment.Center
            ) {
                Text(ageLimit.displayText, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
