package com.iti.data.componentCategories.mapper

import com.iti.data.componentCategories.model.ComponentCategoryDataModel
import com.iti.domain.componentcategories.model.ComponentCategory
import com.iti.domain.componentcategories.model.ComponentCategoryType
import javax.inject.Inject
import kotlin.collections.map

class ComponentCategoryDataMapper @Inject constructor() {

    fun mapToDomain(dataModel: ComponentCategoryDataModel): ComponentCategory {
        val type = when (dataModel.id.uppercase()) {
            "CPU" -> ComponentCategoryType.CPU
            "MOTHERBOARD" -> ComponentCategoryType.MOTHERBOARD
            "GPU" -> ComponentCategoryType.GPU
            "PSU" -> ComponentCategoryType.PSU
            "CASE" -> ComponentCategoryType.CASE
            "COOLER" -> ComponentCategoryType.COOLER
            "MEMORY" -> ComponentCategoryType.MEMORY
            "STORAGE" -> ComponentCategoryType.STORAGE
            else -> throw IllegalArgumentException("Unknown category id: ${dataModel.id}")
        }

        return ComponentCategory(
            type = type,
            displayName = dataModel.name,
            availablePartsCount = dataModel.partsCount
        )
    }

    fun mapToDomainList(dataModels: List<ComponentCategoryDataModel>): List<ComponentCategory> {
        return dataModels.map { mapToDomain(it) }
    }
}