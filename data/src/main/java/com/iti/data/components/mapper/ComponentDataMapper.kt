package com.iti.data.components.mapper

import com.iti.data.components.model.ComponentDataModel
import com.iti.domain.components.model.Component
import javax.inject.Inject

class ComponentDataMapper @Inject constructor() {

    fun mapToDomain(dataModel: ComponentDataModel): Component {
        return Component(
            id = dataModel.id,
            vendorName = dataModel.vendorName,
            category = dataModel.category,
            productName = dataModel.productName,
            productImage = dataModel.productImage,
            price = dataModel.price,
            inStock = dataModel.inStock,
            specs = dataModel.specs.orEmpty(),
            )
    }

    fun mapToDomainList(dataModels: List<ComponentDataModel>): List<Component> {
        return dataModels.map { mapToDomain(it) }
    }
}