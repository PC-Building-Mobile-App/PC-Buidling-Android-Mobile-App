package com.iti.presentation.parts.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.presentation.R
import com.iti.presentation.core.pccomponents.ProductCard
import com.iti.presentation.core.pccomponents.model.ComponentUiModel

@Composable
fun ProductGrid(
    products: List<ComponentUiModel>,
    isInitialLoading: Boolean,
    isPagingLoading: Boolean,
    onLoadMore: () -> Unit,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyGridState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            
            !isPagingLoading && totalItemsCount > 0 && lastVisibleItemIndex >= (totalItemsCount - 4)
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    if (isInitialLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (products.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.no_products_found),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = listState,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products, key = { it.id }) { component ->
                ProductCard(
                    component = component,
                    onClick = { onProductClick(component.id.toString()) }
                )
            }
            
            if (isPagingLoading) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
