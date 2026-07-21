package com.iti.domain.components.usecase

import androidx.paging.PagingData
import com.iti.domain.components.model.Component
import com.iti.domain.components.model.SearchParams
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchComponentsUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(params: SearchParams): Flow<PagingData<Component>> {
        return repository.searchComponents(params)
    }
}
