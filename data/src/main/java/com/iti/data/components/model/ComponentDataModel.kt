package com.iti.data.components.model
import kotlinx.serialization.Serializable

@Serializable
data class ComponentDataModel(
    val id: Long,
    val vendorName: String,
    val category: String,
    val productName: String,
    val productImage: String,
    val price: Double,
    val inStock: Boolean,
    val specs: Map<String, String>? = null
)