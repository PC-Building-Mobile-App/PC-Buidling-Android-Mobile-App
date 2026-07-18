package com.iti.domain.componentcategories.usecase

import com.iti.domain.componentcategories.model.ComponentCategory
import com.iti.domain.componentcategories.repository.ComponentCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetComponentCategoriesUseCase @Inject constructor(
    private val repository: ComponentCategoryRepository
) {
    operator fun invoke(limit: Int? = null): Flow<List<ComponentCategory>> {
        return repository.getComponentCategories().map { categories ->
            if (limit != null) {
                categories.take(limit)
            } else {
                categories
            }
        }
    }
}