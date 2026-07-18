package com.iti.domain.stats.model

data class PlatformStat(
    val type: StatType,
    val label: String,
    val count: Int,
    val isApproximated: Boolean
)