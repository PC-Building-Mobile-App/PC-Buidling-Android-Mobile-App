package com.iti.data.locale.di

import com.iti.data.locale.repository.LocaleRepositoryImpl
import com.iti.domain.locale.repository.LocaleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocaleDataModule {

    @Binds
    abstract fun bindLocaleRepository(impl: LocaleRepositoryImpl): LocaleRepository
}