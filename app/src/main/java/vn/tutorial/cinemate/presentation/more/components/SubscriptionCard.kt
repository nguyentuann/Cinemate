package vn.tutorial.cinemate.presentation.more.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.styles.Styles
import vn.tutorial.cinemate.domain.model.SubscriptionPlanModel

@Composable
fun SubscriptionCard(
    plan: SubscriptionPlanModel,
    onSelect: (String) -> Unit,
    isSelected: Boolean,
) {
    val backgroundColor = if (isSelected) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF004AFF),
                Color(0xFFB80000),
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF424242),  // xám đậm
                Color(0xFF212121)   // xám tối
            )
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(plan.id) },
        shape = Styles.ShapeStyles.mediumCorner,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(brush = backgroundColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "${plan.name} ${plan.resolution}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.monthly_price) + ": " + plan.price + " ₫",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    stringResource(R.string.image_quality) + ": " + plan.quality,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    stringResource(R.string.sound) + ": " + plan.sound,

                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    stringResource(R.string.device_support) + ": " + plan.supportedDevices,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    stringResource(R.string.devices) + ": " + plan.simultaneousDevices,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    stringResource(R.string.download) + ": " + plan.downloadDevices,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}