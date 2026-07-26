package com.iti.domain.builds.usecase

import com.iti.domain.builds.model.BuildComparison
import com.iti.domain.builds.repository.BuildsRepository
import javax.inject.Inject

class CompareBuildsUseCase @Inject constructor(
    private val repository: BuildsRepository
) {
    suspend operator fun invoke(buildIds: List<Int>, buildNames: List<String>): Result<BuildComparison> =
        repository.compareBuilds(buildIds, buildNames)
}
