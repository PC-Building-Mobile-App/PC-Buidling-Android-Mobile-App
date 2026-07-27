package com.iti.domain.builds.model

data class BuildCategory(
    val type: BuildCategoryType,
    val buildsCount: Int,
)

enum class BuildCategoryType {
    GAMING,
    PROGRAMMING,
    OFFICE,
    AI_WORKSTATION,
}