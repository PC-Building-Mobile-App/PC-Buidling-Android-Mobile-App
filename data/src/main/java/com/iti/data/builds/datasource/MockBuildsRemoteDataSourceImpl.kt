package com.iti.data.builds.datasource

import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.BuildSpecDto
import com.iti.data.builds.model.CompatibilityCheckRequestDto
import com.iti.data.builds.model.CompatibilityIssueDto
import com.iti.data.builds.model.CompatibilityReportDto
import com.iti.data.builds.model.GenerateBuildRequestDto
import com.iti.data.builds.model.GeneratedBuildDto
import com.iti.data.builds.model.SaveBuildRequestDto
import com.iti.data.components.model.ComponentDataModel
import com.iti.data.util.safeCall
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class MockBuildsRemoteDataSourceImpl @Inject constructor() : BuildsRemoteDataSource {

    override suspend fun getBuildCategories(): Result<List<BuildCategoryDto>> = safeCall {
        delay(2000.milliseconds)
        mockBuildCategories
    }

    override suspend fun getBuildsByCategory(categoryId: String): Result<List<BuildDto>> = safeCall {
        delay(1500.milliseconds)
        mockBuilds[categoryId] ?: emptyList()
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

        BuildDto(
            id = "build_${System.currentTimeMillis()}",
            categoryId = "",
            name = request.name,
            price = components.sumOf { it.price },
            imageUrl = components.firstOrNull()?.productImage.orEmpty(),
            performanceScore = performanceScore(components),
            avgFps = avgFps(components),
            compatibilityPercent = compatibilityPercent(report),
            specs = components.map {
                BuildSpecDto(category = it.category, name = it.productName, imageUrl = it.productImage)
            },
        )
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
            ComponentDataModel(
                id = 1,
                vendorName = "TechStore",
                category = "CPU",
                productName = "AMD Ryzen 7 7800X3D",
                productImage = "https://cdn.example.com/img/1.jpg",
                price = 24999.0,
                inStock = true,
                specs = mapOf("Socket" to "AM5", "TDP" to "120W"),
            ),
            ComponentDataModel(
                id = 2,
                vendorName = "TechStore",
                category = "MOTHERBOARD",
                productName = "ASUS ROG STRIX B650E-F",
                productImage = "https://cdn.example.com/img/2.jpg",
                price = 12500.0,
                inStock = true,
                specs = mapOf("Socket" to "AM5", "FormFactor" to "ATX", "RamType" to "DDR5"),
            ),
            ComponentDataModel(
                id = 3,
                vendorName = "TechStore",
                category = "GPU",
                productName = "RTX 4070 SUPER 12GB",
                productImage = "https://cdn.example.com/img/3.jpg",
                price = 38499.0,
                inStock = true,
                specs = mapOf("VRAM" to "12GB", "LengthMm" to "285"),
            ),
            ComponentDataModel(
                id = 4,
                vendorName = "TechStore",
                category = "MEMORY",
                productName = "32GB DDR5 6000 CL30",
                productImage = "https://cdn.example.com/img/4.jpg",
                price = 6199.0,
                inStock = true,
                specs = mapOf("RamType" to "DDR5"),
            ),
            ComponentDataModel(
                id = 5,
                vendorName = "TechStore",
                category = "STORAGE",
                productName = "Samsung 990 PRO 2TB",
                productImage = "https://cdn.example.com/img/5.jpg",
                price = 5499.0,
                inStock = true,
                specs = mapOf("Type" to "NVMe", "Capacity" to "2TB"),
            ),
            ComponentDataModel(
                id = 6,
                vendorName = "TechStore",
                category = "PSU",
                productName = "Corsair RM850x",
                productImage = "https://cdn.example.com/img/6.jpg",
                price = 4299.0,
                inStock = true,
                specs = mapOf("Wattage" to "850"),
            ),
            ComponentDataModel(
                id = 7,
                vendorName = "TechStore",
                category = "CASE",
                productName = "Lian Li O11 Dynamic",
                productImage = "https://cdn.example.com/img/7.jpg",
                price = 3499.0,
                inStock = true,
                specs = mapOf("MaxGpuLengthMm" to "420", "MaxCoolerHeightMm" to "167"),
            ),
            ComponentDataModel(
                id = 8,
                vendorName = "TechStore",
                category = "COOLER",
                productName = "Noctua NH-D15",
                productImage = "https://cdn.example.com/img/8.jpg",
                price = 4200.0,
                inStock = true,
                specs = mapOf("HeightMm" to "165"),
            ),
        )

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