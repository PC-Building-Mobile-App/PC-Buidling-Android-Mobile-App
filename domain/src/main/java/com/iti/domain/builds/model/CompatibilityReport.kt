package com.iti.domain.builds.model

data class CompatibilityReport(
    val compatible: Boolean,
    val issues: List<CompatibilityIssue> = emptyList(),
    val warnings: List<CompatibilityIssue> = emptyList(),
)

data class CompatibilityIssue(
    val rule: String,
    val message: String,
)