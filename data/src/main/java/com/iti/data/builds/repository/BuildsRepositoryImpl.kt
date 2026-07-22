package com.iti.data.builds.repository

import com.iti.data.builds.datasource.BuildsRemoteDataSource
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildsRepositoryImpl @Inject constructor(
    private val remoteDataSource: BuildsRemoteDataSource,
) : BuildsRepository {

    override suspend fun getBuildCategories(): Result<List<BuildCategory>> =
        remoteDataSource.getBuildCategories().map { dto -> dto.map { it.toDomain() } }

    override suspend fun getBuildsByCategory(categoryId: String): Result<List<Build>> =
        remoteDataSource.getBuildsByCategory(categoryId)
            .map { dto -> dto.map { it.toDomain() } }

    override suspend fun generateBuild(request: GenerateBuildRequest): Result<GeneratedBuild> =
        remoteDataSource.generateBuild(request.toDto())
            .map { it.toDomain() }

    override suspend fun checkCompatibility(request: CompatibilityCheckRequest): Result<CompatibilityReport> =
        remoteDataSource.checkCompatibility(request.toDto()).map { it.toDomain() }

    override suspend fun saveBuild(request: SaveBuildRequest): Result<Build> =
        remoteDataSource.saveBuild(request.toDto())
            .map { it.toDomain() }
}