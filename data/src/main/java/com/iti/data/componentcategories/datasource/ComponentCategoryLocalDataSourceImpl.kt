package com.iti.data.componentcategories.datasource

import com.iti.data.componentcategories.model.ComponentCategoryDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ComponentCategoryLocalDataSourceImpl @Inject constructor() : ComponentCategoryDataSource {

    override fun getCategories(): Flow<List<ComponentCategoryDataModel>> {
        val staticCategories = listOf(
            ComponentCategoryDataModel(id = "CPU", name = "Processors", partsCount = 94),
            ComponentCategoryDataModel(id = "MOTHERBOARD", name = "Motherboards", partsCount = 205),
            ComponentCategoryDataModel(id = "GPU", name = "Graphics", partsCount = 180),
            ComponentCategoryDataModel(id = "PSU", name = "Power", partsCount = 139),
            ComponentCategoryDataModel(id = "CASE", name = "Cases", partsCount = 373),
            ComponentCategoryDataModel(id = "COOLER", name = "Cooling", partsCount = 389),
            ComponentCategoryDataModel(id = "MEMORY", name = "Memory", partsCount = 66)
        )
        return flowOf(staticCategories)
    }
}