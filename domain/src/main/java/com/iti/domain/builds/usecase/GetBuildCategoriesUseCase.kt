package com.iti.domain.builds.usecase

import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.repository.BuildsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBuildCategoriesUseCase @Inject constructor(
    private val repository: BuildsRepository,
) {
    operator fun invoke(): Flow<Result<List<BuildCategory>>> = repository.getBuildCategories()
}