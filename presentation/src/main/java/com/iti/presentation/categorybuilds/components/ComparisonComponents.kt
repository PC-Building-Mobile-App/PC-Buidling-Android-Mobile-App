package com.iti.presentation.categorybuilds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.domain.builds.model.Build
import com.iti.presentation.R
import com.iti.presentation.ui.theme.PrimaryGradient

@Composable
fun CategoryChipsRow(
    categories: List<String>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(categories) { index, category ->
            val isSelected = index == selectedIndex
            val iconRes = getCategoryIcon(category)
            
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(index) },
                label = {
                    Text(
                        text = mapCategoryToDisplay(category),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                leadingIcon = if (iconRes != null) {
                    {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    selectedLabelColor = MaterialTheme.colorScheme.primary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    selectedBorderColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun ComparisonHeader(
    builds: List<Build>, 
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isLoading) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.medium
                        )
                )
            }
        } else {
            builds.forEachIndexed { index, build ->
                BuildComparisonCard(
                    build = build, 
                    label = stringResource(R.string.build_label_format, if (index == 0) "A" else "B"),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ComparisonBottomBar(
    onChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Button(
            onClick = onChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = PrimaryGradient, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.change_selection_action),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

internal fun getCategoryIcon(category: String): Int? {
    return when (category.uppercase()) {
        "CPU" -> R.drawable.ic_cpu
        "GPU" -> R.drawable.ic_gpu
        "MOTHERBOARD" -> R.drawable.ic_motherboard
        "CASE" -> R.drawable.ic_case
        "PSU" -> R.drawable.ic_psu
        "COOLER" -> R.drawable.ic_cooler
        "MEMORY" -> R.drawable.ic_memory
        "STORAGE" -> R.drawable.ic_storage
        else -> null
    }
}

internal fun mapCategoryToDisplay(category: String): String {
    return when (category.uppercase()) {
        "MEMORY" -> "RAM"
        "COOLER" -> "Cooling"
        "MOTHERBOARD" -> "Motherboard"
        else -> category.replaceFirstChar { it.uppercase() }
    }
}
