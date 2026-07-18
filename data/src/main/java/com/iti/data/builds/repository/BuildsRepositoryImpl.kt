package com.iti.data.builds.repository

import com.iti.data.builds.datasource.BuildsRemoteDataSource
import com.iti.data.builds.mapper.toDomain
import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.repository.BuildsRepository
import javax.inject.Inject

class BuildsRepositoryImpl @Inject constructor(
    private val remoteDataSource: BuildsRemoteDataSource,
) : BuildsRepository {

    override suspend fun getBuildCategories(): Result<List<BuildCategory>> =
        remoteDataSource.getBuildCategories().map { dto -> dto.map { it.toDomain() } }

    override suspend fun getBuildsByCategory(categoryId: String): Result<List<Build>> =
        remoteDataSource.getBuildsByCategory(categoryId).map { dto -> dto.map { it.toDomain() } }
}