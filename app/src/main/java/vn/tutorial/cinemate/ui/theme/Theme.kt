package vn.tutorial.cinemate.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import vn.tutorial.cinemate.core.constant.enums.ThemeType

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
    themeType: ThemeType = ThemeType.SYSTEM_DEFAULT,
    content: @Composable() () -> Unit
) {
    val darkTheme = when(themeType) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM_DEFAULT -> isSystemInDarkTheme()
    }

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

