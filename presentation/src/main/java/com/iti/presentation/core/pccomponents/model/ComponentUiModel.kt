package com.iti.presentation.core.pccomponents.model

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
    val isInStock: Boolean,
    val sourceUrl: String? = null,
    val matchedGlobalName: String? = null
)