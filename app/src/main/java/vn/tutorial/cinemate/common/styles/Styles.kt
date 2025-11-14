package vn.tutorial.cinemate.common.styles

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object Styles {
    object ShapeStyles {
        val smallCorner = RoundedCornerShape(4.dp)
        val mediumCorner = RoundedCornerShape(8.dp)
        val largeCorner = RoundedCornerShape(16.dp)
        val infiniteCorner: RoundedCornerShape = RoundedCornerShape(50)
    }
}