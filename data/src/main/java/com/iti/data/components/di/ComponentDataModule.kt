package com.iti.data.components.di

import com.iti.data.components.datasource.ComponentDataSource
import com.iti.data.components.datasource.ComponentMockDataSourceImpl
import com.iti.data.components.repository.ComponentRepositoryImpl
import com.iti.domain.components.repository.ComponentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ComponentDataModule {

    @Binds
    @Singleton
    abstract fun bindComponentDataSource(
        impl: ComponentMockDataSourceImpl
    ): ComponentDataSource

    @Binds
    @Singleton
    abstract fun bindComponentRepository(
        impl: ComponentRepositoryImpl
    ): ComponentRepository
}