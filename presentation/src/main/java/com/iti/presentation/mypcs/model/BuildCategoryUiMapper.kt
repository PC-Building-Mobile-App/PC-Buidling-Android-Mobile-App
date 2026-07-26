package com.iti.presentation.mypcs.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.R
import com.iti.presentation.ui.theme.AiWorkstationGradient
import com.iti.presentation.ui.theme.AiWorkstationGradientStart
import com.iti.presentation.ui.theme.GamingGradient
import com.iti.presentation.ui.theme.GamingGradientStart
import com.iti.presentation.ui.theme.OfficeGradient
import com.iti.presentation.ui.theme.OfficeGradientStart
import com.iti.presentation.ui.theme.ProgrammingGradient
import com.iti.presentation.ui.theme.ProgrammingGradientStart

fun BuildCategory.toUiModel(): BuildCategoryUiModel = BuildCategoryUiModel(
    id = id,
    name = name,
    description = description,
    buildsCount = buildsCount,
    type = type,
)

val BuildCategoryType.gradient: Brush
    get() = when (this) {
        BuildCategoryType.GAMING -> GamingGradient
        BuildCategoryType.PROGRAMMING -> ProgrammingGradient
        BuildCategoryType.OFFICE -> OfficeGradient
        BuildCategoryType.AI_WORKSTATION -> AiWorkstationGradient
    }

val BuildCategoryType.accentColor: Color
    get() = when (this) {
        BuildCategoryType.GAMING -> GamingGradientStart
        BuildCategoryType.PROGRAMMING -> ProgrammingGradientStart
        BuildCategoryType.OFFICE -> OfficeGradientStart
        BuildCategoryType.AI_WORKSTATION -> AiWorkstationGradientStart
    }

@get:DrawableRes
val BuildCategoryType.iconRes: Int
    get() = when (this) {
        BuildCategoryType.GAMING -> R.drawable.ic_build_gaming
        BuildCategoryType.PROGRAMMING -> R.drawable.ic_build_programming
        BuildCategoryType.OFFICE -> R.drawable.ic_build_office
        BuildCategoryType.AI_WORKSTATION -> R.drawable.ic_build_ai
    }