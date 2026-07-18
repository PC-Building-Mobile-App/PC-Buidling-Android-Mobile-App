package com.iti.data.componentCategories.repository

import com.iti.data.componentCategories.datasource.ComponentCategoryDataSource
import com.iti.data.componentCategories.mapper.ComponentCategoryDataMapper
import com.iti.domain.componentcategories.model.ComponentCategory
import com.iti.domain.componentcategories.repository.ComponentCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ComponentCategoryRepositoryImpl @Inject constructor(
    private val localDataSource: ComponentCategoryDataSource,
    private val mapper: ComponentCategoryDataMapper
) : ComponentCategoryRepository {

    override fun getComponentCategories(): Flow<List<ComponentCategory>> {
        return localDataSource.getCategories().map { dataModels ->
            mapper.mapToDomainList(dataModels)
        }
    }
}