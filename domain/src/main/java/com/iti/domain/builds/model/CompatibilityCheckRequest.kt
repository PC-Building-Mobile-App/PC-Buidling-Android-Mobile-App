package com.iti.domain.builds.model

sealed interface CompatibilityCheckTarget {
    data class SavedBuild(val buildId: String) : CompatibilityCheckTarget
    data class InProgressSelection(val existingComponentIds: List<Long>) : CompatibilityCheckTarget
}

data class CompatibilityCheckRequest(
    val target: CompatibilityCheckTarget,
    val candidateComponentId: Long,
    val mode: CompatibilityMode = CompatibilityMode.RULE_BASED,
)