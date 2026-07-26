package com.iti.data.locale.repository

import android.content.Context
import com.iti.domain.locale.repository.LocaleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocaleRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocaleRepository {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getLanguageTag(): String =
        prefs.getString(KEY_LANGUAGE_TAG, DEFAULT_LANGUAGE_TAG) ?: DEFAULT_LANGUAGE_TAG

    override fun setLanguageTag(languageTag: String) {
        prefs.edit().putString(KEY_LANGUAGE_TAG, languageTag).apply()
    }

    private companion object {
        const val PREFS_NAME = "locale_prefs"
        const val KEY_LANGUAGE_TAG = "language_tag"
        const val DEFAULT_LANGUAGE_TAG = "en"
    }
}