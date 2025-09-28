package vn.tutorial.cinemate.common.styles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object Styles {
    object ShapeStyles {
        val smallCorner = RoundedCornerShape(4.dp)
        val mediumCorner = RoundedCornerShape(8.dp)
        val largeCorner = RoundedCornerShape(16.dp)
    }

    object BorderStyles {
        val thin = BorderStroke(1.dp, Color.Gray)
        val thick = BorderStroke(2.dp, Color.Black)
    }


}