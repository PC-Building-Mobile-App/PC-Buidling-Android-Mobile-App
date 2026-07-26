package com.iti.data.builds.model

import kotlinx.serialization.Serializable

@Serializable
data class CompareBuildsRequestDto(
    val buildIds: List<Int> = emptyList(),
    val buildNames: List<String> = emptyList()
)

@Serializable
data class ComparisonDto(
    val buildIds: List<Int> = emptyList(),
    val buildNames: List<String> = emptyList(),
    val comparisonSummary: String = "",
    val keyDifferences: List<String> = emptyList(),
    val recommendation: String = ""
)
