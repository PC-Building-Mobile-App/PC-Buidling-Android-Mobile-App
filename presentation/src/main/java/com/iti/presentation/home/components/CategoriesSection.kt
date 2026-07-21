package com.iti.presentation.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.presentation.R
import com.iti.presentation.core.componentcategories.model.ComponentCategoryUiModel
import com.iti.presentation.core.uicomponents.shimmerEffect
import com.iti.presentation.ui.theme.AppTheme

@Composable
fun CategoriesSection(
    categories: List<ComponentCategoryUiModel>,
    isExpanded: Boolean,
    isLoading: Boolean,
    onToggleExpanded: () -> Unit,
    onCategoryClick: (ComponentCategoryUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SectionHeader(
            title = stringResource(R.string.categories),
            actionText = if (isExpanded) {
                stringResource(R.string.show_less)
            } else {
                stringResource(R.string.see_all)
            },
            onActionClick = onToggleExpanded
        )

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            CategoriesSkeleton()
        } else {
            val displayedCategories = if (isExpanded) {
                categories.take(7)
            } else {
                categories.take(4)
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                displayedCategories.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { category ->
                            HomeCategoryCard(
                                category = category,
                                onClick = { onCategoryClick(category) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoriesSkeleton() {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun CategoriesSectionPreview() {
    AppTheme {
        CategoriesSection(
            categories = listOf(
                ComponentCategoryUiModel("CPU", "Processors", "472 parts", R.drawable.ic_cpu),
                ComponentCategoryUiModel("GPU", "Graphics", "200 parts", R.drawable.ic_gpu),
                ComponentCategoryUiModel("PSU", "Power", "134 parts", R.drawable.ic_psu),
                ComponentCategoryUiModel("COOLER", "Cooling", "89 parts", R.drawable.ic_cooler),
            ),
            isExpanded = false,
            isLoading = false,
            onToggleExpanded = {},
            onCategoryClick = {}
        )
    }
}
