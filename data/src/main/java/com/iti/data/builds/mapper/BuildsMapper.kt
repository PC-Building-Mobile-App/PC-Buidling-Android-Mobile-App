package com.iti.data.builds.mapper

import com.iti.data.builds.model.BuildCategoryDto
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.BuildSpecDto
import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildCategory
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.domain.builds.model.BuildSpec

fun BuildCategoryDto.toDomain(): BuildCategory = BuildCategory(
    id = id,
    name = name,
    description = description,
    buildsCount = buildsCount,
    type = runCatching { BuildCategoryType.valueOf(type) }.getOrDefault(BuildCategoryType.GAMING),
)

fun BuildDto.toDomain(): Build = Build(
    id = id,
    categoryId = categoryId,
    name = name,
    price = price,
    currency = currency,
    imageUrl = imageUrl,
    performanceScore = performanceScore,
    avgFps = avgFps,
    compatibilityPercent = compatibilityPercent,
    specs = specs.map { it.toDomain() },
)

fun BuildSpecDto.toDomain(): BuildSpec = BuildSpec(
    category = category,
    name = name,
    imageUrl = imageUrl,
)