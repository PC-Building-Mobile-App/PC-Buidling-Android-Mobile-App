package com.iti.domain.components.usecase

import com.iti.domain.components.model.Component
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRandomComponentsUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(category: String="", limit: Int = 10): Flow<List<Component>> {
        return repository.getRandomComponents(category, limit)
    }
}