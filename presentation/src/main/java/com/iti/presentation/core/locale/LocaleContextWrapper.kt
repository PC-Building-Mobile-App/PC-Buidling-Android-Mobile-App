package com.iti.presentation.core.locale

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleContextWrapper {

    fun wrap(context: Context, languageTag: String): Context {
        val locale = Locale.forLanguageTag(languageTag)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}