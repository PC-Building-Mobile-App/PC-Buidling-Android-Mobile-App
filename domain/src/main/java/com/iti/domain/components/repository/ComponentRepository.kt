package com.iti.domain.components.repository
import com.iti.domain.components.model.Component
import kotlinx.coroutines.flow.Flow

interface ComponentRepository {
    fun getComponents(): Flow<List<Component>>
    fun getComponentById(id: Long): Flow<Component?>
}