package com.iti.domain.builds.usecase

import com.iti.domain.builds.model.GenerateBuildRequest
import com.iti.domain.builds.model.GeneratedBuild
import com.iti.domain.builds.repository.BuildsRepository
import javax.inject.Inject

class GenerateBuildUseCase @Inject constructor(
    private val repository: BuildsRepository,
) {
    suspend operator fun invoke(request: GenerateBuildRequest): Result<GeneratedBuild> =
        repository.generateBuild(request)
}