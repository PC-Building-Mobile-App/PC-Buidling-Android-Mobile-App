package com.iti.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.presentation.R
import com.iti.presentation.core.pccomponents.ProductCard
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.core.uicomponents.shimmerEffect
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.ElectricBlue
import com.iti.presentation.ui.theme.RoyalPurple
import com.iti.presentation.ui.theme.TextSecondary

@Composable
fun FeaturedComponentsSection(
    components: List<ComponentUiModel>,
    isLoading: Boolean,
    onSeeAll: () -> Unit,
    onComponentClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            SectionHeader(
                title = "",
                modifier = Modifier.padding(horizontal = 0.dp)
            )

            SectionHeader(
                title = stringResource(R.string.featured_components),
                actionText = stringResource(R.string.see_all),
                onActionClick = onSeeAll,
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(4) {
                    FeaturedComponentSkeleton()
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(components, key = { it.id }) { component ->
                    ProductCard(
                        component = component,
                        onClick = { onComponentClick(component.id) },
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedComponentSkeleton() {
    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .shimmerEffect()
        )
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun FeaturedComponentsSectionPreview() {
    AppTheme {
        FeaturedComponentsSection(
            components = listOf(
                ComponentUiModel(1, "AMD · CPU", "Ryzen 9 7950X", "18,500 EGP", "", listOf("Top Pick", "In Stock"), true),
                ComponentUiModel(2, "NVIDIA · GPU", "RTX 4080", "52,000 EGP", "", listOf("In Stock"), true),
            ),
            isLoading = false,
            onSeeAll = {},
            onComponentClick = {}
        )
    }
}
