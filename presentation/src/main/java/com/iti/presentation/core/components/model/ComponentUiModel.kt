package com.iti.presentation.core.components.model

import kotlinx.serialization.Serializable

@Serializable
data class ComponentUiModel(
    val id: Long,
    val subtitle: String,
    val productName: String,
    val vendorName: String,
    val formattedPrice: String,
    val imageUrl: String,
    val tags: List<String>,
    val isInStock: Boolean
)