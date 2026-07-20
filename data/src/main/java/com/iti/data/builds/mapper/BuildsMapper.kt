package com.iti.data.builds.mapper

import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.BuildItemDto
import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.domain.components.model.Component

fun BuildCategoryDto.toDomain(): BuildCategory = BuildCategory(
    id = id,
    name = name,
    description = description,
    buildsCount = buildsCount,
    type = runCatching { BuildCategoryType.valueOf(type) }.getOrDefault(BuildCategoryType.GAMING),
)

fun BuildItemDto.toDomain(): Component = Component(
    id = id,
    vendorName = "",
    category = category,
    productName = productName,
    productImage = "",
    price = price,
    inStock = true,
)

fun BuildDto.toDomain(): Build = Build(
    id = id.toString(),
    name = name,
    totalPrice = totalPrice,
    compatible = compatible,
    items = items.map { it.toDomain() },
    issues = issues.orEmpty(),
    createdAt = createdAt,
    updatedAt = updatedAt,
)
