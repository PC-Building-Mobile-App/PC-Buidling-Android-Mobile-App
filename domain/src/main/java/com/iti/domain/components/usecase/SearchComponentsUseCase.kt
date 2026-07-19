package com.iti.domain.components.usecase

import com.iti.domain.components.model.Component
import com.iti.domain.components.model.SearchParams
import com.iti.domain.components.repository.ComponentRepository
import javax.inject.Inject

class SearchComponentsUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    suspend operator fun invoke(params: SearchParams): Result<List<Component>> {
        return repository.searchComponents(params)
    }
}
