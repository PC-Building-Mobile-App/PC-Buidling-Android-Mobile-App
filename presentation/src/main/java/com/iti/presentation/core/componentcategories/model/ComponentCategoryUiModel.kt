package com.iti.presentation.core.componentcategories.model

import androidx.annotation.DrawableRes

data class ComponentCategoryUiModel(
    val id: String,
    val title: String,
    val subtitle: String,
    @DrawableRes val iconRes: Int
)