package com.iti.domain.builds.usecase

import com.iti.domain.builds.model.CompatibilityCheckRequest
import com.iti.domain.builds.model.CompatibilityReport
import com.iti.domain.builds.repository.BuildsRepository
import javax.inject.Inject

class CheckComponentCompatibilityUseCase @Inject constructor(
    private val repository: BuildsRepository,
) {
    suspend operator fun invoke(request: CompatibilityCheckRequest): Result<CompatibilityReport> =
        repository.checkCompatibility(request)
}