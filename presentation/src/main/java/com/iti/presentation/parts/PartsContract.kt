package com.iti.presentation.parts

import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.core.componentcategories.model.ComponentCategoryUiModel
import com.iti.presentation.core.components.model.ComponentUiModel
import com.iti.presentation.core.UiText

interface PartsContract {

    sealed interface Event {
        data class UpdateQuery(val query: String) : Event
        data class SelectCategory(val category: ComponentCategoryType?) : Event
        data object Refresh : Event
        data object ToggleFilterSheet : Event
        data class UpdateAdvancedFilters(
            val minPrice: Double?,
            val maxPrice: Double?,
            val inStockOnly: Boolean
        ) : Event
        data object ResetFilters : Event
        data class ProductClicked(val productId: String) : Event
    }

    data class State(
        val query: String = "",
        val categories: List<ComponentCategoryUiModel> = emptyList(),
        val selectedCategory: ComponentCategoryType? = null,
        val products: List<ComponentUiModel> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: UiText? = null,
        val minPrice: Double? = null,
        val maxPrice: Double? = null,
        val inStockOnly: Boolean = false,
        val isFilterSheetOpen: Boolean = false
    )

    sealed interface Effect {
        data class NavigateToDetail(val productId: String) : Effect
    }
}
