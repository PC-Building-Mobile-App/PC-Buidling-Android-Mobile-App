package com.iti.presentation.parts.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.R
import com.iti.presentation.core.componentcategories.model.ComponentCategoryUiModel

@Composable
fun CategoryChipsRow(
    categories: List<ComponentCategoryUiModel>,
    selectedCategory: ComponentCategoryType?,
    onCategorySelected: (ComponentCategoryType?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text(stringResource(R.string.filter_all)) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    selectedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        items(categories) { item ->
            val categoryType = try { ComponentCategoryType.valueOf(item.id) } catch (_: Exception) { null }
            if (categoryType != null && categoryType != ComponentCategoryType.PSU) {
                val isSelected = categoryType == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(categoryType) },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = MaterialTheme.colorScheme.outline,
                        selectedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}
