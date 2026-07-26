package com.iti.domain.locale.repository

interface LocaleRepository {
    fun getLanguageTag(): String
    fun setLanguageTag(languageTag: String)
}