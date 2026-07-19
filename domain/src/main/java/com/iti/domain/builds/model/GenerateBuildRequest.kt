package com.iti.domain.builds.model


data class GenerateBuildRequest(
    val budget: Double,
    val purpose: List<BuildPurpose> = emptyList(),
    val brandPreference: List<String> = emptyList(),
    val mode: BuildGenerationMode = BuildGenerationMode.NEW,
    val existingComponentIds: List<Long> = emptyList(),
) {
    init {
        require(mode == BuildGenerationMode.NEW || existingComponentIds.isNotEmpty()) {
            "existingComponentIds is required when mode is FILL_MISSING or REPLACE"
        }
    }
}
enum class BuildPurpose {
    GAMING, STREAMING, WORKSTATION, BUDGET, SFF, VIDEO_EDIT, AI_ML, ARCHITECTURE, DESIGN
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

