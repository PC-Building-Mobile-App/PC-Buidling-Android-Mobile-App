package com.iti.presentation.componentcategories.mapper

import com.iti.domain.componentcategories.model.ComponentCategory
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.R
import com.iti.presentation.componentcategories.model.ComponentCategoryUiModel
import javax.inject.Inject

class ComponentCategoryUiMapper @Inject constructor() {

    fun mapToUiModel(domainModel: ComponentCategory): ComponentCategoryUiModel {
        return ComponentCategoryUiModel(
            id = domainModel.type.name,
            title = domainModel.displayName,
            subtitle = "${domainModel.availablePartsCount} parts",
            iconRes = getIconResource(domainModel.type)
        )
    }

    fun mapToUiModels(domainModels: List<ComponentCategory>): List<ComponentCategoryUiModel> {
        return domainModels.map { mapToUiModel(it) }
    }

    private fun getIconResource(type: ComponentCategoryType): Int {
        return when (type) {
            ComponentCategoryType.CPU -> R.drawable.ic_cpu
            ComponentCategoryType.MOTHERBOARD -> R.drawable.ic_motherboard
            ComponentCategoryType.GPU -> R.drawable.ic_gpu
            ComponentCategoryType.PSU -> R.drawable.ic_psu
            ComponentCategoryType.CASE -> R.drawable.ic_case
            ComponentCategoryType.COOLER -> R.drawable.ic_cooler
            ComponentCategoryType.MEMORY -> R.drawable.ic_memory
        }
    }
}