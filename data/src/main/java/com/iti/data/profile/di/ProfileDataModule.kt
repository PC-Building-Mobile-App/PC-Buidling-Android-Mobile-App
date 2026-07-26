package com.iti.data.profile.di

import com.iti.data.profile.ProfileImageRepositoryImpl
import com.iti.domain.profile.repository.ProfileImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileDataModule {

    @Binds
    abstract fun bindProfileImageRepository(impl: ProfileImageRepositoryImpl): ProfileImageRepository
}