package com.iti.data.stats.di

import com.iti.data.stats.datasource.PlatformStatDataSource
import com.iti.data.stats.datasource.PlatformStatLocalDataSourceImpl
import com.iti.data.stats.repository.PlatformStatRepositoryImpl
import com.iti.domain.stats.repository.PlatformStatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlatformStatDataModule {

    @Binds
    @Singleton
    abstract fun bindPlatformStatDataSource(
        impl: PlatformStatLocalDataSourceImpl
    ): PlatformStatDataSource

    @Binds
    @Singleton
    abstract fun bindPlatformStatRepository(
        impl: PlatformStatRepositoryImpl
    ): PlatformStatRepository
}