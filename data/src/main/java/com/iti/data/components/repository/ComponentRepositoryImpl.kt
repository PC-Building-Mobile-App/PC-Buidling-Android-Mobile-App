package com.iti.data.components.repository

import com.iti.data.components.datasource.ComponentDataSource
import com.iti.data.components.mapper.ComponentDataMapper
import com.iti.domain.components.model.Component
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ComponentRepositoryImpl @Inject constructor(
    private val mockDataSource: ComponentDataSource,
    private val mapper: ComponentDataMapper
) : ComponentRepository {

    override fun getComponents(): Flow<List<Component>> {
        return mockDataSource.getComponents().map { dataModels ->
            mapper.mapToDomainList(dataModels)
        }
    }

    override fun getComponentById(id: Long): Flow<Component?> {
        return mockDataSource.getComponentById(id).map { dataModel ->
            dataModel?.let { mapper.mapToDomain(it) }
        }
    }
}