package com.iti.data.ai.di

import com.iti.data.ai.datasource.AiDataSource
import com.iti.data.ai.datasource.AiRemoteDataSourceImpl
import com.iti.data.ai.repository.AiRepositoryImpl
import com.iti.domain.ai.repository.AiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiDataModule {

    @Binds
    @Singleton
    abstract fun bindAiDataSource(
        impl: AiRemoteDataSourceImpl
    ): AiDataSource

    @Binds
    @Singleton
    abstract fun bindAiRepository(
        impl: AiRepositoryImpl
    ): AiRepository
}
