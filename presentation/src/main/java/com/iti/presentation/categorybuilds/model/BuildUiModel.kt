package com.iti.presentation.categorybuilds.model

import com.iti.domain.builds.model.Build
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
    val issues: List<String>,
    val specs: List<ComponentUiModel>,
    val createdAt: String,
    val updatedAt: String,
)

fun Build.toUiModel(): BuildUiModel = BuildUiModel(
    id = id,
    name = name,
    totalPrice = totalPrice,
    priceFormatted = "${NumberFormat.getNumberInstance(Locale.US).format(totalPrice.toLong())} EGP",
    compatible = compatible,
    issues = issues,
    specs = items.toUiModels(),
    createdAt = createdAt,
    updatedAt = updatedAt,
)
