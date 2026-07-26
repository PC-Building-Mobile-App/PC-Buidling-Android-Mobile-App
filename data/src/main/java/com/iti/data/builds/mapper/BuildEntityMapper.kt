package com.iti.data.builds.mapper

import com.iti.data.builds.local.entity.BuildEntity
import com.iti.data.builds.local.entity.BuildIssueEntity
import com.iti.data.builds.local.entity.BuildItemEntity
import com.iti.data.builds.local.entity.BuildWithItemsAndIssues
import com.iti.data.builds.model.BuildDto
import com.iti.data.builds.model.BuildIssueDto
import com.iti.data.builds.model.BuildItemDto

fun BuildDto.toEntity(): BuildEntity = BuildEntity(
    id = id,
    name = name,
    type = type,
    typeDisplayName = typeDisplayName,
    totalPrice = totalPrice,
    compatible = compatible,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun BuildItemDto.toEntity(buildId: Int): BuildItemEntity = BuildItemEntity(
    buildId = buildId,
    productId = id,
    productName = productName,
    category = category,
    price = price,
    quantity = quantity,
    subtotal = subtotal,
    imageUrl = imageUrl,
    images = images,
)

fun BuildIssueDto.toEntity(buildId: Int): BuildIssueEntity = BuildIssueEntity(
    buildId = buildId,
    category = category,
    reason = reason,
)

fun BuildItemEntity.toDto(): BuildItemDto = BuildItemDto(
    id = productId,
    productName = productName,
    category = category,
    price = price,
    quantity = quantity,
    subtotal = subtotal,
    imageUrl = imageUrl,
    images = images,
)

fun BuildIssueEntity.toDto(): BuildIssueDto = BuildIssueDto(
    category = category,
    reason = reason,
)

fun BuildWithItemsAndIssues.toDto(): BuildDto = BuildDto(
    id = build.id,
    name = build.name,
    type = build.type,
    typeDisplayName = build.typeDisplayName,
    totalPrice = build.totalPrice,
    compatible = build.compatible,
    items = items.map { it.toDto() },
    issues = issues.map { it.toDto() },
    alternatives = null,
    createdAt = build.createdAt,
    updatedAt = build.updatedAt,
)
