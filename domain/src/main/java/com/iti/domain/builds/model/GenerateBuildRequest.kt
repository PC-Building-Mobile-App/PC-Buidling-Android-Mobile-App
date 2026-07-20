package com.iti.domain.builds.model

data class GenerateBuildRequest(
    val budget: Double,
    val purpose: List<BuildCategoryType> = emptyList(),
    val brandPreference: List<String> = emptyList(),
    val mode: BuildGenerationMode = BuildGenerationMode.NEW,
    val existingComponentIds: List<Long> = emptyList(),
) {
    init {
        require(mode == BuildGenerationMode.NEW || existingComponentIds.isNotEmpty()) {
            "existingComponentIds is required when mode is FILL_MISSING or REPLACE"
        }
    }

    companion object {
        fun create(
            budget: Double,
            purpose: List<BuildCategoryType> = emptyList(),
            brandPreference: List<String> = emptyList(),
            isEditingExistingBuild: Boolean,
            existingComponentIds: List<Long>,
        ): GenerateBuildRequest {
            val safeMode = when {
                existingComponentIds.isEmpty() -> BuildGenerationMode.NEW
                isEditingExistingBuild -> BuildGenerationMode.REPLACE
                else -> BuildGenerationMode.FILL_MISSING
            }
            return GenerateBuildRequest(
                budget = budget,
                purpose = purpose,
                brandPreference = brandPreference,
                mode = safeMode,
                existingComponentIds = existingComponentIds,
            )
        }
    }
}


enum class BuildGenerationMode {
    NEW,
    FILL_MISSING,
    REPLACE,
}

enum class CompatibilityMode {
    RULE_BASED,
    AI,
}