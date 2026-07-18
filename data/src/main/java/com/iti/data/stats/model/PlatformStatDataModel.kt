package com.iti.data.stats.model

data class PlatformStatDataModel(
    val id: String,
    val label: String,
    val count: Int,
    val isApproximated: Boolean
)