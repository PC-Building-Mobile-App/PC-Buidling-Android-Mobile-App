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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.R
import com.iti.presentation.categorybuilds.model.BuildSpecUiModel
import com.iti.presentation.categorybuilds.model.BuildUiModel
import com.iti.presentation.mypcs.model.accentColor
import com.iti.presentation.ui.theme.AppTheme

@Composable
fun BuildCard(
    build: BuildUiModel,
    categoryType: BuildCategoryType,
    onEditClick: () -> Unit,
    onShareClick: () -> Unit,
    onExportClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = MaterialTheme.shapes.medium
    val accent = categoryType.accentColor

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline), shape),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
        ) {
            AsyncImage(
                model = build.imageUrl,
                contentDescription = build.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0f),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                            ),
                        ),
                    ),
            )
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
                    color = accent,
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatChip(
                    value = stringResource(R.string.build_score_format, build.performanceScore),
                    label = stringResource(R.string.performance),
                    modifier = Modifier.weight(1f),
                )
                StatChip(
                    value = stringResource(R.string.build_fps_format, build.avgFps),
                    label = stringResource(R.string.avg_fps),
                    modifier = Modifier.weight(1f),
                )
                StatChip(
                    value = stringResource(
                        R.string.build_percent_format,
                        build.compatibilityPercent
                    ),
                    label = stringResource(R.string.compat),
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SpecChips(specs = build.specs)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                ActionChip(
                    icon = Icons.Filled.Edit,
                    label = stringResource(R.string.edit),
                    modifier = Modifier.weight(1f),
                    onClick = onEditClick,
                )
                ActionChip(
                    icon = Icons.Filled.Share,
                    label = stringResource(R.string.share),
                    modifier = Modifier.weight(1f),
                    onClick = onShareClick,
                )
                ActionChip(
                    icon = Icons.Filled.Download,
                    label = stringResource(R.string.export),
                    modifier = Modifier.weight(1f),
                    onClick = onExportClick,
                )
            }
        }
    }
}

@Composable
private fun StatChip(
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
        Text(text = value, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimary)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SpecChips(specs: List<BuildSpecUiModel>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        specs.forEach { spec ->
            Text(
                text = spec.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun ActionChip(
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

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun BuildCardPreview() {
    AppTheme {
        BuildCard(
            build = BuildUiModel(
                id = "gaming_1",
                name = "Ultimate 4K Gaming Rig",
                priceFormatted = "89,500 EGP",
                imageUrl = "",
                performanceScore = 99,
                avgFps = 165,
                compatibilityPercent = 100,
                specs = listOf(
                    BuildSpecUiModel("CPU", "AMD Ryzen 9 7950X", null),
                    BuildSpecUiModel("GPU", "NVIDIA RTX 4090", null),
                    BuildSpecUiModel("RAM", "64 GB DDR5", null),
                    BuildSpecUiModel("STORAGE", "2 TB NVMe", null),
                ),
            ),
            categoryType = BuildCategoryType.GAMING,
            onEditClick = {},
            onShareClick = {},
            onExportClick = {},
        )
    }
}