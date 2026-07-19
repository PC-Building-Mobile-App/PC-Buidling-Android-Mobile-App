package com.iti.data.components.repository

import com.iti.data.components.datasource.ComponentDataSource
import com.iti.data.components.mapper.ComponentDataMapper
import com.iti.data.util.safeCall
import com.iti.domain.components.model.Component
import com.iti.domain.components.model.PageResult
import com.iti.domain.components.model.SearchParams
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

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

    override suspend fun searchComponents(params: SearchParams): Result<PageResult<Component>> = safeCall {
        // todo(switch to real RemoteDataSource once backend is done)

        delay(600.milliseconds)

        val filteredData = mockDataSource.searchComponents(params)
        
        val totalElements = filteredData.size
        val totalPages = (totalElements + params.size - 1) / params.size
        
        val start = params.page * params.size
        val end = (start + params.size).coerceAtMost(totalElements)
        
        val content = if (start < totalElements) {
            filteredData.subList(start, end).map { mapper.mapToDomain(it) }
        } else {
            emptyList()
        }

        PageResult(
            content = content,
            page = params.page,
            size = params.size,
            totalElements = totalElements,
            totalPages = totalPages
        )
    }
}
