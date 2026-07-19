package com.iti.data.builds.model

import com.iti.data.components.model.ComponentDataModel
import kotlinx.serialization.Serializable

@Serializable
data class GenerateBuildRequestDto(
    val budget: Double,
    val purpose: List<String> = emptyList(),
    val brandPreference: List<String> = emptyList(),
    val mode: String = "NEW",
    val existingComponentIds: List<Long> = emptyList(),
)

@Serializable
data class GeneratedBuildDto(
    val components: List<ComponentDataModel>,
    val totalPrice: Double,
    val compatibilityReport: CompatibilityReportDto,
)

@Serializable
data class CompatibilityReportDto(
    val compatible: Boolean,
    val issues: List<CompatibilityIssueDto> = emptyList(),
    val warnings: List<CompatibilityIssueDto> = emptyList(),
)

@Serializable
data class CompatibilityIssueDto(
    val rule: String,
    val message: String,
)

@Serializable
data class CompatibilityCheckRequestDto(
    val buildId: String? = null,
    val existingComponentIds: List<Long>? = null,
    val candidateComponentId: Long,
    val mode: String = "RULE_BASED",
)
@Serializable
data class SaveBuildRequestDto(
    val name: String,
    val componentIds: List<Long>,
    val categoryId: String,
    val buildId: String? = null,
)