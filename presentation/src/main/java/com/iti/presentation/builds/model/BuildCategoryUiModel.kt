package com.iti.presentation.builds.model

import com.iti.domain.builds.model.BuildCategoryType

data class BuildCategoryUiModel(
    val id: String,
    val name: String,
    val description: String,
    val buildsCount: Int,
    val type: BuildCategoryType,
)
