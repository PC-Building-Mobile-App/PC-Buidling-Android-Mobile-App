package com.iti.domain.builds.usecase

import com.iti.domain.builds.model.Build
import com.iti.domain.builds.repository.BuildsRepository
import javax.inject.Inject

class GetBuildByIdUseCase @Inject constructor(
    private val repository: BuildsRepository
) {
    suspend operator fun invoke(id: String): Result<Build> =
        repository.getBuildById(id)
}
