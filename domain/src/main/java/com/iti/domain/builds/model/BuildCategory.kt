package com.iti.domain.builds.model

data class BuildCategory(
    val id: String,
    val name: String,
    val description: String,
    val buildsCount: Int,
    val type: BuildCategoryType,
)

enum class BuildCategoryType {
    GAMING,
    PROGRAMMING,
    CONTENT_CREATION,
    OFFICE,
    AI_WORKSTATION,
    DREAM_BUILDS,
}