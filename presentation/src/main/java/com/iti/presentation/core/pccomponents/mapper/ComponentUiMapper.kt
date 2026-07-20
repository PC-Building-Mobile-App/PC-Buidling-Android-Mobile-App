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
    imageUrl = productImage.ifEmpty { getPlaceholderImageForCategory(category) },
    tags = buildTags(this),
    isInStock = inStock,
    vendorName = vendorName,
    sourceUrl = sourceUrl,
    matchedGlobalName = matchedGlobalName
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

private fun getPlaceholderImageForCategory(category: String): String {
    return when (category.uppercase()) {
        "CPU" -> "https://images.unsplash.com/photo-1591453089816-0fbb971b454c?w=400&auto=format&fit=crop&q=60"
        "GPU" -> "https://images.unsplash.com/photo-1591488320449-011701bb6704?w=400&auto=format&fit=crop&q=60"
        "MOTHERBOARD" -> "https://images.unsplash.com/photo-1518770660439-4636190af475?w=400&auto=format&fit=crop&q=60"
        "MEMORY" -> "https://images.unsplash.com/photo-1562976540-1502c2145186?w=400&auto=format&fit=crop&q=60"
        "STORAGE" -> "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400&auto=format&fit=crop&q=60"
        "PSU" -> "https://images.unsplash.com/photo-1587202372775-e229f172b9d7?w=400&auto=format&fit=crop&q=60"
        "CASE" -> "https://images.unsplash.com/photo-1555664424-778a1e5e1b48?w=400&auto=format&fit=crop&q=60"
        "COOLER" -> "https://images.unsplash.com/photo-1625805708453-2949ff12b9ff?w=400&auto=format&fit=crop&q=60"
        else -> "https://images.unsplash.com/photo-1587202372775-e229f172b9d7?w=400&auto=format&fit=crop&q=60" // Generic fallback
    }
}