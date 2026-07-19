package com.iti.presentation.buildgeneration.model

import com.iti.domain.components.model.Component
import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Locale

@Serializable
data class PickerComponentUiModel(
    val id: Long,
    val vendorName: String,
    val category: String,
    val productName: String,
    val productImage: String,
    val price: Double,
    val priceFormatted: String,
    val inStock: Boolean,
)

fun Component.toPickerUiModel(): PickerComponentUiModel = PickerComponentUiModel(
    id = id,
    vendorName = vendorName,
    category = category,
    productName = productName,
    productImage = productImage,
    price = price,
    priceFormatted = "${NumberFormat.getNumberInstance(Locale.US).format(price.toLong())} EGP", // Appended currency for cleaner UI
    inStock = inStock,
)
