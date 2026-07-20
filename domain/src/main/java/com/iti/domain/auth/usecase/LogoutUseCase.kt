package com.iti.domain.auth.usecase

import com.iti.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repo: AuthRepository,
) {
    suspend operator fun invoke() = repo.logout()
}