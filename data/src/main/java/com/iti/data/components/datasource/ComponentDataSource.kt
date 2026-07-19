package com.iti.data.components.datasource

import com.iti.data.components.model.ComponentDataModel
import com.iti.domain.components.model.SearchParams
import kotlinx.coroutines.flow.Flow

interface ComponentDataSource {
    fun getComponents(): Flow<List<ComponentDataModel>>
    fun getComponentById(id: Long): Flow<ComponentDataModel?>
    suspend fun searchComponents(params: SearchParams): List<ComponentDataModel>
}
