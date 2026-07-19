package com.iti.data.builds.di

import com.iti.data.builds.datasource.BuildsRemoteDataSource
import com.iti.data.builds.datasource.MockBuildsRemoteDataSourceImpl
import com.iti.data.builds.repository.BuildsRepositoryImpl
import com.iti.domain.builds.repository.BuildsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BuildsModule {

    @Binds
    abstract fun bindBuildsRemoteDataSource(impl: MockBuildsRemoteDataSourceImpl): BuildsRemoteDataSource

    @Binds
    abstract fun bindBuildsRepository(impl: BuildsRepositoryImpl): BuildsRepository
}