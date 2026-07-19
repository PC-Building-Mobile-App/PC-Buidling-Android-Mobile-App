package com.iti.presentation.core.pccomponents.model

data class ComponentUiModel(
    val id: Long,
    val subtitle: String,
    val productName: String,
    val formattedPrice: String,
    val imageUrl: String,
    val tags: List<String>,
    val isInStock: Boolean
)