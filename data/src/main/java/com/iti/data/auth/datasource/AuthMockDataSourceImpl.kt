package com.iti.data.auth.datasource

import com.iti.data.auth.model.AuthDataDto
import com.iti.data.auth.model.AuthResponseDto
import com.iti.data.auth.model.UserDto
import com.iti.domain.exceptions.AuthException
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthMockDataSourceImpl @Inject constructor() : AuthRemoteDataSource {

    override suspend fun login(email: String, password: String): Result<AuthResponseDto> {
        delay(FAKE_NETWORK_DELAY_MS)

        if (email.isBlank() || password.isBlank()) {
            return Result.failure(AuthException.InvalidCredentials)
        }
        if (email.equals("blocked@example.com", ignoreCase = true) || password == "wrongpass") {
            return Result.failure(AuthException.InvalidCredentials)
        }

        return Result.success(
            AuthResponseDto(
                status = true,
                message = "Login successful",
                data = AuthDataDto(
                    token = "mock-token-${System.currentTimeMillis()}",
                    tokenType = "Bearer",
                    user = UserDto(
                        id = 1,
                        name = email.substringBefore("@").ifBlank { "User" },
                        email = email,
                    ),
                ),
            ),
        )
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
    ): Result<AuthResponseDto> {
        delay(FAKE_NETWORK_DELAY_MS)

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return Result.failure(AuthException.InvalidCredentials)
        }
        if (email.equals("taken@example.com", ignoreCase = true)) {
            return Result.failure(AuthException.EmailAlreadyExists)
        }

        return Result.success(
            AuthResponseDto(
                status = true,
                message = "Registration successful",
                data = AuthDataDto(
                    token = "mock-token-${System.currentTimeMillis()}",
                    tokenType = "Bearer",
                    user = UserDto(
                        id = 2,
                        name = name,
                        email = email,
                    ),
                ),
            ),
        )
    }

    private companion object {
        const val FAKE_NETWORK_DELAY_MS = 900L
    }
}