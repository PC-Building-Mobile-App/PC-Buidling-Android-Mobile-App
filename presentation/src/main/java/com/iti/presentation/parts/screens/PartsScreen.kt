package com.iti.presentation.parts.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.parts.PartsContract
import com.iti.presentation.parts.PartsContract.Event
import com.iti.presentation.parts.components.AdvancedSearchSheet
import com.iti.presentation.parts.components.AiOverviewCard
import com.iti.presentation.parts.components.CategoryChipsRow
import com.iti.presentation.parts.components.ProductGrid
import com.iti.presentation.parts.viewmodel.PartsViewModel
import com.iti.presentation.shared.SearchBarField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PartsScreen(
    initialQuery: String? = null,
    initialCategoryId: String? = null,
    viewModel: PartsViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagingItems = state.products.collectAsLazyPagingItems()
    val categoryListState = rememberLazyListState()

    LaunchedEffect(initialQuery, initialCategoryId) {
        if (initialQuery != null) {
            viewModel.onEvent(Event.UpdateQuery(initialQuery))
        }
        if (initialCategoryId != null) {
            val category = runCatching {
                ComponentCategoryType.valueOf(initialCategoryId.uppercase())
            }.getOrNull()
            viewModel.onEvent(Event.SelectCategory(category))
        }
    }

    LaunchedEffect(state.selectedCategory, state.categories) {
        val selectedCategory = state.selectedCategory
        if (selectedCategory != null && state.categories.isNotEmpty()) {
            val index = state.categories.indexOfFirst { it.id == selectedCategory.name }
            if (index != -1) {
                categoryListState.animateScrollToItem(index + 1)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is PartsContract.Effect.NavigateToDetail -> onNavigateToDetail(effect.productId)
            }
        }
    }

    Scaffold(
        topBar = {
            SearchBarField(
                query = state.query,
                onQueryChange = { viewModel.onEvent(Event.UpdateQuery(it)) },
                onFilterClick = { viewModel.onEvent(Event.ToggleFilterSheet) },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        ProductGrid(
            products = pagingItems,
            onProductClick = { viewModel.onEvent(Event.ProductClicked(it)) },
            modifier = Modifier.padding(padding),
            headerContent = {
                Column {
                    if (state.isAiVisible) {
                        AiOverviewCard(
                            overview = state.aiOverview,
                            isLoading = state.isAiLoading,
                            isExpanded = state.isAiExpanded,
                            errorMessage = state.aiErrorMessage,
                            onToggleExpand = { viewModel.onEvent(Event.ToggleAiExpanded) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    CategoryChipsRow(
                        categories = state.categories,
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = { viewModel.onEvent(Event.SelectCategory(it)) },
                        lazyListState = categoryListState,
                        contentPadding = PaddingValues(0.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        )

        if (state.isFilterSheetOpen) {
            AdvancedSearchSheet(
                initialMinPrice = state.minPrice,
                initialMaxPrice = state.maxPrice,
                onApply = { min, max ->
                    viewModel.onEvent(Event.UpdateAdvancedFilters(min, max))
                },
                onReset = { viewModel.onEvent(Event.ResetFilters) },
                onDismiss = { viewModel.onEvent(Event.ToggleFilterSheet) }
            )
        }
    }
}
