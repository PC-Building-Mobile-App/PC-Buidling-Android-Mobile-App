package com.iti.domain.builds.repository

import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategory

interface BuildsRepository {
    suspend fun getBuildCategories(): Result<List<BuildCategory>>
    suspend fun getBuildsByCategory(categoryId: String): Result<List<Build>>
}