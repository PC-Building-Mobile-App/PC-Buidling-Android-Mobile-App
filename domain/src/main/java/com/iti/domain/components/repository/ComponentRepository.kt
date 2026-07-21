package com.iti.domain.components.repository
import androidx.paging.PagingData
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.domain.components.model.Component
import com.iti.domain.components.model.SearchParams
import kotlinx.coroutines.flow.Flow

interface ComponentRepository {
    fun getComponents(): Flow<List<Component>>
    fun getComponentById(id: Long): Flow<Component?>
    fun searchComponents(params: SearchParams): Flow<PagingData<Component>>
    fun getComponentsByCategory(category: ComponentCategoryType): Flow<List<Component>>

    fun getRandomComponents(category: String, limit: Int): Flow<List<Component>>
}
