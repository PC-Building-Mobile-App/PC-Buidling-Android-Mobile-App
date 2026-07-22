package com.iti.data.components.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.iti.data.components.datasource.ComponentDataSource
import com.iti.data.components.datasource.ComponentRemoteDataSource
import com.iti.data.components.mapper.toDomain
import com.iti.data.components.remote.ProductPagingSource
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.domain.components.model.Component
import com.iti.domain.components.model.SearchParams
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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

    override fun searchComponents(params: SearchParams): Flow<PagingData<Component>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductPagingSource(remoteDataSource, params)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain()!! }
        }
    }

    override fun getComponentsByCategory(category: ComponentCategoryType): Flow<List<Component>> {
        return mockDataSource.getComponents().map { dataModels ->
            dataModels
                .filter { it.category.equals(category.name, ignoreCase = true) }
                .mapNotNull { it.toDomain() }
        }
    }
}
