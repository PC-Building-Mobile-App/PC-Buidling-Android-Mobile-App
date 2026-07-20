package com.iti.presentation.parts.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.presentation.parts.PartsContract
import com.iti.presentation.parts.PartsContract.Event
import com.iti.presentation.parts.components.AdvancedSearchSheet
import com.iti.presentation.parts.components.CategoryChipsRow
import com.iti.presentation.parts.components.ProductGrid
import com.iti.presentation.shared.SearchBarField
import com.iti.presentation.parts.viewmodel.PartsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PartsScreen(
    viewModel: PartsViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is PartsContract.Effect.NavigateToDetail -> onNavigateToDetail(effect.productId)
            }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 16.dp)
            ) {
                SearchBarField(
                    query = state.query,
                    onQueryChange = { viewModel.onEvent(Event.UpdateQuery(it)) },
                    onFilterClick = { viewModel.onEvent(Event.ToggleFilterSheet) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                CategoryChipsRow(
                    categories = state.categories,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { viewModel.onEvent(Event.SelectCategory(it)) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        ProductGrid(
            products = state.products,
            isLoading = state.isLoading,
            onProductClick = { viewModel.onEvent(Event.ProductClicked(it)) },
            modifier = Modifier.padding(padding)
        )

        if (state.isFilterSheetOpen) {
            AdvancedSearchSheet(
                initialMinPrice = state.minPrice,
                initialMaxPrice = state.maxPrice,
                initialInStockOnly = state.inStockOnly,
                onApply = { min, max, stock ->
                    viewModel.onEvent(Event.UpdateAdvancedFilters(min, max, stock))
                },
                onReset = { viewModel.onEvent(Event.ResetFilters) },
                onDismiss = { viewModel.onEvent(Event.ToggleFilterSheet) }
            )
        }
    }
}
