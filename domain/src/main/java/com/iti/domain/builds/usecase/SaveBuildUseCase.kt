package com.iti.domain.builds.usecase

import com.iti.domain.builds.model.SaveBuildRequest
import com.iti.domain.builds.model.Build
import com.iti.domain.builds.repository.BuildsRepository
import javax.inject.Inject

class SaveBuildUseCase @Inject constructor(
    private val repository: BuildsRepository,
) {
    suspend operator fun invoke(request: SaveBuildRequest): Result<Build> =
        repository.saveBuild(request)
}