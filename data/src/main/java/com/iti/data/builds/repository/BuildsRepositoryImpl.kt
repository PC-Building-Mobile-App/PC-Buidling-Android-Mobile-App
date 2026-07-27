package com.iti.data.builds.repository

import com.iti.data.builds.datasource.BuildsRemoteDataSource
import com.iti.data.builds.datasource.local.BuildsLocalDataSource
import com.iti.data.builds.mapper.toDomain
import com.iti.data.builds.mapper.toDto
import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.model.CompatibilityCheckRequest
import com.iti.domain.builds.model.CompatibilityReport
import com.iti.domain.builds.model.GenerateBuildRequest
import com.iti.domain.builds.model.GeneratedBuild
import com.iti.domain.builds.model.SaveBuildRequest
import com.iti.domain.builds.repository.BuildsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildsRepositoryImpl @Inject constructor(
    private val remoteDataSource: BuildsRemoteDataSource,
    private val localDataSource: BuildsLocalDataSource,
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

    override suspend fun checkCompatibility(request: CompatibilityCheckRequest): Result<CompatibilityReport> =
        remoteDataSource.checkCompatibility(request.toDto()).map { it.toDomain() }

    override suspend fun saveBuild(request: SaveBuildRequest): Result<Build> {
        val dto = request.toDto()
        val buildId = request.buildId
        val result = if (!buildId.isNullOrBlank()) {
            remoteDataSource.updateBuild(buildId, dto)
        } else {
            remoteDataSource.saveBuild(dto)
        }
        return result.map { it.toDomain() }
    }
}