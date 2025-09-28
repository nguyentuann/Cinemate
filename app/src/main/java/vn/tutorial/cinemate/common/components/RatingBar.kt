package vn.tutorial.cinemate.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.tutorial.cinemate.ui.theme.yellow
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    starSize: Dp = 20.dp,
    starColor: Color = yellow
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = rating.toString(),
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 18.sp
            )
        )

        for (i in 1..maxRating) {
            val fillFraction = when {
                i <= rating -> 1f
                i - rating in 0.0..1.0 -> (rating - (i - 1)).toFloat()
                else -> 0f
            }

            Canvas(
                modifier = Modifier.size(starSize)
            ) {
                val path = Path().apply {
                    moveTo(size.width / 2, 0f)
                    for (j in 1..5) {
                        val angle = Math.toRadians((j * 144).toDouble() - 90)
                        lineTo(
                            (size.width / 2 + size.width / 2 * cos(angle)).toFloat(),
                            (size.height / 2 + size.height / 2 * sin(angle)).toFloat()
                        )
                    }
                    close()
                }

                // vẽ sao rỗng
                drawPath(path, color = starColor.copy(alpha = 0.3f))

                // vẽ sao đầy theo fillFraction
                clipRect(right = size.width * fillFraction) {
                    drawPath(path, color = starColor)
                }
            }
        }
    }
}
