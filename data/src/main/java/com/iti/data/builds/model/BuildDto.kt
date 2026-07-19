package com.iti.data.builds.model
import kotlinx.serialization.Serializable

@Serializable
data class BuildDto(
    val id: String,
    val categoryId: String,
    val name: String,
    val price: Double,
    val currency: String = "EGP",
    val imageUrl: String,
    val performanceScore: Int,
    val avgFps: Int,
    val compatibilityPercent: Int,
    val specs: List<BuildSpecDto>,
)

@Serializable
data class BuildSpecDto(
    val category: String,
    val name: String,
    val imageUrl: String? = null,
)
