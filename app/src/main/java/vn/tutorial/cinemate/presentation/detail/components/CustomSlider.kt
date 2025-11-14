package vn.tutorial.cinemate.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.tutorial.cinemate.core.util.timeFormatter

@Composable
fun CustomSlider(
    position: Float,
    duration: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        BoxWithConstraints(
            modifier = Modifier.height(30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val trackWidth = maxWidth
            val progressFraction = if (duration > 0f) position / duration else 0f

            // Track nền
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(2.dp))
            )

            // Track đã xem
            Box(
                modifier = Modifier
                    .width(trackWidth * progressFraction)
                    .height(4.dp)
                    .background(Color.Red, shape = RoundedCornerShape(2.dp))
            )

            // Thumb
            Box(
                modifier = Modifier
                    .offset(x = trackWidth * progressFraction - 6.dp) // 6.dp = radius
                    .size(12.dp)
                    .background(Color.Red, shape = CircleShape)
            )

            // Invisible Slider để drag
            Slider(
                value = position,
                onValueChange = onValueChange,
                valueRange = 0f..duration,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Thời gian 2 đầu
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(timeFormatter(position.toLong()), color = Color.White, fontSize = 12.sp)
            Text(timeFormatter(duration.toLong()), color = Color.White, fontSize = 12.sp)
        }
    }
}
