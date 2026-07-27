package com.iti.presentation.categorybuilds.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.R
import com.iti.presentation.categorybuilds.model.BuildUiModel
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.mypcs.model.gradient

private const val MAX_VISIBLE_SPECS = 4

@Composable
fun BuildCard(
    build: BuildUiModel,
    categoryType: BuildCategoryType,
    isSelectionMode: Boolean,
    onEditClick: () -> Unit,
    onShareClick: () -> Unit,
    onExportClick: () -> Unit,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = MaterialTheme.shapes.medium

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline), shape)
            .clickable {
                if (isSelectionMode) onSelect()
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(categoryType.gradient),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
            ) {
                Text(
                    text = build.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = build.priceFormatted,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            BuildStatsRow(
                isCompatible = build.compatible,
                issuesCount = build.issues.size,
                partsCount = build.specs.size,
            )

            Spacer(modifier = Modifier.height(10.dp))

            SpecChips(specs = build.specs)

            Spacer(modifier = Modifier.height(12.dp))

            BuildActionsRow(
                onEditClick = onEditClick,
                onShareClick = onShareClick,
                onExportClick = onExportClick,
            )
        }
    }
}

@Composable
private fun BuildStatsRow(
    isCompatible: Boolean,
    issuesCount: Int,
    partsCount: Int,
    modifier: Modifier = Modifier,
) {
    val statusText = if (isCompatible) {
        stringResource(R.string.status_compatible)
    } else {
        stringResource(R.string.status_incompatible)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BuildStatChip(
            value = statusText,
            label = stringResource(R.string.label_status),
            modifier = Modifier.weight(1f),
        )
        BuildStatChip(
            value = "$issuesCount",
            label = stringResource(R.string.label_issues),
            modifier = Modifier.weight(1f),
        )
        BuildStatChip(
            value = "$partsCount",
            label = stringResource(R.string.label_parts),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun BuildStatChip(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SpecChips(
    specs: List<ComponentUiModel>,
    modifier: Modifier = Modifier,
) {
    if (specs.isEmpty()) return

    var isExpanded by remember { mutableStateOf(false) }
    val visibleSpecs = if (isExpanded) specs else specs.take(MAX_VISIBLE_SPECS)
    val hiddenCount = specs.size - visibleSpecs.size

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        visibleSpecs.forEach { spec ->
            SpecChip(displayName = spec.displayName())
        }

        when {
            hiddenCount > 0 -> MoreSpecsChip(
                count = hiddenCount,
                onClick = { isExpanded = true },
            )
            isExpanded && specs.size > MAX_VISIBLE_SPECS -> ShowLessChip(
                onClick = { isExpanded = false },
            )
        }
    }
}

@Composable
private fun SpecChip(displayName: String) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .widthIn(max = 140.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun MoreSpecsChip(count: Int, onClick: () -> Unit) {
    val label = pluralStringResource(
        R.plurals.specs_more_count,
        count,
        count
    )
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}

@Composable
private fun ShowLessChip(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.show_less),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}

private fun ComponentUiModel.displayName(): String {
    return productName.takeIf { it.isNotBlank() }
        ?: subtitle.takeIf { it.isNotBlank() }
        ?: matchedGlobalName?.takeIf { it.isNotBlank() }
        ?: category.name.lowercase().replaceFirstChar { it.uppercase() }
}

@Composable
private fun BuildActionsRow(
    onEditClick: () -> Unit,
    onShareClick: () -> Unit,
    onExportClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BuildActionChip(
            icon = Icons.Filled.Edit,
            label = stringResource(R.string.edit),
            modifier = Modifier.weight(1f),
            onClick = onEditClick,
        )
        BuildActionChip(
            icon = Icons.Filled.Share,
            label = stringResource(R.string.share),
            modifier = Modifier.weight(1f),
            onClick = onShareClick,
        )
        BuildActionChip(
            icon = Icons.Filled.Download,
            label = stringResource(R.string.export),
            modifier = Modifier.weight(1f),
            onClick = onExportClick,
        )
    }
}

@Composable
private fun BuildActionChip(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val shape = MaterialTheme.shapes.medium

    Row(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline), shape)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.height(16.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}