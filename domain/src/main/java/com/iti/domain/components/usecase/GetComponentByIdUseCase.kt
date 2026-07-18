package com.iti.domain.components.usecase

import com.iti.domain.components.model.Component
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetComponentByIdUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(id: Long): Flow<Component?> {
        return repository.getComponentById(id)
    }
}