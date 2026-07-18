package com.iti.domain.builds.model

data class Build(
    val id: String,
    val categoryId: String,
    val name: String,
    val price: Double,
    val currency: String = "EGP",
    val imageUrl: String,
    val performanceScore: Int,
    val avgFps: Int,
    val compatibilityPercent: Int,
    val specs: List<BuildSpec>,
)

data class BuildSpec(
    val category: String,
    val name: String,
    val imageUrl: String?,
)