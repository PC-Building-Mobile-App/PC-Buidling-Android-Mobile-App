package com.iti.data.componentcategories.datasource

import com.iti.data.componentcategories.model.ComponentCategoryDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ComponentCategoryLocalDataSourceImpl @Inject constructor() : ComponentCategoryDataSource {

    override fun getCategories(): Flow<List<ComponentCategoryDataModel>> {
        val staticCategories = listOf(
            ComponentCategoryDataModel(id = "CPU", name = "Processors", partsCount = 412),
            ComponentCategoryDataModel(id = "MOTHERBOARD", name = "Motherboards", partsCount = 189),
            ComponentCategoryDataModel(id = "GPU", name = "Graphics", partsCount = 268),
            ComponentCategoryDataModel(id = "PSU", name = "Power", partsCount = 154),
            ComponentCategoryDataModel(id = "CASE", name = "Cases", partsCount = 210),
            ComponentCategoryDataModel(id = "COOLER", name = "Cooling", partsCount = 301),
            ComponentCategoryDataModel(id = "MEMORY", name = "Memory", partsCount = 345)
        )
        return flowOf(staticCategories)
    }
}