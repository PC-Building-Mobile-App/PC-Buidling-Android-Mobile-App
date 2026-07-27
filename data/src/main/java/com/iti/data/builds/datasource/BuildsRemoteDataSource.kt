package com.iti.data.builds.datasource

import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.CompareBuildsRequestDto
import com.iti.data.builds.model.ComparisonDto
import com.iti.data.builds.model.CompatibilityCheckRequestDto
import com.iti.data.builds.model.CompatibilityReportDto
import com.iti.data.builds.model.GenerateBuildRequestDto
import com.iti.data.builds.model.GeneratedBuildDto
import com.iti.data.builds.model.SaveBuildRequestDto

interface BuildsRemoteDataSource {
    suspend fun getBuildCategories(): Result<List<BuildCategoryDto>>
    suspend fun getBuildsByCategory(categoryId: String): Result<List<BuildDto>>
    suspend fun generateBuild(request: GenerateBuildRequestDto): Result<GeneratedBuildDto>
    suspend fun checkCompatibility(request: CompatibilityCheckRequestDto): Result<CompatibilityReportDto>
    suspend fun saveBuild(request: SaveBuildRequestDto): Result<BuildDto>
    suspend fun updateBuild(buildId: String, request: SaveBuildRequestDto): Result<BuildDto>
    suspend fun compareBuilds(request: CompareBuildsRequestDto): Result<ComparisonDto>
    suspend fun getBuildById(id: String): Result<BuildDto>
}
