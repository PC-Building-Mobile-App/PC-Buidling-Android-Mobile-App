package com.iti.data.builds.model

import com.iti.data.components.model.ComponentDataModel
import kotlinx.serialization.Serializable

@Serializable
data class GenerateBuildRequestDto(
    val prompt: String,
    val budget: Double,
    val usage: String,
    val preferredBrand: String? = null,
)

@Serializable
data class GeneratedBuildDto(
    val components: List<ComponentDataModel>,
    val totalPrice: Double,
    val reasoning: String = "",
    val compatibilityOk: Boolean = true,
)

@Serializable
data class CompatibilityReportDto(
    val compatible: Boolean,
    val issues: List<CompatibilityIssueDto> = emptyList(),
    val warnings: List<CompatibilityIssueDto> = emptyList(),
    val explanation: String = "",
    val resolvedByRuleEngine: Boolean = false,
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
data class BundleItemRequestDto(
    val productId: Long,
    val quantity: Int = 1,
)

@Serializable
data class SaveBuildRequestDto(
    val name: String,
    val type: String,
    val items: List<BundleItemRequestDto>,
)