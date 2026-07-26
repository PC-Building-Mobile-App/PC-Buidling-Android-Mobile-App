package com.iti.presentation.core.locale

import com.iti.domain.locale.repository.LocaleRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocaleEntryPoint {
    fun localeRepository(): LocaleRepository
}