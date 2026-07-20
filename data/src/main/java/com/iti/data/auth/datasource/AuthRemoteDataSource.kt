package com.iti.data.auth.datasource

import com.iti.data.auth.model.AuthResponseDto

interface AuthRemoteDataSource {
    suspend fun login(email: String, password: String): Result<AuthResponseDto>
    suspend fun register(name: String, email: String, password: String): Result<AuthResponseDto>
}