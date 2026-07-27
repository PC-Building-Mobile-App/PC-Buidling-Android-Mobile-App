package com.iti.presentation.buildgeneration.model

import androidx.annotation.StringRes
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.R

@get:StringRes
val BuildCategoryType.labelRes: Int
    get() = when (this) {
        BuildCategoryType.GAMING -> R.string.category_type_gaming
        BuildCategoryType.PROGRAMMING -> R.string.category_type_programming
        BuildCategoryType.OFFICE -> R.string.category_type_office
        BuildCategoryType.AI_WORKSTATION -> R.string.category_type_ai_workstation
    }

@get:StringRes
val BuildCategoryType.descriptionRes: Int
    get() = when (this) {
        BuildCategoryType.GAMING -> R.string.category_desc_gaming
        BuildCategoryType.PROGRAMMING -> R.string.category_desc_programming
        BuildCategoryType.OFFICE -> R.string.category_desc_office
        BuildCategoryType.AI_WORKSTATION -> R.string.category_desc_ai_workstation
    }