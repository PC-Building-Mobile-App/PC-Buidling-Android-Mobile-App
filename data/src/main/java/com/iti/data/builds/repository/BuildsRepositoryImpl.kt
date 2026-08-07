package com.iti.data.builds.repository

import com.iti.data.builds.datasource.BuildsRemoteDataSource
import com.iti.data.builds.datasource.local.BuildsLocalDataSource
import com.iti.data.builds.mapper.toDomain
import com.iti.data.builds.mapper.toDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.BuildItemDto
import com.iti.data.builds.model.CompareBuildsRequestDto
import com.iti.data.components.datasource.ComponentDataSource
import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.model.BuildComparison
import com.iti.domain.builds.model.CompatibilityCheckRequest
import com.iti.domain.builds.model.CompatibilityCheckTarget
import com.iti.domain.builds.model.CompatibilityIssue
import com.iti.domain.builds.model.CompatibilityReport
import com.iti.domain.builds.model.GenerateBuildRequest
import com.iti.domain.builds.model.GeneratedBuild
import com.iti.domain.builds.model.SaveBuildRequest
import com.iti.domain.builds.repository.BuildsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildsRepositoryImpl @Inject constructor(
    private val remoteDataSource: BuildsRemoteDataSource,
    private val localDataSource: BuildsLocalDataSource,
    private val componentDataSource: ComponentDataSource,
) : BuildsRepository {

    override fun getBuildCategories(): Flow<Result<List<BuildCategory>>> = flow {
        val remoteResult = remoteDataSource.getBuildCategories()
        remoteResult.fold(
            onSuccess = { dtos -> emit(Result.success(dtos.map { it.toDomain() })) },
            onFailure = { error ->
                emitAll(
                    localDataSource.getCachedCategoryCounts().map { cached ->
                        if (cached.isNotEmpty()) Result.success(cached.map { it.toDomain() })
                        else Result.failure(error)
                    },
                )
            },
        )
    }

    override suspend fun getBuildsByCategory(categoryId: String): Result<List<Build>> {
        val remoteResult = remoteDataSource.getBuildsByCategory(categoryId)
        return remoteResult.fold(
            onSuccess = { dtos ->
                localDataSource.cacheBuildsForCategory(categoryId, dtos)
                Result.success(dtos.map { it.toDomain() })
            },
            onFailure = { error ->
                val cached = localDataSource.getCachedBuildsByCategory(categoryId)
                if (cached.isNotEmpty()) Result.success(cached.map { it.toDomain() })
                else Result.failure(error)
            },
        )
    }

    override suspend fun generateBuild(request: GenerateBuildRequest): Result<GeneratedBuild> =
        remoteDataSource.generateBuild(request.toDto())
            .map { it.toDomain() }

    override suspend fun checkCompatibility(request: CompatibilityCheckRequest): Result<CompatibilityReport> {
        val existingIds = (request.target as? CompatibilityCheckTarget.InProgressSelection)?.existingComponentIds
            ?: (request.target as? CompatibilityCheckTarget.SavedBuild)?.let { target ->
                localDataSource.getAllCachedBuilds().find { it.id.toString() == target.buildId }?.items?.map { it.id }
            }
            .orEmpty()

        // 1. A candidate component added to an empty build (new build) has 0 conflicts. Always compatible!
        if (existingIds.isEmpty()) {
            return Result.success(CompatibilityReport(compatible = true, issues = emptyList()))
        }

        // 2. Try remote compatibility check first
        val remoteResult = remoteDataSource.checkCompatibility(request.toDto())
        if (remoteResult.isSuccess) {
            return remoteResult.map { it.toDomain() }
        }

        // 3. Fallback to rule engine if remote API fails or incomplete build
        val candidateComp = runCatching { componentDataSource.getComponentById(request.candidateComponentId).firstOrNull() }.getOrNull()
        val candidateCategory = candidateComp?.category?.uppercase()

        val singleSlotCategories = setOf("CPU", "MOTHERBOARD", "PSU", "CASE", "COOLER")
        if (candidateCategory != null && candidateCategory in singleSlotCategories) {
            val hasDuplicateCategory = existingIds.any { existingId ->
                val existingComp = runCatching { componentDataSource.getComponentById(existingId).firstOrNull() }.getOrNull()
                existingComp?.category?.equals(candidateCategory, ignoreCase = true) == true
            }
            if (hasDuplicateCategory) {
                return Result.success(
                    CompatibilityReport(
                        compatible = false,
                        issues = listOf(
                            CompatibilityIssue(
                                rule = "${candidateCategory}_ALREADY_SELECTED",
                                message = "A ${candidateCategory.lowercase()} is already part of this build."
                            )
                        )
                    )
                )
            }
        }

        return Result.success(CompatibilityReport(compatible = true, issues = emptyList()))
    }

    override suspend fun saveBuild(request: SaveBuildRequest): Result<Build> {
        val dto = request.toDto()
        val buildId = request.buildId
        val remoteResult = if (!buildId.isNullOrBlank()) {
            remoteDataSource.updateBuild(buildId, dto)
        } else {
            remoteDataSource.saveBuild(dto)
        }

        if (remoteResult.isSuccess) {
            val savedDto = remoteResult.getOrThrow()
            localDataSource.saveBuildLocally(savedDto)
            return Result.success(savedDto.toDomain())
        }

        // If backend rejects incomplete build or server error occurs, save locally in Room DB!
        val items = request.componentIds.map { compId ->
            val compData = runCatching { componentDataSource.getComponentById(compId).firstOrNull() }.getOrNull()
            BuildItemDto(
                id = compId,
                productName = compData?.productName ?: "Component #$compId",
                category = compData?.category ?: "GENERAL",
                price = compData?.price ?: 0.0,
                quantity = 1,
                subtotal = compData?.price ?: 0.0,
                imageUrl = compData?.productImage
            )

        }

        val numericId = buildId?.toIntOrNull() ?: (System.currentTimeMillis() % 1000000).toInt()
        val now = "2026-08-07T16:00:00"
        val localDto = BuildDto(
            id = numericId,
            name = request.name,
            type = request.categoryId.uppercase(),
            typeDisplayName = request.name,
            totalPrice = items.sumOf { it.subtotal },
            compatible = true,
            items = items,
            issues = emptyList(),
            createdAt = now,
            updatedAt = now,
        )

        localDataSource.saveBuildLocally(localDto)
        return Result.success(localDto.toDomain())
    }

    override suspend fun compareBuilds(buildIds: List<Int>, buildNames: List<String>): Result<BuildComparison> =
        remoteDataSource.compareBuilds(CompareBuildsRequestDto(buildIds, buildNames)).map { it.toDomain() }

    override suspend fun getBuildById(id: String): Result<Build> {
        val remoteResult = remoteDataSource.getBuildById(id)
        if (remoteResult.isSuccess) return remoteResult.map { it.toDomain() }
        val localMatch = localDataSource.getAllCachedBuilds().find { it.id.toString() == id }
        return if (localMatch != null) Result.success(localMatch.toDomain())
        else Result.failure(Exception("Build not found"))
    }

    override suspend fun getAllBuilds(): Result<List<Build>> {
        val remoteBuilds = remoteDataSource.getAllBuilds().getOrDefault(emptyList())
        val localBuilds = localDataSource.getAllCachedBuilds()
        val merged = (remoteBuilds + localBuilds).distinctBy { it.id }
        return Result.success(merged.map { it.toDomain() })
    }
}


