package com.iti.data.builds.datasource

import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.CompatibilityCheckRequestDto
import com.iti.data.builds.model.CompatibilityIssueDto
import com.iti.data.builds.model.CompatibilityReportDto
import com.iti.data.builds.model.GenerateBuildRequestDto
import com.iti.data.builds.model.GeneratedBuildDto
import com.iti.data.builds.model.SaveBuildRequestDto
import com.iti.data.components.model.ComponentDataModel
import com.iti.data.util.safeCall
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds
@Singleton
class MockBuildsRemoteDataSourceImpl @Inject constructor() : BuildsRemoteDataSource {

    private val mutex = Mutex()
    private val buildIdCounter = AtomicLong(1000L)
    private val savedBuilds: MutableMap<String, MutableList<BuildDto>> =
        seedBuilds.mapValues { (_, builds) -> builds.toMutableList() }.toMutableMap()

    override suspend fun getBuildCategories(): Result<List<BuildCategoryDto>> = safeCall {
        delay(2000.milliseconds)
        mockBuildCategories
    }

    override suspend fun getBuildsByCategory(categoryId: String): Result<List<BuildDto>> = safeCall {
        delay(1500.milliseconds)
        mutex.withLock { savedBuilds[categoryId]?.toList() ?: emptyList() }
    }

    override suspend fun generateBuild(request: GenerateBuildRequestDto): Result<GeneratedBuildDto> = safeCall {
        delay(2500.milliseconds)
        val existing = mockComponentCatalog.filter { it.id in request.existingComponentIds }
        val coveredCategories = existing.map { it.category }.toSet()

        val components = if (request.mode == "NEW") {
            mockComponentCatalog.distinctByCategory()
        } else {
            existing + mockComponentCatalog
                .filter { it.category !in coveredCategories }
                .distinctByCategory()
        }

        GeneratedBuildDto(
            components = components,
            totalPrice = components.sumOf { it.price },
            compatibilityReport = CompatibilityReportDto(compatible = true),
        )
    }

    override suspend fun checkCompatibility(request: CompatibilityCheckRequestDto): Result<CompatibilityReportDto> = safeCall {
        delay(800.milliseconds)
        val candidate = mockComponentCatalog.firstOrNull { it.id == request.candidateComponentId }
        val existingIds = request.existingComponentIds.orEmpty()
        val hasSameCategorySelected = mockComponentCatalog.any {
            it.id in existingIds && it.category.equals(candidate?.category, ignoreCase = true)
        }

        val issues = if (candidate != null &&
            singleSlotCategories.any { it.equals(candidate.category, ignoreCase = true) } &&
            hasSameCategorySelected
        ) {
            listOf(
                CompatibilityIssueDto(
                    rule = "${candidate.category.uppercase()}_ALREADY_SELECTED",
                    message = "A ${candidate.category.lowercase()} is already part of this build.",
                ),
            )
        } else {
            emptyList()
        }

        CompatibilityReportDto(compatible = issues.isEmpty(), issues = issues)
    }

    override suspend fun saveBuild(request: SaveBuildRequestDto): Result<BuildDto> = safeCall {
        delay(1200.milliseconds)
        val components = mockComponentCatalog.filter { it.id in request.componentIds }
        val report = CompatibilityReportDto(compatible = true)

        mutex.withLock {
            val existingEntry = request.buildId?.let { id -> findBuildById(id) }

            if (existingEntry != null) {
                val (oldCategoryId, oldBuild) = existingEntry
                val updated = oldBuild.copy(
                    categoryId = request.categoryId,
                    name = request.name,
                    price = components.sumOf { it.price },
                    imageUrl = components.firstOrNull()?.productImage.orEmpty(),
                    performanceScore = performanceScore(components),
                    avgFps = avgFps(components),
                    compatibilityPercent = compatibilityPercent(report),
                    specs = components,
                )
                savedBuilds[oldCategoryId]?.removeAll { it.id == updated.id }
                savedBuilds.getOrPut(request.categoryId) { mutableListOf() }.add(0, updated)
                updated
            } else {
                val newBuild = BuildDto(
                    id = "build_${buildIdCounter.incrementAndGet()}",
                    categoryId = request.categoryId,
                    name = request.name,
                    price = components.sumOf { it.price },
                    imageUrl = components.firstOrNull()?.productImage.orEmpty(),
                    performanceScore = performanceScore(components),
                    avgFps = avgFps(components),
                    compatibilityPercent = compatibilityPercent(report),
                    specs = components,
                )
                savedBuilds.getOrPut(request.categoryId) { mutableListOf() }.add(0, newBuild)
                newBuild
            }
        }
    }
    private fun findBuildById(buildId: String): Pair<String, BuildDto>? {
        savedBuilds.forEach { (categoryId, builds) ->
            val match = builds.firstOrNull { it.id == buildId }
            if (match != null) return categoryId to match
        }
        return null
    }

    private fun performanceScore(components: List<ComponentDataModel>): Int {
        val gpuPrice = components.firstOrNull { it.category.equals("GPU", ignoreCase = true) }?.price ?: 0.0
        val cpuPrice = components.firstOrNull { it.category.equals("CPU", ignoreCase = true) }?.price ?: 0.0
        val weighted = gpuPrice * 0.6 + cpuPrice * 0.4
        return (weighted / 500).toInt().coerceIn(0, 100)
    }

    private fun avgFps(components: List<ComponentDataModel>): Int {
        val gpuPrice = components.firstOrNull { it.category.equals("GPU", ignoreCase = true) }?.price ?: return 0
        return (gpuPrice / 250).toInt().coerceIn(0, 300)
    }

    private fun compatibilityPercent(report: CompatibilityReportDto): Int {
        if (report.compatible) return 100
        val penalty = report.issues.size * 15 + report.warnings.size * 5
        return (100 - penalty).coerceIn(0, 100)
    }

