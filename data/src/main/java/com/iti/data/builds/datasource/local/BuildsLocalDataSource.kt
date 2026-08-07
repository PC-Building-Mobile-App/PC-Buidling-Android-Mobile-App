package com.iti.data.builds.datasource.local

import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import kotlinx.coroutines.flow.Flow

interface BuildsLocalDataSource {
    suspend fun getCachedBuildsByCategory(categoryId: String): List<BuildDto>
    suspend fun getAllCachedBuilds(): List<BuildDto>
    suspend fun cacheBuildsForCategory(categoryId: String, builds: List<BuildDto>)
    suspend fun saveBuildLocally(build: BuildDto)
    fun getCachedCategoryCounts(): Flow<List<BuildCategoryDto>>
}