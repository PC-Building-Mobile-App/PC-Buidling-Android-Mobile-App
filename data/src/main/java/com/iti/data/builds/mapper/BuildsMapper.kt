package com.iti.data.builds.mapper

import com.iti.data.builds.model.AlternativeDto
import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.BuildIssueDto
import com.iti.data.builds.model.BuildItemDto
import com.iti.domain.builds.model.AlternativeOption
import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.domain.builds.model.BuildIssue
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.domain.components.model.Component

fun BuildCategoryDto.toDomain(): BuildCategory = BuildCategory(
    id = id,
    name = name,
    description = description,
    buildsCount = buildsCount,
    type = runCatching { BuildCategoryType.valueOf(type) }.getOrDefault(BuildCategoryType.GAMING),
)

// FIXED: Safely converts string to Enum and returns null if the category is unsupported
fun BuildItemDto.toDomain(): Component? {
    val enumCategory = runCatching { ComponentCategoryType.valueOf(category.uppercase()) }.getOrNull()
        ?: return null

    return Component(
        id = id,
        vendorName = "",
        category = enumCategory,
        productName = productName,
        productImage = "",
        price = price,
        inStock = true,
    )
}

fun BuildIssueDto.toDomain(): BuildIssue = BuildIssue(
    category = category,
    reason = reason,
)

fun AlternativeDto.toDomain(): AlternativeOption = AlternativeOption(
    id = id,
    name = name,
    price = price,
)

fun BuildDto.toDomain(): Build = Build(
    id = id.toString(),
    name = name,
    totalPrice = totalPrice,
    compatible = compatible,
    items = items?.mapNotNull { it.toDomain() }.orEmpty(),
    issues = issues?.map { it.toDomain() }.orEmpty(),
    alternatives = alternatives?.mapValues { (_, list) -> list.map { it.toDomain() } }.orEmpty(),
    createdAt = createdAt,
    updatedAt = updatedAt,
)