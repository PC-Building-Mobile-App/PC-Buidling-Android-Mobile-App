package com.iti.presentation.categorybuilds.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import com.iti.presentation.R
import com.iti.presentation.categorybuilds.model.BuildUiModel
import com.iti.presentation.ui.theme.DeepBlack
import com.iti.presentation.ui.theme.PrimaryGradient

@Composable
fun CompareSelectionBar(
    selectedBuilds: List<BuildUiModel>,
    isVisible: Boolean,
    onRemove: (BuildUiModel) -> Unit,
    onCompare: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = 8.dp,
            shadowElevation = 24.dp
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.selected_builds),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = onCancel) {
                        Text(
                            text = stringResource(R.string.exit_compare),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(selectedBuilds, key = { it.id }) { build ->
                        SelectedBuildChip(
                            name = build.name,
                            onRemove = { onRemove(build) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCompare,
                    enabled = selectedBuilds.size >= 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(54.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    val alpha = if (selectedBuilds.size >= 2) 1f else 0.5f
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(alpha = alpha)
                            .background(brush = PrimaryGradient, shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (selectedBuilds.size < 2) {
                                stringResource(R.string.select_more_to_compare, 2 - selectedBuilds.size)
                            } else {
                                stringResource(R.string.compare_n, selectedBuilds.size)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedBuilds.size >= 2) Color.White else Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedBuildChip(
    name: String,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 120.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier
                    .size(16.dp)
                    .clickable(onClick = onRemove),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
