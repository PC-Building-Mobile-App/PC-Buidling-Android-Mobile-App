package com.iti.domain.builds.model

data class BuildComparison(
    val buildIds: List<Int>,
    val buildNames: List<String>,
    val comparisonSummary: String,
    val keyDifferences: List<String>,
    val recommendation: String
)

