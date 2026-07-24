package com.iti.presentation.buildgeneration.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.R
import com.iti.presentation.buildgeneration.model.labelRes
import com.iti.presentation.categorybuilds.model.AlternativeOptionUiModel
import com.iti.presentation.core.UiText
import com.iti.presentation.core.componentcategories.mapper.toIconResource
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.SuccessGreen
import com.iti.presentation.ui.theme.WarningOrange

@Composable
fun ComponentSlotCard(
    category: ComponentCategoryType,
    component: ComponentUiModel?,
    warningMessage: UiText?,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
    alternatives: List<AlternativeOptionUiModel> = emptyList(),
) {
    val context = LocalContext.current
    val shape = MaterialTheme.shapes.large
    val hasWarning = warningMessage != null
    val hasComponent = component != null

    val borderColor = when {
        hasWarning -> WarningOrange.copy(alpha = 0.55f)
        hasComponent -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(BorderStroke(1.dp, borderColor), shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ComponentThumbnail(
                category = category,
                imageUrl = component?.imageUrl,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp),
            ) {
                Text(
                    text = stringResource(category.labelRes),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                if (component != null) {
                    Text(
                        text = component.productName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                    StockBadge(
                        inStock = component.isInStock,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                } else {
                    Text(
                        text = stringResource(R.string.slot_empty_action),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            if (component != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.price_format, component.formattedPrice),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(top = 4.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.remove_component),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }

        if (warningMessage != null) {
            ComponentWarningBanner(
                message = warningMessage.asString(context),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .padding(bottom = if (alternatives.isEmpty()) 12.dp else 4.dp),
            )
        }

        if (alternatives.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Recommended Alternatives:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                alternatives.forEach { alternative ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = alternative.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = alternative.priceFormatted,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComponentThumbnail(
    category: ComponentCategoryType,
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center,
    ) {
        Log.d("TAG", "ComponentThumbnail: $imageUrl")
        if (imageUrl.isNullOrBlank()) {
            CategoryFallbackIcon(category = category)
        } else {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryFallbackIcon(category = category)
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryFallbackIcon(category = category)
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun CategoryFallbackIcon(category: ComponentCategoryType) {
    Icon(
        painter = painterResource(category.toIconResource()),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(26.dp),
    )
}

@Composable
private fun StockBadge(
    inStock: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = if (inStock) SuccessGreen else MaterialTheme.colorScheme.error
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color),
        )
        Text(
            text = if (inStock) stringResource(R.string.picker_in_stock) else stringResource(R.string.picker_out_of_stock),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(start = 6.dp),
        )
    }
}

@Composable
private fun ComponentWarningBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(WarningOrange.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            tint = WarningOrange,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 1.dp),
        )
        Text(
            text = message,
            style = MaterialTheme.typography.labelSmall,
            color = WarningOrange,
        )
    }
}

@Preview(name = "Empty", showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun ComponentSlotCardEmptyPreview() {
    AppTheme {
        ComponentSlotCard(
            category = ComponentCategoryType.GPU,
            component = null,
            warningMessage = null,
            onClick = {},
            onRemoveClick = {},
        )
    }
}

@Preview(name = "Filled - In stock", showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun ComponentSlotCardFilledPreview() {
    AppTheme {
        ComponentSlotCard(
            category = ComponentCategoryType.GPU,
            component = ComponentUiModel(
                id = 5L,
                subtitle = "GearHub · GPU",
                productName = "NVIDIA GeForce RTX 4070 Super",
                vendorName = "GearHub",
                formattedPrice = "32,000 EGP",
                imageUrl = "",
                tags = listOf("Top Pick", "In Stock"),
                isInStock = true,
                category = ComponentCategoryType.GPU,
            ),
            warningMessage = null,
            onClick = {},
            onRemoveClick = {},
        )
    }
}

@Preview(name = "Filled - Out of stock", showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun ComponentSlotCardOutOfStockPreview() {
    AppTheme {
        ComponentSlotCard(
            category = ComponentCategoryType.CPU,
            component = ComponentUiModel(
                id = 7L,
                subtitle = "GearHub · CPU",
                productName = "AMD Ryzen 7 7800X3D",
                vendorName = "GearHub",
                formattedPrice = "14,500 EGP",
                imageUrl = "",
                tags = listOf("Out of Stock"),
                isInStock = false,
                category = ComponentCategoryType.CPU,
            ),
            warningMessage = null,
            onClick = {},
            onRemoveClick = {},
        )
    }
}

@Preview(name = "Filled - With warning", showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun ComponentSlotCardWarningPreview() {
    AppTheme {
        ComponentSlotCard(
            category = ComponentCategoryType.MEMORY,
            component = ComponentUiModel(
                id = 12L,
                subtitle = "GearHub · RAM",
                productName = "Corsair Vengeance 32GB DDR5 6000MHz",
                vendorName = "GearHub",
                formattedPrice = "5,200 EGP",
                imageUrl = "",
                tags = listOf("In Stock"),
                isInStock = true,
                category = ComponentCategoryType.MEMORY,
            ),
            warningMessage = UiText.DynamicString("Runs below the motherboard's rated memory speed."),
            onClick = {},
            onRemoveClick = {},
        )
    }
}

@Preview(name = "Filled - Warning + out of stock", showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun ComponentSlotCardWarningOutOfStockPreview() {
    AppTheme {
        ComponentSlotCard(
            category = ComponentCategoryType.PSU,
            component = ComponentUiModel(
                id = 19L,
                subtitle = "GearHub · PSU",
                productName = "Corsair RM750x 750W 80+ Gold",
                vendorName = "GearHub",
                formattedPrice = "4,300 EGP",
                imageUrl = "",
                tags = listOf("Out of Stock"),
                isInStock = false,
                category = ComponentCategoryType.PSU,
            ),
            warningMessage = UiText.DynamicString("Wattage is close to your build's estimated draw."),
            onClick = {},
            onRemoveClick = {},
        )
    }
}