package com.iti.data.auth.di

import com.iti.data.auth.datasource.AuthMockDataSourceImpl
import com.iti.data.auth.datasource.AuthRemoteDataSource
import com.iti.data.auth.repository.AuthRepositoryImpl
import com.iti.domain.auth.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataModule {

    @Binds
    abstract fun bindAuthRemoteDataSource(
        impl: AuthMockDataSourceImpl,
    ): AuthRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository
}