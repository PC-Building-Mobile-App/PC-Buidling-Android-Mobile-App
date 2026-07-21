package com.iti.presentation.parts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.iti.presentation.R
import com.iti.presentation.core.pccomponents.ProductCard
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.core.pccomponents.ProductCardSkeleton

@Composable
fun ProductGrid(
    products: LazyPagingItems<ComponentUiModel>,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoadingInitial = products.loadState.refresh is LoadState.Loading
    val isErrorInitial = products.loadState.refresh is LoadState.Error
    val isEmpty = products.itemCount == 0 && products.loadState.append.endOfPaginationReached

    if (isLoadingInitial) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(6) {
                ProductCardSkeleton()
            }
        }
    } else if (isErrorInitial) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Something went wrong. Please try again.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    } else if (isEmpty) {
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
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                count = products.itemCount,
                key = products.itemKey { it.id }
            ) { index ->
                products[index]?.let { component ->
                    ProductCard(
                        component = component,
                        onClick = { onProductClick(component.id.toString()) }
                    )
                }
            }

            if (products.loadState.append is LoadState.Loading) {
                items(2) {
                    ProductCardSkeleton()
                }
            }
        }
    }
}
