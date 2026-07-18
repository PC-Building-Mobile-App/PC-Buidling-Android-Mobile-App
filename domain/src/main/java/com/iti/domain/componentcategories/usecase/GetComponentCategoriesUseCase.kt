package com.iti.domain.componentcategories.usecase

import com.iti.domain.componentcategories.model.ComponentCategory
import com.iti.domain.componentcategories.repository.ComponentCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetComponentCategoriesUseCase @Inject constructor(
    private val repository: ComponentCategoryRepository
) {
    operator fun invoke(): Flow<List<ComponentCategory>> {
        return repository.getComponentCategories()
    }
}