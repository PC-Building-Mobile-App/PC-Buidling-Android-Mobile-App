package com.iti.presentation.core.componentcategories.mapper

import com.iti.domain.componentcategories.model.ComponentCategory
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.R
import com.iti.presentation.core.componentcategories.model.ComponentCategoryUiModel


fun ComponentCategory.toUiModel(): ComponentCategoryUiModel = ComponentCategoryUiModel(
    id = type.name,
    title = displayName,
    subtitle = "$availablePartsCount parts",
    iconRes = type.toIconResource(),
)

fun List<ComponentCategory>.toUiModels(): List<ComponentCategoryUiModel> = map { it.toUiModel() }

fun ComponentCategoryType.toIconResource(): Int = when (this) {
    ComponentCategoryType.CPU -> R.drawable.ic_cpu
    ComponentCategoryType.MOTHERBOARD -> R.drawable.ic_motherboard
    ComponentCategoryType.GPU -> R.drawable.ic_gpu
    ComponentCategoryType.PSU -> R.drawable.ic_psu
    ComponentCategoryType.CASE -> R.drawable.ic_case
    ComponentCategoryType.COOLER -> R.drawable.ic_cooler
    ComponentCategoryType.MEMORY -> R.drawable.ic_memory
}