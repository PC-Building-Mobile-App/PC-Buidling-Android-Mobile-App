package com.iti.presentation.parts.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iti.domain.componentcategories.usecase.GetComponentCategoriesUseCase
import com.iti.domain.components.model.SearchParams
import com.iti.domain.components.usecase.SearchComponentsUseCase
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.componentcategories.mapper.ComponentCategoryUiMapper
import com.iti.presentation.core.components.mapper.ComponentUiMapper
import com.iti.presentation.core.toUiText
import com.iti.presentation.parts.PartsContract.Effect
import com.iti.presentation.parts.PartsContract.Event
import com.iti.presentation.parts.PartsContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@HiltViewModel
class PartsViewModel @Inject constructor(
    private val searchComponentsUseCase: SearchComponentsUseCase,
    private val getCategoriesUseCase: GetComponentCategoriesUseCase,
    private val categoryUiMapper: ComponentCategoryUiMapper,
    private val componentUiMapper: ComponentUiMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State()

    private val queryFlow = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        val initialQuery: String? = savedStateHandle["initialQuery"]
        
        loadCategories()

        viewModelScope.launch {
            queryFlow
                .debounce(400.milliseconds)
                .distinctUntilChanged()
                .collectLatest {
                    performSearch(reset = true)
                }
        }

        if (initialQuery != null) {
            updateState { it.copy(query = initialQuery) }
            queryFlow.value = initialQuery
        } else {
            performSearch(reset = true)
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.UpdateQuery -> {
                updateState { it.copy(query = event.query) }
                queryFlow.value = event.query
            }
            is Event.SelectCategory -> {
                updateState { it.copy(selectedCategory = event.category) }
                performSearch(reset = true)
            }
            is Event.LoadNextPage -> {
                if (!state.value.isPagingLoading && !state.value.isLastPage) {
                    performSearch(reset = false)
                }
            }
            is Event.Refresh -> {
                performSearch(reset = true)
            }
            is Event.ToggleFilterSheet -> {
                updateState { it.copy(isFilterSheetOpen = !it.isFilterSheetOpen) }
            }
            is Event.UpdateAdvancedFilters -> {
                updateState { it.copy(
                    minPrice = event.minPrice,
                    maxPrice = event.maxPrice,
                    inStockOnly = event.inStockOnly,
                    isFilterSheetOpen = false
                ) }
                performSearch(reset = true)
            }
            is Event.ResetFilters -> {
                updateState { it.copy(
                    minPrice = null,
                    maxPrice = null,
                    inStockOnly = false
                ) }
                performSearch(reset = true)
            }
            is Event.ProductClicked -> {
                sendEffect(Effect.NavigateToDetail(event.productId))
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .map { domainCategories ->
                    categoryUiMapper.mapToUiModels(domainCategories)
                }
                .collect { uiCategories ->
                    updateState { it.copy(categories = uiCategories) }
                }
        }
    }

    private fun performSearch(reset: Boolean) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val currentState = state.value
            val targetPage = if (reset) 0 else currentState.page + 1
            
            if (reset) {
                updateState { it.copy(isInitialLoading = true, errorMessage = null, products = emptyList()) }
            } else {
                updateState { it.copy(isPagingLoading = true) }
            }

            val params = SearchParams(
                query = currentState.query,
                category = currentState.selectedCategory,
                minPrice = currentState.minPrice,
                maxPrice = currentState.maxPrice,
                inStockOnly = currentState.inStockOnly,
                page = targetPage,
                size = 20
            )

            searchComponentsUseCase(params).onSuccess { pageResult ->
                val newProducts = componentUiMapper.mapToUiModels(pageResult.content)
                updateState {
                    it.copy(
                        products = if (reset) newProducts else it.products + newProducts,
                        page = pageResult.page,
                        isLastPage = pageResult.page >= pageResult.totalPages - 1,
                        isInitialLoading = false,
                        isPagingLoading = false
                    )
                }
            }.onFailure { throwable ->
                updateState { 
                    it.copy(
                        errorMessage = throwable.toUiText(),
                        isInitialLoading = false,
                        isPagingLoading = false
                    )
                }
            }
        }
    }
}
