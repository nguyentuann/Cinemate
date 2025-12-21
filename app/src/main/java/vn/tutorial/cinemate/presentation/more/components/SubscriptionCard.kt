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
    onSelect: (String) -> Unit? = {},
    isSelected: Boolean? = false,
) {
    val backgroundBrush = if (isSelected == true) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF004AFF),
                Color(0xFFB80000),
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF424242),
                Color(0xFF212121)
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
            modifier = Modifier.background(backgroundBrush)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                // 🔹 Plan name
                Text(
                    text = plan.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                // 🔹 Description
                Text(
                    text = plan.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(Modifier.height(8.dp))

                // 🔹 Price
                Text(
                    text = stringResource(R.string.monthly_price) +
                            ": ${plan.price} ₫ / ${plan.durationDays} " + stringResource(R.string.days),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                // 🔹 Max devices
                Text(
                    text = stringResource(R.string.device_support) +
                            ": ${plan.maxDevice}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                Spacer(Modifier.height(8.dp))

                // 🔹 Features
                FeatureItem(
                    text = stringResource(R.string.ad_free),
                    enabled = plan.featured.addFree
                )

                FeatureItem(
                    text = stringResource(R.string.hd_streaming),
                    enabled = plan.featured.hdStreaming
                )

                FeatureItem(
                    text = stringResource(R.string.offline_download),
                    enabled = plan.featured.offlineDownload
                )

                FeatureItem(
                    text = stringResource(R.string.multiple_devices),
                    enabled = plan.featured.multipleDevices
                )

                if (plan.featured.familySharing) {
                    FeatureItem(
                        text = stringResource(R.string.family_sharing),
                        enabled = true
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureItem(
    text: String,
    enabled: Boolean
) {
    Text(
        text = if (enabled) "✓ $text" else "✗ $text",
        style = MaterialTheme.typography.bodySmall,
        color = if (enabled) Color.White else Color.White.copy(alpha = 0.4f)
    )
}
