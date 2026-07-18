package com.iti.domain.components.usecase

import com.iti.domain.components.model.Component
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetComponentsUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(limit: Int? = null): Flow<List<Component>> {
        return repository.getComponents().map { components ->
            if (limit != null) {
                components.take(limit)
            } else {
                components
            }
        }
    }
}