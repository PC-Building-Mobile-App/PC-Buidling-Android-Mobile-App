package com.iti.data.componentCategories.datasource

import com.iti.data.componentCategories.model.ComponentCategoryDataModel
import kotlinx.coroutines.flow.Flow

interface ComponentCategoryDataSource {
    fun getCategories(): Flow<List<ComponentCategoryDataModel>>
}