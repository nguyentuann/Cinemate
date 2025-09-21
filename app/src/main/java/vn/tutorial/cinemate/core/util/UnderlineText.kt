package vn.tutorial.cinemate.core.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

fun underLineText(message: String, email: String, color: Color, fontWeight: FontWeight) =
    buildAnnotatedString {
        val parts = message.split("%s")
        append(parts[0])
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = color,
                fontWeight = fontWeight
            ),

            ) {
            append(email)
        }
        if (parts.size > 1) append(parts[1])
    }
