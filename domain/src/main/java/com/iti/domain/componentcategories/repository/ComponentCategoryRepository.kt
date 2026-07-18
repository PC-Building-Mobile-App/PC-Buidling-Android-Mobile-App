package com.iti.domain.componentcategories.repository

import com.iti.domain.componentcategories.model.ComponentCategory
import kotlinx.coroutines.flow.Flow

interface ComponentCategoryRepository {
    fun getComponentCategories(): Flow<List<ComponentCategory>>
}