package com.iti.presentation.parts

import androidx.paging.PagingData
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.core.componentcategories.model.ComponentCategoryUiModel
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.core.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface PartsContract {

    sealed interface Event {
        data class UpdateQuery(val query: String) : Event
        data class SelectCategory(val category: ComponentCategoryType?) : Event
        data object Refresh : Event
        data object ToggleFilterSheet : Event
        data object ToggleAiExpanded : Event
        data class UpdateAdvancedFilters(
            val minPrice: Double?,
            val maxPrice: Double?
        ) : Event
        data object ResetFilters : Event
        data class ProductClicked(val component: ComponentUiModel) : Event
    }

    data class State(
        val query: String = "",
        val categories: List<ComponentCategoryUiModel> = emptyList(),
        val selectedCategory: ComponentCategoryType? = null,
        val products: Flow<PagingData<ComponentUiModel>> = flowOf(PagingData.empty()),
        val isLoading: Boolean = false,
        val errorMessage: UiText? = null,
        val minPrice: Double? = null,
        val maxPrice: Double? = null,
        val isFilterSheetOpen: Boolean = false,
        val aiOverview: String? = null,
        val aiErrorMessage: UiText? = null,
        val isAiLoading: Boolean = false,
        val isAiVisible: Boolean = false,
        val isAiExpanded: Boolean = true
    )

    sealed interface Effect {
        data class NavigateToDetail(val componentJson: String) : Effect
    }
}