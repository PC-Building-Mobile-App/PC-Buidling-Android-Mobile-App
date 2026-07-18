package com.iti.data.builds.repository

import com.iti.data.builds.datasource.BuildsRemoteDataSource
import com.iti.data.builds.model.toDomain
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.repository.BuildsRepository
import javax.inject.Inject

class BuildsRepositoryImpl @Inject constructor(
    private val remoteDataSource: BuildsRemoteDataSource,
) : BuildsRepository {

    override suspend fun getBuildCategories(): Result<List<BuildCategory>> =
        remoteDataSource.getBuildCategories().map { dto -> dto.map { it.toDomain() } }
}