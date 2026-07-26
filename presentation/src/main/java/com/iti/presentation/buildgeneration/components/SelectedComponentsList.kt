package com.iti.presentation.buildgeneration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.R
import com.iti.presentation.buildgeneration.model.labelRes
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.ui.theme.AppTheme

@Composable
fun SelectedComponentsList(
    components: List<ComponentUiModel>,
    totalPriceFormatted: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(vertical = 8.dp),
    ) {
        components.forEachIndexed { index, component ->
            SelectedComponentRow(
                component = component,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
            if (index != components.lastIndex) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.total_price_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.price_format, totalPriceFormatted),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun SelectedComponentRow(component: ComponentUiModel, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = component.productName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(component.category.labelRes),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = stringResource(R.string.price_format, component.formattedPrice),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun SelectedComponentsListPreview() {
    AppTheme {
        SelectedComponentsList(
            components = listOf(
                ComponentUiModel(
                    id = 1L,
                    subtitle = "TechStore · CPU",
                    productName = "AMD Ryzen 7 7800X3D",
                    vendorName = "TechStore",
                    formattedPrice = "18,500 EGP",
                    imageUrl = "",
                    tags = listOf("Top Pick", "In Stock"),
                    isInStock = true,
                    category = ComponentCategoryType.CPU
                ),
                ComponentUiModel(
                    id = 5L,
                    subtitle = "GearHub · GPU",
                    productName = "NVIDIA RTX 4070 Super",
                    vendorName = "GearHub",
                    formattedPrice = "32,000 EGP",
                    imageUrl = "",
                    tags = listOf("Top Pick", "In Stock"),
                    isInStock = true,
                    category = ComponentCategoryType.GPU
                )
            ),
            totalPriceFormatted = "50,500 EGP",
        )
    }
}