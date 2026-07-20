package com.iti.data.components.repository

import com.iti.data.components.datasource.ComponentDataSource
import com.iti.data.components.datasource.ComponentRemoteDataSource
import com.iti.data.components.mapper.toDomain
import com.iti.data.util.safeCall
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.domain.components.model.Component
import com.iti.domain.components.model.SearchParams
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.Result
import kotlin.time.Duration.Companion.milliseconds

class ComponentRepositoryImpl @Inject constructor(
    private val mockDataSource: ComponentDataSource,
    private val remoteDataSource: ComponentRemoteDataSource

) : ComponentRepository {

    override fun getRandomComponents(category: String, limit: Int): Flow<List<Component>> = flow {
        val result = remoteDataSource.getRandomComponents(category, limit)
        val dataModels = result.getOrThrow()
        emit(dataModels.toDomain())
    }
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

    override suspend fun searchComponents(params: SearchParams): Result<List<Component>> = safeCall {
        // todo(Switch to real RemoteDataSource once backend is done)
        delay(600.milliseconds)

        val filteredData = mockDataSource.searchComponents(params)

        filteredData.toDomain()

    }

    override fun getComponentsByCategory(category: ComponentCategoryType): Flow<List<Component>> {
        return mockDataSource.getComponents().map { dataModels ->
            dataModels
                .filter { it.category.equals(category.name, ignoreCase = true) }
                .map { it.toDomain() }
        }
    }
}
