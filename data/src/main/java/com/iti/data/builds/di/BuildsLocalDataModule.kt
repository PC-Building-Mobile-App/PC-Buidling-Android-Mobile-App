package com.iti.data.builds.di

import com.iti.data.builds.datasource.local.BuildsLocalDataSource
import com.iti.data.builds.datasource.local.BuildsLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BuildsLocalDataModule {

    @Binds
    @Singleton
    abstract fun bindBuildsLocalDataSource(
        impl: BuildsLocalDataSourceImpl,
    ): BuildsLocalDataSource
}
