package com.iti.presentation.home.mapper

import com.iti.domain.stats.model.PlatformStat
import com.iti.presentation.home.model.PlatformStatUiModel
import java.text.NumberFormat
import java.util.Locale

fun PlatformStat.toUiModel(): PlatformStatUiModel = PlatformStatUiModel(
    id = type.name,
    formattedValue = formatCount(count, isApproximated),
    label = label.uppercase(),
)

fun List<PlatformStat>.toUiModels(): List<PlatformStatUiModel> = map { it.toUiModel() }

private fun formatCount(count: Int, isApproximated: Boolean): String {
    return when {
        isApproximated && count >= 1000 -> "${count / 1000}K+"
        else -> NumberFormat.getNumberInstance(Locale.US).format(count)
    }
}