package com.iti.data.builds.model

import kotlinx.serialization.Serializable

@Serializable
data class BuildCategoryDto(
    val type: String,
    val buildsCount: Int,
)