package com.iti.data.hardwarenews.di

import com.iti.data.hardwarenews.datasource.HardwareNewsRemoteDataSource
import com.iti.data.hardwarenews.datasource.HardwareNewsRemoteDataSourceImpl
import com.iti.data.hardwarenews.repository.HardwareNewsRepositoryImpl
import com.iti.domain.hardwarenews.repository.HardwareNewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareNewsDataModule {

    @Binds
    @Singleton
    abstract fun bindHardwareNewsRemoteDataSource(
        impl: HardwareNewsRemoteDataSourceImpl
    ): HardwareNewsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindHardwareNewsRepository(
        impl: HardwareNewsRepositoryImpl
    ): HardwareNewsRepository

    companion object {
        @Provides
        @Singleton
        fun provideKtorHttpClient(): HttpClient {
            return HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        prettyPrint = true
                    })
                }
                install(Logging) {
                    level = LogLevel.INFO
                }
            }
        }
    }
}