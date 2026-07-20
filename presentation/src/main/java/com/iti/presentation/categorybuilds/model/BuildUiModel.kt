package com.iti.presentation.categorybuilds.model

import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildIssue
import com.iti.domain.builds.model.AlternativeOption
import com.iti.presentation.core.pccomponents.mapper.toUiModels
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Locale

@Serializable
data class BuildUiModel(
    val id: String,
    val name: String,
    val totalPrice: Double,
    val priceFormatted: String,
    val compatible: Boolean,
    val issues: List<BuildIssueUiModel>,
    val alternatives: Map<String, List<AlternativeOptionUiModel>>,
    val specs: List<ComponentUiModel>,
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class BuildIssueUiModel(
    val category: String,
    val reason: String,
)

@Serializable
data class AlternativeOptionUiModel(
    val id: Long,
    val name: String,
    val price: Double,
    val priceFormatted: String,
)

fun BuildIssue.toUiModel(): BuildIssueUiModel = BuildIssueUiModel(
    category = category,
    reason = reason,
)

fun AlternativeOption.toUiModel(): AlternativeOptionUiModel = AlternativeOptionUiModel(
    id = id,
    name = name,
    price = price,
    priceFormatted = "${NumberFormat.getNumberInstance(Locale.US).format(price.toLong())} EGP",
)

fun Build.toUiModel(): BuildUiModel = BuildUiModel(
    id = id,
    name = name,
    totalPrice = totalPrice,
    priceFormatted = "${NumberFormat.getNumberInstance(Locale.US).format(totalPrice.toLong())} EGP",
    compatible = compatible,
    issues = issues.map { it.toUiModel() },
    alternatives = alternatives.mapValues { (_, list) -> list.map { it.toUiModel() } },
    specs = items.toUiModels(),
    createdAt = createdAt,
    updatedAt = updatedAt,
)
