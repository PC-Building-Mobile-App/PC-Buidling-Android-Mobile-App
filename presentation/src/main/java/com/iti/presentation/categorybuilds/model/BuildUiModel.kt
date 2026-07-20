package com.iti.presentation.categorybuilds.model

import com.iti.domain.builds.model.Build
import com.iti.presentation.buildgeneration.model.PickerComponentUiModel
import com.iti.presentation.buildgeneration.model.toPickerUiModel
import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Locale

@Serializable
data class BuildUiModel(
    val id: String,
    val name: String,
    val price: Double,
    val priceFormatted: String,
    val imageUrl: String,
    val performanceScore: Int,
    val avgFps: Int,
    val compatibilityPercent: Int,
    val specs: List<PickerComponentUiModel>,
)
fun Build.toUiModel(): BuildUiModel = BuildUiModel(
    id = id,
    name = name,
    price = price,
    priceFormatted = "${NumberFormat.getNumberInstance(Locale.US).format(price.toLong())} $currency",
    imageUrl = imageUrl,
    performanceScore = performanceScore,
    avgFps = avgFps,
    compatibilityPercent = compatibilityPercent,
    specs = specs.map { it.toPickerUiModel() },
)
