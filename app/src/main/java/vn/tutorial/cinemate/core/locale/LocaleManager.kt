package vn.tutorial.cinemate.core.locale

import android.content.Context
import java.util.*

object LocaleManager {
    fun setLocale(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}