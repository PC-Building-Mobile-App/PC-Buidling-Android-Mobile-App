package com.iti.domain.components.model

import com.iti.domain.componentcategories.model.ComponentCategoryType

data class Component (
    val id: Long,
    val vendorName: String,
    val category: ComponentCategoryType,
    val productName: String,
    val productImage: String,
    val price: Double,
    val inStock: Boolean,
    val sourceUrl: String? = null,
    val matchedGlobalName: String? = null,
    val specs: Map<String, String> = emptyMap(),
    val images: List<String> = emptyList()
)