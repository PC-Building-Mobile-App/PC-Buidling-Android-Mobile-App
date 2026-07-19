package com.iti.presentation.core.pccomponents.mapper

import com.iti.domain.components.model.Component
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import java.text.NumberFormat
import java.util.Locale

fun Component.toUiModel(): ComponentUiModel = ComponentUiModel(
    id = id,
    subtitle = "$vendorName · $category",
    productName = productName,
    formattedPrice = formatPriceToEGP(price),
    imageUrl = productImage,
    tags = buildTags(this),
    isInStock = inStock,
)

fun List<Component>.toUiModels(): List<ComponentUiModel> = map { it.toUiModel() }

private fun formatPriceToEGP(price: Double): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }
    return "${numberFormat.format(price)} EGP"
}

private fun buildTags(component: Component): List<String> {
    val tags = mutableListOf<String>()
    if (component.price >= 15000.0) tags.add("Top Pick")
    tags.add(if (component.inStock) "In Stock" else "Out of Stock")
    return tags
}