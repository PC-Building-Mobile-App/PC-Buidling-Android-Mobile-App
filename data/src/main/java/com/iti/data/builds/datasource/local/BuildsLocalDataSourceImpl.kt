package com.iti.data.builds.datasource.local

import androidx.room.withTransaction
import com.iti.data.builds.local.dao.BuildDao
import com.iti.data.builds.mapper.toDto
import com.iti.data.builds.mapper.toEntity
import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.core.database.AppDatabase
import com.iti.domain.builds.model.BuildCategoryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BuildsLocalDataSourceImpl @Inject constructor(
    private val buildDao: BuildDao,
    private val appDatabase: AppDatabase,
) : BuildsLocalDataSource {

    override suspend fun getCachedBuildsByCategory(categoryId: String): List<BuildDto> =
        buildDao.getBuildsByCategory(categoryId).map { it.toDto() }

    override suspend fun getAllCachedBuilds(): List<BuildDto> =
        buildDao.getAllBuilds().map { it.toDto() }

    override suspend fun cacheBuildsForCategory(categoryId: String, builds: List<BuildDto>) {
        appDatabase.withTransaction {
            buildDao.clearCategory(categoryId)
            buildDao.insertBuilds(builds.map { it.toEntity() })
            buildDao.insertItems(builds.flatMap { build -> build.items.map { it.toEntity(build.id) } })
            buildDao.insertIssues(builds.flatMap { build -> build.issues.orEmpty().map { it.toEntity(build.id) } })
        }
    }

    override suspend fun saveBuildLocally(build: BuildDto) {
        appDatabase.withTransaction {
            buildDao.insertBuilds(listOf(build.toEntity()))
            buildDao.insertItems(build.items.map { it.toEntity(build.id) })
            buildDao.insertIssues(build.issues.orEmpty().map { it.toEntity(build.id) })
        }
    }

    override fun getCachedCategoryCounts(): Flow<List<BuildCategoryDto>> =
        buildDao.getCategoriesCount().map { counted ->
            val byType = counted.associateBy { it.type }
            BuildCategoryType.entries.map { type ->
                byType[type.name] ?: BuildCategoryDto(type = type.name, buildsCount = 0)
            }
        }
}