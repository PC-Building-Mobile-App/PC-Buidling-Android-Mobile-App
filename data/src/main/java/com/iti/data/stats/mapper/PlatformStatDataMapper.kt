package com.iti.data.stats.mapper

import com.iti.data.stats.model.PlatformStatDataModel
import com.iti.domain.stats.model.PlatformStat
import com.iti.domain.stats.model.StatType

fun PlatformStatDataModel.toDomain(): PlatformStat = PlatformStat(
    type = runCatching { StatType.valueOf(id.uppercase()) }
        .getOrDefault(StatType.BUILDS),
    label = label,
    count = count,
    isApproximated = isApproximated,
)

fun List<PlatformStatDataModel>.toDomain(): List<PlatformStat> = map { it.toDomain() }