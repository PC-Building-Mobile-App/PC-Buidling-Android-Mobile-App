package com.iti.data.components.repository

import com.iti.data.components.datasource.ComponentDataSource
import com.iti.data.components.mapper.toDomain
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.domain.components.model.Component
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ComponentRepositoryImpl @Inject constructor(
    private val mockDataSource: ComponentDataSource
) : ComponentRepository {

    override fun getComponents(): Flow<List<Component>> {
        return mockDataSource.getComponents().map { dataModels ->
            dataModels.toDomain()
        }
    }

    override fun getComponentById(id: Long): Flow<Component?> {
        return mockDataSource.getComponentById(id).map { dataModel ->
            dataModel?.toDomain()
        }
    }

    override fun getComponentsByCategory(category: ComponentCategoryType): Flow<List<Component>> {
        return mockDataSource.getComponents().map { dataModels ->
            dataModels
                .filter { it.category.equals(category.name, ignoreCase = true) }
                .map { it.toDomain() }
        }
    }
}