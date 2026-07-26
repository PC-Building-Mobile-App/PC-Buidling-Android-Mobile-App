package com.iti.domain.builds.repository

import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.model.BuildComparison
import com.iti.domain.builds.model.CompatibilityCheckRequest
import com.iti.domain.builds.model.CompatibilityReport
import com.iti.domain.builds.model.GenerateBuildRequest
import com.iti.domain.builds.model.GeneratedBuild
import com.iti.domain.builds.model.SaveBuildRequest

interface BuildsRepository {
    suspend fun getBuildCategories(): Result<List<BuildCategory>>
    suspend fun getBuildsByCategory(categoryId: String): Result<List<Build>>
    suspend fun generateBuild(request: GenerateBuildRequest): Result<GeneratedBuild>
    suspend fun checkCompatibility(request: CompatibilityCheckRequest): Result<CompatibilityReport>
    suspend fun saveBuild(request: SaveBuildRequest): Result<Build>
    suspend fun compareBuilds(buildIds: List<Int>, buildNames: List<String>): Result<BuildComparison>
    suspend fun getBuildById(id: String): Result<Build>
}
