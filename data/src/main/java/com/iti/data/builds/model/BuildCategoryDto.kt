package com.iti.data.builds.model

import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.model.BuildCategoryType
import kotlinx.serialization.Serializable

@Serializable
data class BuildCategoryDto(
    val id: String,
    val name: String,
    val description: String,
    val buildsCount: Int,
    val type: String,
)
