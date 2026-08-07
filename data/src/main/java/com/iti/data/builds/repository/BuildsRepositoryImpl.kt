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
        if (candidateComp != null) {
            val existingComps = existingIds.mapNotNull { id ->
                runCatching { componentDataSource.getComponentById(id).firstOrNull() }.getOrNull()
            }
            val report = evaluateCompatibility(candidateComp, existingComps)
            if (!report.compatible) {
                return Result.success(report)
            }
        }

        return Result.success(CompatibilityReport(compatible = true, issues = emptyList()))
    }

    private fun evaluateCompatibility(candidate: com.iti.data.components.model.ComponentDataModel, existingComps: List<com.iti.data.components.model.ComponentDataModel>): CompatibilityReport {
        val issues = mutableListOf<CompatibilityIssue>()

        val candidateCategory = candidate.category.uppercase()
        val candidateSocket = extractSocket(candidate)
        val candidateBrand = extractBrand(candidate)
        val candidateRamType = extractRamType(candidate)

        for (existing in existingComps) {
            val existingCategory = existing.category.uppercase()
            val existingSocket = extractSocket(existing)
            val existingBrand = extractBrand(existing)
            val existingRamType = extractRamType(existing)

            // CPU vs Motherboard socket/brand mismatch
            if ((candidateCategory == "CPU" && existingCategory == "MOTHERBOARD") ||
                (candidateCategory == "MOTHERBOARD" && existingCategory == "CPU")) {

                if (candidateBrand != null && existingBrand != null && candidateBrand != existingBrand) {
                    issues.add(
                        CompatibilityIssue(
                            rule = "BRAND_MISMATCH",
                            message = "$candidateBrand $candidateCategory is incompatible with $existingBrand $existingCategory (${existing.productName})."
                        )
                    )
                } else if (candidateSocket != null && existingSocket != null && candidateSocket != existingSocket) {
                    issues.add(
                        CompatibilityIssue(
                            rule = "SOCKET_MISMATCH",
                            message = "Socket mismatch: $candidateCategory ($candidateSocket) does not fit $existingCategory ($existingSocket)."
                        )
                    )
                }
            }

            // RAM vs Motherboard/CPU DDR type mismatch
            if (candidateCategory == "MEMORY" && existingRamType != null) {
                if (existingSocket == "AM5" && candidateRamType == "DDR4") {
                    issues.add(
                        CompatibilityIssue(
                            rule = "RAM_MISMATCH",
                            message = "DDR4 RAM is incompatible with AM5 motherboard (AM5 requires DDR5)."
                        )
                    )
                } else if (existingRamType != null && candidateRamType != null && existingRamType != candidateRamType) {
                    issues.add(
                        CompatibilityIssue(
                            rule = "RAM_MISMATCH",
                            message = "$candidateRamType RAM is incompatible with $existingRamType platform (${existing.productName})."
                        )
                    )
                }
            }
        }

        return CompatibilityReport(
            compatible = issues.isEmpty(),
            issues = issues
        )
    }

    private fun extractSocket(comp: com.iti.data.components.model.ComponentDataModel): String? {
        val text = "${comp.productName} ${comp.specs}".uppercase()
        return when {
            text.contains("AM5") || text.contains("B650") || text.contains("X670") || text.contains("7800X3D") || text.contains("7950X") || text.contains("7600X") -> "AM5"
            text.contains("AM4") || text.contains("B550") || text.contains("X570") || text.contains("5800X") || text.contains("5600X") -> "AM4"
            text.contains("LGA1700") || text.contains("Z790") || text.contains("B760") || text.contains("Z690") || text.contains("14700K") || text.contains("13600K") || text.contains("12400F") -> "LGA1700"
            text.contains("LGA1200") || text.contains("Z590") || text.contains("B560") -> "LGA1200"
            else -> null
        }
    }

    private fun extractBrand(comp: com.iti.data.components.model.ComponentDataModel): String? {
        val text = "${comp.productName} ${comp.specs}".uppercase()
        return when {
            text.contains("INTEL") || text.contains("LGA1700") || text.contains("Z790") || text.contains("B760") || text.contains("Z690") || text.contains("14700K") || text.contains("13600K") -> "INTEL"
            text.contains("AMD") || text.contains("RYZEN") || text.contains("AM5") || text.contains("AM4") || text.contains("B650") || text.contains("X670") -> "AMD"
            else -> null
        }
    }

    private fun extractRamType(comp: com.iti.data.components.model.ComponentDataModel): String? {
        val text = "${comp.productName} ${comp.specs}".uppercase()
        return when {
            text.contains("DDR5") -> "DDR5"
            text.contains("DDR4") -> "DDR4"
            else -> null
        }
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


