package vn.tutorial.cinemate.core.locale


import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.*

val LocalAppLocale = compositionLocalOf { Locale.getDefault() }

@Composable
fun ProvideAppLocale(locale: Locale, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val localizedContext = remember(locale) {
        LocaleManager.setLocale(context, locale)
    }
    CompositionLocalProvider(
        LocalAppLocale provides locale,
        LocalContext provides localizedContext
    ) {
        content()
    }
}