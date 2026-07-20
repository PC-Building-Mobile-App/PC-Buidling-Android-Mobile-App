package com.iti.presentation.parts.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.domain.componentcategories.usecase.GetComponentCategoriesUseCase
import com.iti.domain.components.model.SearchParams
import com.iti.domain.components.usecase.SearchComponentsUseCase
import com.iti.presentation.core.BaseViewModel
import com.iti.presentation.core.componentcategories.mapper.toUiModels
import com.iti.presentation.core.components.mapper.toUiModels
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
    savedStateHandle: SavedStateHandle
) : BaseViewModel<Event, State, Effect>() {

    override fun createInitialState(): State = State()

    private val queryFlow = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        val initialQuery: String? = savedStateHandle["initialQuery"]
        val initialCategoryId: String? = savedStateHandle["initialCategoryId"]

        val initialCategory = initialCategoryId?.let { id ->
            runCatching { ComponentCategoryType.valueOf(id.uppercase()) }.getOrNull()
        }
        
        loadCategories()

        viewModelScope.launch {
            queryFlow
                .debounce(400.milliseconds)
                .distinctUntilChanged()
                .collectLatest {
                    performSearch()
                }
        }

        if (initialQuery != null || initialCategory != null) {
            updateState { 
                it.copy(
                    query = initialQuery ?: "",
                    selectedCategory = initialCategory
                ) 
            }
            queryFlow.value = initialQuery ?: ""
            performSearch()
        } else {
            performSearch()
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
                performSearch()
            }
            is Event.Refresh -> {
                performSearch()
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
                performSearch()
            }
            is Event.ResetFilters -> {
                updateState { it.copy(
                    minPrice = null,
                    maxPrice = null,
                    inStockOnly = false
                ) }
                performSearch()
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
                    domainCategories.toUiModels()
                }
                .collect { uiCategories ->
                    updateState { it.copy(categories = uiCategories) }
                }
        }
    }

    private fun performSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val currentState = state.value
            
            updateState { it.copy(isLoading = true, errorMessage = null, products = emptyList()) }

            val params = SearchParams(
                query = currentState.query,
                category = currentState.selectedCategory,
                minPrice = currentState.minPrice,
                maxPrice = currentState.maxPrice,
                inStockOnly = currentState.inStockOnly
            )

            searchComponentsUseCase(params).onSuccess { components ->
                updateState {
                    it.copy(
                        products = components.toUiModels(),
                        isLoading = false
                    )
                }
            }.onFailure { throwable ->
                updateState { 
                    it.copy(
                        errorMessage = throwable.toUiText(),
                        isLoading = false
                    )
                }
            }
        }
    }
}
