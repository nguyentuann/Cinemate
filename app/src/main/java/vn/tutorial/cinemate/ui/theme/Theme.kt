package vn.tutorial.cinemate.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Immutable
data class ExtendedColorScheme(
    val customColor1: ColorFamily,
)

val lightScheme = lightColorScheme(
    primary = Red,
    onPrimary = White,
    secondary = SystemBlue,
    onSecondary = White,

    background = White,
    onBackground = Black,

    surface = GreyLight3,
    onSurface = Black,

    error = SystemRed,
    onError = White
)

val darkScheme = darkColorScheme(
    primary = Red,
    onPrimary = White,

    secondary = SystemBlue,
    onSecondary = Black,

    background = Black,
    onBackground = White,

    surface = GreyDark2,
    onSurface = White,

    error = SystemRed,
    onError = White
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

@Composable
fun CinemateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Set màu nền status bar = màu primary của theme
            window.statusBarColor = colorScheme.background.toArgb()

            // Set icon status bar trắng/đen
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

