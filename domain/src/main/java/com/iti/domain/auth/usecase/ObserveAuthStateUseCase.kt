package com.iti.domain.auth.usecase

import com.iti.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val repo: AuthRepository,
) {
    operator fun invoke(): Flow<Boolean> = repo.isLoggedIn()
}