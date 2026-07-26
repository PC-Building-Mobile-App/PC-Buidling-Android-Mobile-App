package com.iti.data.aichat.di

import com.iti.data.aichat.datasource.AiChatDataSource
import com.iti.data.aichat.datasource.AiChatRemoteDataSourceImpl
import com.iti.data.aichat.repository.AiChatRepositoryImpl
import com.iti.domain.aichat.repository.AiChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiChatDataModule {

    @Binds
    @Singleton
    abstract fun bindAiChatDataSource(
        impl: AiChatRemoteDataSourceImpl,
    ): AiChatDataSource

    @Binds
    @Singleton
    abstract fun bindAiChatRepository(
        impl: AiChatRepositoryImpl,
    ): AiChatRepository
}
