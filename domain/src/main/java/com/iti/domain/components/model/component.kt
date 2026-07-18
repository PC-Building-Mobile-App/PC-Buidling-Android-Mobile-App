package com.iti.domain.components.model

data class Component (
    val id: Long,
    val vendorName: String,
    val category: String,
    val productName: String,
    val productImage: String,
    val price: Double,
    val inStock: Boolean
)