package com.iti.domain.builds.repository

import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.model.CompatibilityCheckRequest
import com.iti.domain.builds.model.CompatibilityReport
import com.iti.domain.builds.model.GenerateBuildRequest
import com.iti.domain.builds.model.GeneratedBuild
import com.iti.domain.builds.model.SaveBuildRequest
import kotlinx.coroutines.flow.Flow

interface BuildsRepository {
    fun getBuildCategories(): Flow<Result<List<BuildCategory>>>
    suspend fun getBuildsByCategory(categoryId: String): Result<List<Build>>
    suspend fun generateBuild(request: GenerateBuildRequest): Result<GeneratedBuild>
    suspend fun checkCompatibility(request: CompatibilityCheckRequest): Result<CompatibilityReport>
    suspend fun saveBuild(request: SaveBuildRequest): Result<Build>
}