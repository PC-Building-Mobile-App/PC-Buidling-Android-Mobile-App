package com.iti.domain.builds.model

import com.iti.domain.components.model.Component

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
    val specs: List<Component>,
)