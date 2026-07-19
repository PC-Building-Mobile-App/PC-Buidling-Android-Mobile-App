package com.iti.data.componentCategories.mapper

import com.iti.data.componentCategories.model.ComponentCategoryDataModel
import com.iti.domain.componentcategories.model.ComponentCategory
import com.iti.domain.componentcategories.model.ComponentCategoryType

fun ComponentCategoryDataModel.toDomain(): ComponentCategory = ComponentCategory(
    type = runCatching { ComponentCategoryType.valueOf(id.uppercase()) }
        .getOrDefault(ComponentCategoryType.CPU),
    displayName = name,
    availablePartsCount = partsCount,
)

fun List<ComponentCategoryDataModel>.toDomain(): List<ComponentCategory> = map { it.toDomain() }