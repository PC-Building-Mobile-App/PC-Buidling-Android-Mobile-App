package com.iti.data.components.mapper

import com.iti.data.components.model.ComponentDataModel
import com.iti.domain.components.model.Component

fun ComponentDataModel.toDomain(): Component = Component(
    id = id,
    vendorName = vendorName,
    category = category,
    productName = productName,
    productImage = productImage,
    price = price,
    inStock = inStock,
    sourceUrl = sourceUrl,
    matchedGlobalName = matchedGlobalName,
    specs = specs
)

fun List<ComponentDataModel>.toDomain(): List<Component> = map { it.toDomain() }