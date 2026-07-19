package com.iti.data.componentcategories.datasource

import com.iti.data.componentcategories.model.ComponentCategoryDataModel
import kotlinx.coroutines.flow.Flow

interface ComponentCategoryDataSource {
    fun getCategories(): Flow<List<ComponentCategoryDataModel>>
}