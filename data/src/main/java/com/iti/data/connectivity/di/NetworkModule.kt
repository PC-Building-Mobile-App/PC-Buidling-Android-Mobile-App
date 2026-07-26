package com.iti.data.connectivity.di

import android.content.Context
import com.iti.data.connectivity.repository.ConnectivityRepositoryImpl
import com.iti.domain.connectivity.repository.ConnectivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideConnectivityRepository(
        @ApplicationContext context: Context
    ): ConnectivityRepository {
        return ConnectivityRepositoryImpl(context)
    }
}
