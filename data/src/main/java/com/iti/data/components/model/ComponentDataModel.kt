package com.iti.data.components.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComponentDataModel(
    val id: Long,
    val category: String,
    @SerialName("name") val productName: String,
    @SerialName("store") val vendorName: String,
    val price: Double,
    val inStock: Boolean,
    val sourceUrl: String? = null,
    val matchedGlobalName: String? = null,
    val specs: Map<String, String> = emptyMap(),
    // The API doesn't return an image right now, so we give it a default empty value
    val productImage: String = ""
)