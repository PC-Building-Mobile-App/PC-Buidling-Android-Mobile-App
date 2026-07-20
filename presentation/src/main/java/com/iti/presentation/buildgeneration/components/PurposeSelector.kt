package com.iti.presentation.buildgeneration.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.R
import com.iti.presentation.buildgeneration.model.labelRes
import com.iti.presentation.mypcs.model.accentColor
import com.iti.presentation.mypcs.model.gradient
import com.iti.presentation.mypcs.model.iconRes
import com.iti.presentation.ui.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PurposeSelector(
    selectedCategoryTypes: Set<BuildCategoryType>,
    onCategoryTypeToggled: (BuildCategoryType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.purpose_section_title),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            maxItemsInEachRow = 3
        ) {
            BuildCategoryType.entries.forEach { type ->
                PurposeCard(
                    type = type,
                    selected = type in selectedCategoryTypes,
                    onClick = { onCategoryTypeToggled(type) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PurposeCard(
    type: BuildCategoryType,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.96f,
        label = "card_scale"
    )
    val accentColor = type.accentColor
    val shape = MaterialTheme.shapes.large

    Box(
        modifier = modifier
            .scale(scale)
            .aspectRatio(1.25f)
            .clip(shape)
            .background(
                if (selected) {
                    type.gradient
                } else {
                    Brush.linearGradient(listOf(
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                    ))
                }
            )
            .then(
                if (!selected) Modifier.border(1.dp, accentColor.copy(alpha = 0.25f), shape)
                else Modifier.border(1.5.dp, accentColor.copy(alpha = 0.6f), shape)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 4.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(16.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\u2713",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(type.iconRes),
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.background else accentColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(type.labelRes),
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun PurposeSelectorPreview() {
    AppTheme {
        PurposeSelector(selectedCategoryTypes = setOf(BuildCategoryType.GAMING), onCategoryTypeToggled = {})
    }
}