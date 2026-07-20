package com.iti.domain.components.model

import com.iti.domain.componentcategories.model.ComponentCategoryType

data class SearchParams(
    val query: String? = null,
    val category: ComponentCategoryType? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val inStockOnly: Boolean = false
)
