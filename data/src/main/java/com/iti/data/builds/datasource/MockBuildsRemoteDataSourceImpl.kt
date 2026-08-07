package com.iti.data.builds.datasource

import com.iti.data.builds.model.AlternativeDto
import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.BuildIssueDto
import com.iti.data.builds.model.BuildItemDto
import com.iti.data.builds.model.CompareBuildsRequestDto
import com.iti.data.builds.model.ComparisonDto
import com.iti.data.builds.model.CompatibilityCheckRequestDto
import com.iti.data.builds.model.CompatibilityIssueDto
import com.iti.data.builds.model.CompatibilityReportDto
import com.iti.data.builds.model.GenerateBuildRequestDto
import com.iti.data.builds.model.GeneratedBuildDto
import com.iti.data.builds.model.SaveBuildRequestDto
import com.iti.data.components.datasource.ComponentDataSource
import com.iti.data.components.model.ComponentDataModel
import com.iti.data.util.safeCall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class MockBuildsRemoteDataSourceImpl @Inject constructor(
    private val componentDataSource: ComponentDataSource,
) : BuildsRemoteDataSource {

    private val mutex = Mutex()
    private val buildIdCounter = AtomicLong(1000L)
    private val savedBuilds: MutableMap<String, MutableList<BuildDto>> =
        seedBuilds.mapValues { (_, builds) -> builds.toMutableList() }.toMutableMap()

    private suspend fun catalog(): List<ComponentDataModel> =
        componentDataSource.getComponents().first()
            .filter { it.category.uppercase() in supportedCategoryNames }

    override suspend fun getBuildCategories(): Result<List<BuildCategoryDto>> = safeCall {
        delay(2000.milliseconds)
        mutex.withLock {
            mockBuildCategories.map { category ->
                category.copy(buildsCount = savedBuilds[category.type]?.size ?: 0)
            }
        }
    }

    override suspend fun getBuildsByCategory(categoryId: String): Result<List<BuildDto>> = safeCall {
        delay(1500.milliseconds)
        mutex.withLock { savedBuilds[categoryId.uppercase()]?.toList() ?: emptyList() }
    }

    override suspend fun generateBuild(request: GenerateBuildRequestDto): Result<GeneratedBuildDto> = safeCall {
        delay(2500.milliseconds)
        val fullCatalog = catalog()

        val components = fullCatalog.distinctByCategory()

        GeneratedBuildDto(
            components = components,
            totalPrice = components.sumOf { it.price },
            reasoning = "Generated build based on prompt: ${request.prompt}",
            compatibilityOk = true,
        )
    }

    override suspend fun checkCompatibility(request: CompatibilityCheckRequestDto): Result<CompatibilityReportDto> = safeCall {
        delay(800.milliseconds)
        val fullCatalog = catalog()
        val candidate = fullCatalog.firstOrNull { it.id == request.candidateComponentId }
        val existingIds = request.existingComponentIds.orEmpty()

        val hasSameCategorySelected = existingIds.any { existingId ->
            val existingCategory = fullCatalog.firstOrNull { it.id == existingId }?.category
                ?: categoryForSavedItemId(existingId)
            existingCategory != null && existingCategory.equals(candidate?.category, ignoreCase = true)
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
        val fullCatalog = catalog()
        val now = "2026-07-20T12:17:17.000000"

        mutex.withLock {
            val items = request.items.mapNotNull { itemReq ->
                fullCatalog.firstOrNull { it.id == itemReq.productId }?.toItem()
            }
            val totalPrice = items.sumOf { it.subtotal }

            val isIncompatible = request.name.equals("My Gaming Rig", ignoreCase = true)

            val compatible = !isIncompatible
            val issues = if (isIncompatible) {
                listOf(
                    BuildIssueDto(
                        category = "MOTHERBOARD",
                        reason = "CPU socket (AM5) does not match motherboard socket (LGA1700)."
                    )
                )
            } else null

            val alternatives = if (isIncompatible) {
                mapOf(
                    "MOTHERBOARD" to listOf(
                        AlternativeDto(id = 33L, name = "ASUS TUF GAMING X870-PLUS WIFI...", price = 17500.00)
                    ),
                    "CPU" to listOf(
                        AlternativeDto(id = 21L, name = "Intel Core i5 12400F...", price = 8000.00)
                    )
                )
            } else null

            val newBuild = BuildDto(
                id = buildIdCounter.incrementAndGet().toInt(),
                name = request.name,
                type = request.type,
                typeDisplayName = request.type,
                totalPrice = totalPrice,
                compatible = compatible,
                items = items,
                issues = issues,
                alternatives = alternatives,
                createdAt = now,
                updatedAt = now,
            )
            savedBuilds.getOrPut(request.type.uppercase()) { mutableListOf() }.add(0, newBuild)
            newBuild
        }
    }

    override suspend fun updateBuild(buildId: String, request: SaveBuildRequestDto): Result<BuildDto> = safeCall {
        saveBuild(request).getOrThrow()
    }

    override suspend fun compareBuilds(request: CompareBuildsRequestDto): Result<ComparisonDto> = safeCall {
        delay(2000.milliseconds)
        ComparisonDto(
            buildIds = emptyList(),
            buildNames = request.buildNames,
            comparisonSummary = "AI Summary: Comparing ${request.buildNames.joinToString(" and ")}",
            keyDifferences = listOf(
                "Different CPU architectures: Intel vs AMD",
                "Memory capacity variations",
                "GPU performance tiers"
            ),
            recommendation = "Based on your needs, ${request.buildNames.firstOrNull() ?: "the first build"} is recommended."
        )
    }

    override suspend fun getBuildById(id: String): Result<BuildDto> = safeCall {
        delay(1000.milliseconds)
        mutex.withLock {
            var found: BuildDto? = null
            savedBuilds.values.forEach { list ->
                val match = list.find { it.id.toString() == id }
                if (match != null) found = match
            }
            found ?: throw Exception("Build not found")
        }
    }

    override suspend fun getAllBuilds(): Result<List<BuildDto>> = safeCall {
        delay(1000.milliseconds)
        mutex.withLock {
            savedBuilds.values.flatten().distinctBy { it.id }
        }
    }


    private fun categoryForSavedItemId(id: Long): String? {
        savedBuilds.values.forEach { builds ->
            builds.forEach { build ->
                build.items.firstOrNull { it.id == id }?.let { return it.category }
            }
        }
        return null
    }

    private fun ComponentDataModel.toItem(): BuildItemDto = BuildItemDto(
        id = id,
        productName = productName,
        category = category,
        price = price,
        quantity = 1,
        subtotal = price,
    )

    private fun List<ComponentDataModel>.distinctByCategory(): List<ComponentDataModel> = distinctBy { it.category }

    private companion object {
        val singleSlotCategories = setOf("CPU", "MOTHERBOARD", "PSU", "CASE", "COOLER")
        val supportedCategoryNames = setOf("CPU", "MOTHERBOARD", "GPU", "PSU", "CASE", "COOLER", "MEMORY")

        val seedBuilds: Map<String, List<BuildDto>> = mapOf(
            "GAMING" to listOf(
                BuildDto(
                    id = 12,
                    name = "My Gaming Rig",
                    type = "GAMING",
                    typeDisplayName = "Gaming",
                    totalPrice = 45230.00,
                    compatible = false,
                    items = listOf(
                        BuildItemDto(101L, "AMD Ryzen 9 7950X", "CPU", 35000.0, 1, 35000.0),
                        BuildItemDto(202L, "MSI MAG B760 TOMAHAWK", "MOTHERBOARD", 6500.0, 1, 6500.0),
                        BuildItemDto(307L, "Aerocool Cylon Mini", "CASE", 1200.0, 1, 1200.0),
                        BuildItemDto(306L, "EVGA 600 W1 White", "PSU", 1500.0, 1, 1500.0),
                        BuildItemDto(304L, "16 GB DDR5", "MEMORY", 1500.0, 1, 1500.0)
                    ),
                    issues = listOf(
                        BuildIssueDto(
                            category = "MOTHERBOARD",
                            reason = "CPU socket (AM5) does not match motherboard socket (LGA1700)."
                        )
                    ),
                    alternatives = mapOf(
                        "MOTHERBOARD" to listOf(
                            AlternativeDto(id = 33L, name = "ASUS TUF GAMING X870-PLUS WIFI...", price = 17500.00)
                        ),
                        "CPU" to listOf(
                            AlternativeDto(id = 21L, name = "Intel Core i5 12400F...", price = 8000.00)
                        )
                    ),
                    createdAt = "2026-07-20T12:17:17.000000",
                    updatedAt = "2026-07-20T12:17:17.000000"
                ),
                BuildDto(
                    id = 1,
                    name = "Ultimate 4K Gaming Rig",
                    type = "GAMING",
                    typeDisplayName = "Gaming",
                    totalPrice = 125500.0,
                    compatible = true,
                    items = listOf(
                        BuildItemDto(101L, "AMD Ryzen 9 7950X", "CPU", 35000.0, 1, 35000.0),
                        BuildItemDto(102L, "ASUS ROG Crosshair X670E", "MOTHERBOARD", 18000.0, 1, 18000.0),
                        BuildItemDto(103L, "NVIDIA RTX 4090", "GPU", 45000.0, 1, 45000.0),
                        BuildItemDto(104L, "64 GB DDR5", "MEMORY", 6500.0, 1, 6500.0),
                        BuildItemDto(106L, "Corsair AX1600i Titanium", "PSU", 9000.0, 1, 9000.0),
                        BuildItemDto(107L, "Lian Li O11 Vision", "CASE", 4500.0, 1, 4500.0),
                        BuildItemDto(108L, "ASUS ROG RYUJIN III AIO", "COOLER", 7500.0, 1, 7500.0),
                    ),
                    issues = null,
                    alternatives = null,
                    createdAt = "2026-07-20T12:17:17.000000",
                    updatedAt = "2026-07-20T12:17:17.000000",
                ),
                BuildDto(
                    id = 2,
                    name = "Mid-Range 1440p Master",
                    type = "GAMING",
                    typeDisplayName = "Gaming",
                    totalPrice = 54700.0,
                    compatible = true,
                    items = listOf(
                        BuildItemDto(201L, "Intel Core i5-13600K", "CPU", 14000.0, 1, 14000.0),
                        BuildItemDto(202L, "MSI MAG B760 TOMAHAWK", "MOTHERBOARD", 6500.0, 1, 6500.0),
                        BuildItemDto(203L, "NVIDIA RTX 4070", "GPU", 22000.0, 1, 22000.0),
                        BuildItemDto(204L, "32 GB DDR5", "MEMORY", 4200.0, 1, 4200.0),
                        BuildItemDto(206L, "Corsair RM750e Gold", "PSU", 3500.0, 1, 3500.0),
                        BuildItemDto(207L, "NZXT H5 Flow", "CASE", 2500.0, 1, 2500.0),
                        BuildItemDto(208L, "DeepCool AK620 Air", "COOLER", 2000.0, 1, 2000.0),
                    ),
                    issues = null,
                    alternatives = null,
                    createdAt = "2026-07-20T12:17:17.000000",
                    updatedAt = "2026-07-20T12:17:17.000000",
                ),
                BuildDto(
                    id = 3,
                    name = "Budget 1080p Blaster",
                    type = "GAMING",
                    typeDisplayName = "Gaming",
                    totalPrice = 27700.0,
                    compatible = true,
                    items = listOf(
                        BuildItemDto(301L, "AMD Ryzen 5 7600", "CPU", 9000.0, 1, 9000.0),
                        BuildItemDto(302L, "Gigabyte B650M DS3H", "MOTHERBOARD", 4500.0, 1, 4500.0),
                        BuildItemDto(303L, "NVIDIA RTX 4060", "GPU", 9500.0, 1, 9500.0),
                        BuildItemDto(304L, "16 GB DDR5", "MEMORY", 1500.0, 1, 1500.0),
                        BuildItemDto(306L, "EVGA 600 W1 White", "PSU", 1500.0, 1, 1500.0),
                        BuildItemDto(307L, "Aerocool Cylon Mini", "CASE", 1200.0, 1, 1200.0),
                        BuildItemDto(308L, "AMD Wraith Stealth", "COOLER", 500.0, 1, 500.0),
                    ),
                    issues = null,
                    alternatives = null,
                    createdAt = "2026-07-20T12:17:17.000000",
                    updatedAt = "2026-07-20T12:17:17.000000",
                ),
            ),
        )

        val mockBuildCategories = listOf(
            BuildCategoryDto("GAMING", 0),
            BuildCategoryDto("PROGRAMMING", 0),
            BuildCategoryDto("OFFICE", 0),
            BuildCategoryDto("AI_WORKSTATION", 0),
        )
    }
}
