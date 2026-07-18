package com.iti.presentation.home.mapper

import com.iti.domain.stats.model.PlatformStat
import com.iti.presentation.home.model.PlatformStatUiModel
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class PlatformStatUiMapper @Inject constructor() {

    fun mapToUiModel(domainModel: PlatformStat): PlatformStatUiModel {
        return PlatformStatUiModel(
            id = domainModel.type.name,
            formattedValue = formatCount(domainModel.count, domainModel.isApproximated),
            label = domainModel.label.uppercase()
        )
    }

    fun mapToUiModels(domainModels: List<PlatformStat>): List<PlatformStatUiModel> {
        return domainModels.map { mapToUiModel(it) }
    }

    private fun formatCount(count: Int, isApproximated: Boolean): String {
        return when {
            isApproximated && count >= 1000 -> "${count / 1000}K+"
            else -> NumberFormat.getNumberInstance(Locale.US).format(count)
        }
    }
}