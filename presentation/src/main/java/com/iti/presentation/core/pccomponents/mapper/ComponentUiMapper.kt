package com.iti.presentation.core.pccomponents.mapper

import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.domain.components.model.Component
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import java.text.NumberFormat
import java.util.Locale

fun Component.toUiModel(): ComponentUiModel {
    val formattedCategory = category.name.lowercase().replaceFirstChar { it.uppercase() }


    val cleanProductImage = productImage.replace("null", "").trim()
    val cleanImages = images.filter { it.isNotBlank() && it != "null" }

    return ComponentUiModel(
        id = id,
        subtitle = "$formattedCategory · $vendorName",
        productName = productName,
        formattedPrice = formatPriceToEGP(price),
        imageUrl = cleanProductImage,
        tags = buildTags(this),
        isInStock = inStock,
        vendorName = vendorName,
        sourceUrl = sourceUrl,
        matchedGlobalName = matchedGlobalName,
        category = category,
        price = price,
        specs = specs,
        images = cleanImages
    )
}

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