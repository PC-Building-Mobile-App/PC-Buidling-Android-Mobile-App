package com.iti.domain.components.usecase

import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.domain.components.model.Component
import com.iti.domain.components.repository.ComponentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetComponentsByCategoryUseCase @Inject constructor(
    private val repository: ComponentRepository,
) {
    operator fun invoke(category: ComponentCategoryType): Flow<List<Component>> =
        repository.getComponentsByCategory(category)
}