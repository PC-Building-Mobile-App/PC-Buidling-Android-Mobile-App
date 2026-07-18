package com.iti.presentation.mypcs.model

import com.iti.domain.builds.model.BuildCategoryType
import kotlinx.serialization.Serializable

@Serializable
data class BuildCategoryUiModel(
    val id: String,
    val name: String,
    val description: String,
    val buildsCount: Int,
    val type: BuildCategoryType,
)
