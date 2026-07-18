package com.iti.data.builds.datasource

import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.BuildSpecDto
import com.iti.data.util.safeCall
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

interface BuildsRemoteDataSource {
    suspend fun getBuildCategories(): Result<List<BuildCategoryDto>>
    suspend fun getBuildsByCategory(categoryId: String): Result<List<BuildDto>>
}

class BuildsRemoteDataSourceImpl @Inject constructor() : BuildsRemoteDataSource {

    override suspend fun getBuildCategories(): Result<List<BuildCategoryDto>> = safeCall {
        delay(2000.milliseconds)
        mockBuildCategories
    }

    override suspend fun getBuildsByCategory(categoryId: String): Result<List<BuildDto>> = safeCall {
        delay(1500.milliseconds)
        mockBuilds[categoryId] ?: emptyList()
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

        val mockBuilds: Map<String, List<BuildDto>> = mapOf(
            "gaming" to listOf(
                BuildDto(
                    id = "gaming_1",
                    categoryId = "gaming",
                    name = "Ultimate 4K Gaming Rig",
                    price = 89500.0,
                    imageUrl = "https://picsum.photos/seed/gaming1/400/300",
                    performanceScore = 99,
                    avgFps = 165,
                    compatibilityPercent = 100,
                    specs = listOf(
                        BuildSpecDto("CPU", "AMD Ryzen 9 7950X", "https://picsum.photos/seed/cpu1/100/100"),
                        BuildSpecDto("GPU", "NVIDIA RTX 4090", "https://picsum.photos/seed/gpu1/100/100"),
                        BuildSpecDto("RAM", "64 GB DDR5", "https://picsum.photos/seed/ram1/100/100"),
                        BuildSpecDto("STORAGE", "2 TB NVMe", "https://picsum.photos/seed/storage1/100/100"),
                    ),
                ),
                BuildDto(
                    id = "gaming_2",
                    categoryId = "gaming",
                    name = "Mid-Range 1440p Master",
                    price = 42000.0,
                    imageUrl = "https://picsum.photos/seed/gaming2/400/300",
                    performanceScore = 85,
                    avgFps = 144,
                    compatibilityPercent = 100,
                    specs = listOf(
                        BuildSpecDto("CPU", "Intel Core i5-13600K"),
                        BuildSpecDto("GPU", "NVIDIA RTX 4070"),
                        BuildSpecDto("RAM", "32 GB DDR5"),
                        BuildSpecDto("STORAGE", "1 TB NVMe"),
                    ),
                ),
                BuildDto(
                    id = "gaming_3",
                    categoryId = "gaming",
                    name = "Budget 1080p Blaster",
                    price = 21000.0,
                    imageUrl = "https://picsum.photos/seed/gaming3/400/300",
                    performanceScore = 72,
                    avgFps = 120,
                    compatibilityPercent = 100,
                    specs = listOf(
                        BuildSpecDto("CPU", "AMD Ryzen 5 7600"),
                        BuildSpecDto("GPU", "NVIDIA RTX 4060"),
                        BuildSpecDto("RAM", "16 GB DDR5"),
                        BuildSpecDto("STORAGE", "500 GB NVMe"),
                    ),
                ),
            ),
        )
    }
}