package com.iti.data.components.datasource

import com.iti.data.components.model.ComponentDataModel
import kotlinx.coroutines.flow.Flow

interface ComponentDataSource {
    fun getComponents(): Flow<List<ComponentDataModel>>
    fun getComponentById(id: Long): Flow<ComponentDataModel?>
}