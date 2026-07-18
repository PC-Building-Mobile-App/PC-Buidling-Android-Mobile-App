package com.iti.domain.componentcategories.model

data class ComponentCategory(
    val type: ComponentCategoryType,
    val displayName: String,
    val availablePartsCount: Int
)