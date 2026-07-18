package com.iti.presentation.core.components.mapper

import com.iti.domain.components.model.Component
import com.iti.presentation.core.components.model.ComponentUiModel
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class ComponentUiMapper @Inject constructor() {

    fun mapToUiModel(domainModel: Component): ComponentUiModel {
        return ComponentUiModel(
            id = domainModel.id,
            subtitle = "${domainModel.vendorName} · ${domainModel.category}",
            productName = domainModel.productName,
            formattedPrice = formatPriceToEGP(domainModel.price),
            imageUrl = domainModel.productImage,
            tags = buildTags(domainModel),
            isInStock = domainModel.inStock
        )
    }

    fun mapToUiModels(domainModels: List<Component>): List<ComponentUiModel> {
        return domainModels.map { mapToUiModel(it) }
    }

    private fun formatPriceToEGP(price: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
            maximumFractionDigits = 0
        }
        return "${numberFormat.format(price)} EGP"
    }

    private fun buildTags(component: Component): List<String> {
        val tags = mutableListOf<String>()

        if (component.price >= 15000.0) {
            tags.add("Top Pick")
        }

        if (component.inStock) {
            tags.add("In Stock")
        } else {
            tags.add("Out of Stock")
        }

        return tags
    }
}