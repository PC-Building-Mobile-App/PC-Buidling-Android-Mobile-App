package com.iti.domain.builds.usecase

import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.repository.BuildsRepository
import javax.inject.Inject

class GetBuildCategoriesUseCase @Inject constructor(
    private val repository: BuildsRepository,
) {
    suspend operator fun invoke(): Result<List<BuildCategory>> = repository.getBuildCategories()
}