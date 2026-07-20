package com.iti.data.auth.repository

import com.iti.data.auth.local.AuthTokenStorage
import com.iti.data.auth.datasource.AuthRemoteDataSource
import com.iti.data.auth.mapper.toDomain
import com.iti.domain.auth.model.AuthUser
import com.iti.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenStorage: AuthTokenStorage,
) : AuthRepository {

    override fun login(email: String, password: String): Flow<AuthUser> = flow {
        val result = remoteDataSource.login(email, password)
        val user = result.getOrThrow().toDomain()
        tokenStorage.saveSession(user)
        emit(user)
    }

    override fun register(name: String, email: String, password: String): Flow<AuthUser> = flow {
        val result = remoteDataSource.register(name, email, password)
        val user = result.getOrThrow().toDomain()
        tokenStorage.saveSession(user)
        emit(user)
    }

    override fun isLoggedIn(): Flow<Boolean> = tokenStorage.isLoggedIn

    override suspend fun logout() = tokenStorage.clearSession()
}