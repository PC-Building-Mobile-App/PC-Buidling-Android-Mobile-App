package com.iti.data.componentCategories.di

import com.iti.data.componentCategories.datasource.ComponentCategoryDataSource
import com.iti.data.componentCategories.datasource.ComponentCategoryLocalDataSourceImpl
import com.iti.data.componentCategories.repository.ComponentCategoryRepositoryImpl
import com.iti.domain.componentcategories.repository.ComponentCategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ComponentsCategoriesDataModule {

    @Binds
    @Singleton
    abstract fun bindComponentCategoryDataSource(
        impl: ComponentCategoryLocalDataSourceImpl
    ): ComponentCategoryDataSource

    @Binds
    @Singleton
    abstract fun bindComponentCategoryRepository(
        impl: ComponentCategoryRepositoryImpl
    ): ComponentCategoryRepository
}