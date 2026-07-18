package com.iti.data.stats.mapper

import com.iti.data.stats.model.PlatformStatDataModel
import com.iti.domain.stats.model.PlatformStat
import com.iti.domain.stats.model.StatType
import javax.inject.Inject

class PlatformStatDataMapper @Inject constructor() {

    fun mapToDomain(dataModel: PlatformStatDataModel): PlatformStat {
        val type = when (dataModel.id.uppercase()) {
            "BUILDS" -> StatType.BUILDS
            "PARTS" -> StatType.PARTS
            "STORES" -> StatType.STORES
            else -> throw IllegalArgumentException("Unknown stat id: ${dataModel.id}")
        }

        return PlatformStat(
            type = type,
            label = dataModel.label,
            count = dataModel.count,
            isApproximated = dataModel.isApproximated
        )
    }

    fun mapToDomainList(dataModels: List<PlatformStatDataModel>): List<PlatformStat> {
        return dataModels.map { mapToDomain(it) }
    }
}