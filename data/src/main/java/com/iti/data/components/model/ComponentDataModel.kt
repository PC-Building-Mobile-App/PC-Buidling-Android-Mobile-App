package com.iti.data.components.model

data class ComponentDataModel(
    val id: Long,
    val vendorName: String,
    val category: String,
    val productName: String,
    val productImage: String,
    val price: Double,
    val inStock: Boolean
)