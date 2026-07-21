package com.iti.data.components.mapper

import com.iti.data.components.model.ComponentDataModel
import com.iti.domain.components.model.Component
import com.iti.domain.componentcategories.model.ComponentCategoryType

fun ComponentDataModel.toDomain(): Component? {
    val enumCategory = runCatching { ComponentCategoryType.valueOf(category.uppercase()) }.getOrNull()
        ?: return null

    return Component(
        id = id,
        vendorName = vendorName,
        category = enumCategory,
        productName = productName,
        productImage = productImage,
        price = price,
        inStock = inStock,
        sourceUrl = sourceUrl,
        matchedGlobalName = matchedGlobalName,
        specs = specs
    )
}

// Use mapNotNull to automatically drop any nulls (unsupported categories)
fun List<ComponentDataModel>.toDomain(): List<Component> = mapNotNull { it.toDomain() }