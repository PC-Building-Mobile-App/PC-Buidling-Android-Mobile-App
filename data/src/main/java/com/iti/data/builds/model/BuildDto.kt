package com.iti.data.builds.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuildDto(
    val id: Int,
    val name: String,
    val type: String = "",
    val typeDisplayName: String = "",
    val totalPrice: Double,
    val compatible: Boolean,
    val items: List<BuildItemDto>,
    val issues: List<BuildIssueDto>? = null,
    val alternatives: Map<String, List<AlternativeDto>>? = null,
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class BuildItemDto(
    @SerialName("productId") val id: Long,
    @SerialName("name") val productName: String,
    val category: String,
    val price: Double,
    val quantity: Int,
    val subtotal: Double,
    val imageUrl: String? = null,
    val images: List<String> = emptyList(),
)

@Serializable
data class BuildIssueDto(
    val category: String,
    val reason: String,
)

@Serializable
data class AlternativeDto(
    val id: Long,
    val name: String,
    val price: Double,
)