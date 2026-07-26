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
import com.iti.data.builds.remote.BuildsApiService
import com.iti.data.util.safeCall
import com.iti.domain.exceptions.ServerException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildsRemoteDataSourceImpl @Inject constructor(
    private val apiService: BuildsApiService
) : BuildsRemoteDataSource {

    override suspend fun getBuildCategories(): Result<List<BuildCategoryDto>> = safeCall {
        val response = apiService.getBundles(type = null, page = 0, size = 100)
        val countsByType = if (response.status && response.data != null) {
            response.data.content.groupBy { it.type.uppercase() }.mapValues { it.value.size }
        } else emptyMap()

        mockBuildCategories.map { category ->
            category.copy(buildsCount = countsByType[category.id.uppercase()] ?: 0)
        }
    }

    override suspend fun getBuildsByCategory(categoryId: String): Result<List<BuildDto>> = safeCall {
        val response = apiService.getBundles(
            type = categoryId.uppercase(),
            page = 0,
            size = 20
        )
        if (response.status && response.data != null) {
            response.data.content
        } else {
            throw ServerException.Generic(
                message = response.message,
                code = 400
            )
        }
    }

    override suspend fun generateBuild(request: GenerateBuildRequestDto): Result<GeneratedBuildDto> = safeCall {
        val response = apiService.generateBuild(request)
        if (response.status && response.data != null) {
            response.data
        } else {
            throw ServerException.Generic(
                message = response.message,
                code = 400
            )
        }
    }

    override suspend fun checkCompatibility(request: CompatibilityCheckRequestDto): Result<CompatibilityReportDto> = safeCall {
        val response = apiService.checkCompatibility(request)
        if (response.status && response.data != null) {
            response.data
        } else {
            throw ServerException.Generic(
                message = response.message,
                code = 400
            )
        }
    }

    override suspend fun saveBuild(request: SaveBuildRequestDto): Result<BuildDto> = safeCall {
        val response = apiService.saveBuild(request)
        if (response.status && response.data != null) {
            response.data
        } else {
            throw ServerException.Generic(
                message = response.message,
                code = 400
            )
        }
    }

    override suspend fun updateBuild(buildId: String, request: SaveBuildRequestDto): Result<BuildDto> = safeCall {
        val response = apiService.updateBuild(buildId, request)
        if (response.status && response.data != null) {
            response.data
        } else {
            throw ServerException.Generic(
                message = response.message,
                code = 400
            )
        }
    }

    override suspend fun compareBuilds(request: CompareBuildsRequestDto): Result<ComparisonDto> = safeCall {
        val response = apiService.compareBuilds(request)
        if (response.status && response.data != null) {
            response.data
        } else {
            throw ServerException.Generic(
                message = response.message,
                code = 400
            )
        }
    }

    override suspend fun getBuildById(id: String): Result<BuildDto> = safeCall {
        val response = apiService.getBundleById(id)
        if (response.status && response.data != null) {
            response.data
        } else {
            throw ServerException.Generic(
                message = response.message,
                code = 400
            )
        }
    }

    private companion object {
        val mockBuildCategories = listOf(
            BuildCategoryDto("GAMING", "Gaming", "High FPS, max settings", 0, "GAMING"),
            BuildCategoryDto("PROGRAMMING", "Programming", "Fast compile, multitasking", 0, "PROGRAMMING"),
            BuildCategoryDto("CONTENT_CREATION", "Content Creation", "4K editing, rendering", 0, "CONTENT_CREATION"),
            BuildCategoryDto("OFFICE", "Office", "Productivity & speed", 0, "OFFICE"),
            BuildCategoryDto("AI_WORKSTATION", "AI & Workstation", "ML training, inference", 0, "AI_WORKSTATION"),
            BuildCategoryDto("DREAM_BUILDS", "Dream Builds", "No budget limits", 0, "DREAM_BUILDS"),
        )
    }
}
