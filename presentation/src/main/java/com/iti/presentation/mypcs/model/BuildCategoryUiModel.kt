package com.iti.presentation.mypcs.model

import com.iti.domain.builds.model.BuildCategoryType
import kotlinx.serialization.Serializable

@Serializable
data class BuildCategoryUiModel(
    val buildsCount: Int,
    val type: BuildCategoryType,
)
