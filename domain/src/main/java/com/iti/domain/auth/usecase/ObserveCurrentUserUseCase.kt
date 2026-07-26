package com.iti.domain.auth.usecase

import com.iti.domain.auth.model.AuthUser
import com.iti.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
    private val repo: AuthRepository,
) {
    operator fun invoke(): Flow<AuthUser?> = repo.observeCurrentUser()
}