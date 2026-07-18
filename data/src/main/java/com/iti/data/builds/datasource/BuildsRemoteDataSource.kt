package com.iti.data.builds.datasource

import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.util.safeCall
import javax.inject.Inject

interface BuildsRemoteDataSource {
    suspend fun getBuildCategories(): Result<List<BuildCategoryDto>>
}

class BuildsRemoteDataSourceImpl @Inject constructor() : BuildsRemoteDataSource {

    override suspend fun getBuildCategories(): Result<List<BuildCategoryDto>> = safeCall {
        mockBuildCategories
    }

    private companion object {
        val mockBuildCategories = listOf(
            BuildCategoryDto("gaming", "Gaming", "High FPS, max settings", 3, "GAMING"),
            BuildCategoryDto("programming", "Programming", "Fast compile, multitasking", 3, "PROGRAMMING"),
            BuildCategoryDto("content_creation", "Content Creation", "4K editing, rendering", 3, "CONTENT_CREATION"),
            BuildCategoryDto("office", "Office", "Productivity & speed", 3, "OFFICE"),
            BuildCategoryDto("ai_workstation", "AI & Workstation", "ML training, inference", 3, "AI_WORKSTATION"),
            BuildCategoryDto("dream_builds", "Dream Builds", "No budget limits", 3, "DREAM_BUILDS"),
        )
    }
}