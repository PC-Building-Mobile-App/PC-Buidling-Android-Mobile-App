package com.iti.presentation.categorybuilds.model

import com.iti.domain.builds.model.Build
import com.iti.domain.builds.model.BuildSpec
import java.text.NumberFormat
import java.util.Locale

data class BuildSpecUiModel(
    val category: String,
    val name: String,
    val imageUrl: String?,
)

data class BuildUiModel(
    val id: String,
    val name: String,
    val priceFormatted: String,
    val imageUrl: String,
    val performanceScore: Int,
    val avgFps: Int,
    val compatibilityPercent: Int,
    val specs: List<BuildSpecUiModel>,
)

fun Build.toUiModel(): BuildUiModel = BuildUiModel(
    id = id,
    name = name,
    priceFormatted = "${NumberFormat.getNumberInstance(Locale.US).format(price.toLong())} $currency",
    imageUrl = imageUrl,
    performanceScore = performanceScore,
    avgFps = avgFps,
    compatibilityPercent = compatibilityPercent,
    specs = specs.map { it.toUiModel() },
)

private fun BuildSpec.toUiModel(): BuildSpecUiModel = BuildSpecUiModel(
    category = category,
    name = name,
    imageUrl = imageUrl,
)