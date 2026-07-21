package com.iti.presentation.core.pccomponents.model

import com.iti.domain.componentcategories.model.ComponentCategoryType
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
    val matchedGlobalName: String? = null,
    val category: ComponentCategoryType = ComponentCategoryType.CPU,
    val price: Double = 32000.0,
    val specs: Map<String, String> = emptyMap()
)