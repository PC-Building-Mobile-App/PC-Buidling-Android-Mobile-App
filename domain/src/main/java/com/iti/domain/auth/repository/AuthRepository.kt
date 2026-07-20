package com.iti.domain.auth.repository

import com.iti.domain.auth.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<AuthUser>
    fun register(name: String, email: String, password: String): Flow<AuthUser>

    fun isLoggedIn(): Flow<Boolean>
    suspend fun logout()
}