package com.iti.domain.builds.model

import com.iti.domain.components.model.Component

data class Build(
    val id: String,
    val name: String,
    val category: BuildCategoryType = BuildCategoryType.GAMING,
    val totalPrice: Double,
    val compatible: Boolean,
    val items: List<Component>,
    val issues: List<BuildIssue>,
    val alternatives: Map<String, List<AlternativeOption>>,
    val createdAt: String,
    val updatedAt: String,
)


data class BuildIssue(
    val category: String,
    val reason: String,
)

data class AlternativeOption(
    val id: Long,
    val name: String,
    val price: Double,
)