package com.iti.presentation.buildgeneration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.iti.domain.componentcategories.model.ComponentCategoryType
import com.iti.presentation.R
import com.iti.presentation.buildgeneration.model.PickerComponentUiModel
import com.iti.presentation.buildgeneration.model.labelRes
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.core.uicomponents.EmptyScreen
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentPickerBottomSheet(
    category: ComponentCategoryType,
    components: List<ComponentUiModel>,
    isLoading: Boolean,
    onComponentSelected: (ComponentUiModel) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp)) {
            Text(
                text = stringResource(R.string.picker_title_format, stringResource(category.labelRes)),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(240.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                components.isEmpty() -> {
                    EmptyScreen(
                        title = stringResource(R.string.picker_empty_title),
                        message = stringResource(R.string.picker_empty_message),
                    )
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.height(420.dp)) {
                        items(components, key = { it.id }) { component ->
                            PickerComponentRow(component = component, onClick = { onComponentSelected(component) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PickerComponentRow(
    component: ComponentUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable(enabled = component.isInStock, onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = component.imageUrl,
            contentDescription = component.productName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp).clip(MaterialTheme.shapes.extraSmall).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)),
        )

        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(
                text = component.productName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = component.vendorName,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = if (component.isInStock) stringResource(R.string.picker_in_stock) else stringResource(R.string.picker_out_of_stock),
                style = MaterialTheme.typography.labelLarge,
                color = if (component.isInStock) SuccessGreen else MaterialTheme.colorScheme.error,
            )
        }

        Text(
            text = stringResource(R.string.price_format, component.formattedPrice),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