    private fun List<ComponentDataModel>.distinctByCategory(): List<ComponentDataModel> = distinctBy { it.category }

    private companion object {
        val singleSlotCategories = setOf("CPU", "MOTHERBOARD", "PSU", "CASE", "COOLER")

        val mockComponentCatalog = listOf(
            ComponentDataModel(1, "TechStore", "CPU", "AMD Ryzen 7 7800X3D", "https://placehold.co/400x400/png?text=Ryzen+7", 24999.0, true, mapOf("Socket" to "AM5", "TDP" to "120W")),
            ComponentDataModel(2, "TechStore", "MOTHERBOARD", "ASUS ROG STRIX B650E-F", "https://placehold.co/400x400/png?text=Motherboard", 12500.0, true, mapOf("Socket" to "AM5", "FormFactor" to "ATX", "RamType" to "DDR5")),
            ComponentDataModel(3, "TechStore", "GPU", "RTX 4070 SUPER 12GB", "https://placehold.co/400x400/png?text=GPU", 38499.0, true, mapOf("VRAM" to "12GB", "LengthMm" to "285")),
            ComponentDataModel(4, "TechStore", "MEMORY", "32GB DDR5 6000 CL30", "https://placehold.co/400x400/png?text=RAM", 6199.0, true, mapOf("RamType" to "DDR5")),
            ComponentDataModel(5, "TechStore", "STORAGE", "Samsung 990 PRO 2TB", "https://placehold.co/400x400/png?text=SSD", 5499.0, true, mapOf("Type" to "NVMe", "Capacity" to "2TB")),
            ComponentDataModel(6, "TechStore", "PSU", "Corsair RM850x", "https://placehold.co/400x400/png?text=PSU", 4299.0, true, mapOf("Wattage" to "850")),
            ComponentDataModel(7, "TechStore", "CASE", "Lian Li O11 Dynamic", "https://placehold.co/400x400/png?text=Case", 3499.0, true, mapOf("MaxGpuLengthMm" to "420", "MaxCoolerHeightMm" to "167")),
            ComponentDataModel(8, "TechStore", "COOLER", "Noctua NH-D15", "https://placehold.co/400x400/png?text=Cooler", 4200.0, true, mapOf("HeightMm" to "165")),
        )

        val mockBuildCategories = listOf(
            BuildCategoryDto("gaming", "Gaming", "High FPS, max settings", 3, "GAMING"),
            BuildCategoryDto("programming", "Programming", "Fast compile, multitasking", 3, "PROGRAMMING"),
            BuildCategoryDto("content_creation", "Content Creation", "4K editing, rendering", 3, "CONTENT_CREATION"),
            BuildCategoryDto("office", "Office", "Productivity & speed", 3, "OFFICE"),
            BuildCategoryDto("ai_workstation", "AI & Workstation", "ML training, inference", 3, "AI_WORKSTATION"),
            BuildCategoryDto("dream_builds", "Dream Builds", "No budget limits", 3, "DREAM_BUILDS"),
        )

        val seedBuilds: Map<String, List<BuildDto>> = mapOf(
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
                        ComponentDataModel(101, "TechStore", "CPU", "AMD Ryzen 9 7950X", "https://picsum.photos/seed/cpu1/100/100", 35000.0, true, mapOf("Socket" to "AM5")),
                        ComponentDataModel(103, "TechStore", "GPU", "NVIDIA RTX 4090", "https://picsum.photos/seed/gpu1/100/100", 45000.0, true, mapOf("VRAM" to "24GB")),
                        ComponentDataModel(104, "TechStore", "MEMORY", "64 GB DDR5", "https://picsum.photos/seed/ram1/100/100", 6500.0, true, mapOf("RamType" to "DDR5")),
                        ComponentDataModel(105, "TechStore", "STORAGE", "2 TB NVMe", "https://picsum.photos/seed/storage1/100/100", 3000.0, true, mapOf("Type" to "NVMe"))
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
                        ComponentDataModel(201, "TechStore", "CPU", "Intel Core i5-13600K", "https://placehold.co/400x400/png?text=i5", 14000.0, true, mapOf("Socket" to "LGA1700")),
                        ComponentDataModel(203, "TechStore", "GPU", "NVIDIA RTX 4070", "https://placehold.co/400x400/png?text=RTX4070", 22000.0, true, mapOf("VRAM" to "12GB")),
                        ComponentDataModel(204, "TechStore", "MEMORY", "32 GB DDR5", "https://placehold.co/400x400/png?text=RAM", 4200.0, true, mapOf("RamType" to "DDR5")),
                        ComponentDataModel(205, "TechStore", "STORAGE", "1 TB NVMe", "https://placehold.co/400x400/png?text=SSD", 1800.0, true, mapOf("Type" to "NVMe"))
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
                        ComponentDataModel(301, "TechStore", "CPU", "AMD Ryzen 5 7600", "https://placehold.co/400x400/png?text=Ryzen5", 9000.0, true, mapOf("Socket" to "AM5")),
                        ComponentDataModel(303, "TechStore", "GPU", "NVIDIA RTX 4060", "https://placehold.co/400x400/png?text=RTX4060", 9500.0, true, mapOf("VRAM" to "8GB")),
                        ComponentDataModel(304, "TechStore", "MEMORY", "16 GB DDR5", "https://placehold.co/400x400/png?text=RAM", 1500.0, true, mapOf("RamType" to "DDR5")),
                        ComponentDataModel(305, "TechStore", "STORAGE", "500 GB NVMe", "https://placehold.co/400x400/png?text=SSD", 1000.0, true, mapOf("Type" to "NVMe"))
                    ),
                ),
            ),
        )
    }
}