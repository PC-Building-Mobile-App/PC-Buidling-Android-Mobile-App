package com.iti.domain.auth.usecase

import com.iti.domain.auth.model.AuthUser
import com.iti.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repo: AuthRepository,
) {
    operator fun invoke(name: String, email: String, password: String): Flow<AuthUser> =
        repo.register(name.trim(), email.trim(), password)
}