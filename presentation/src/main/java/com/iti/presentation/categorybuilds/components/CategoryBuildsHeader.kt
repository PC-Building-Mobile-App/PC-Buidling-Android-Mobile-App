package com.iti.presentation.categorybuilds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.R
import com.iti.presentation.mypcs.model.accentColor
import com.iti.presentation.mypcs.model.gradient
import com.iti.presentation.mypcs.model.iconRes
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.DeepBlack
import com.iti.presentation.ui.theme.PrimaryGradient

@Composable
fun CategoryBuildsHeader(
    categoryName: String,
    categoryDescription: String,
    categoryType: BuildCategoryType,
    isSelectionMode: Boolean,
    onBackClick: () -> Unit,
    onCompareToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(DeepBlack),
    ) {
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            categoryType.accentColor.copy(alpha = 0.55f),
                            categoryType.accentColor.copy(alpha = 0.16f),
                            DeepBlack,
                        ),
                        center = Offset(x = widthPx * 0.22f, y = heightPx * 0.35f),
                        radius = widthPx * 0.75f,
                    ),
                ),
        )

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val backInteractionSource = remember { MutableInteractionSource() }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(DeepBlack.copy(alpha = 0.45f))
                    .clickable(
                        interactionSource = backInteractionSource,
                        indication = null,
                        role = Role.Button,
                        onClickLabel = stringResource(R.string.back),
                        onClick = onBackClick,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(categoryType.gradient),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = categoryType.iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.category_builds_title, categoryName),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    text = categoryDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                )
            }

            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .then(
                        if (isSelectionMode) {
                            Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                        } else {
                            Modifier.background(brush = PrimaryGradient)
                        }
                    )
                    .clickable(onClick = onCompareToggle)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (isSelectionMode) stringResource(R.string.cancel_label) 
                           else stringResource(R.string.compare_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelectionMode) MaterialTheme.colorScheme.onSurface else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun CategoryBuildsHeaderPreview() {
    AppTheme {
        CategoryBuildsHeader(
            categoryName = "Gaming",
            categoryDescription = "High FPS, max settings",
            categoryType = BuildCategoryType.GAMING,
            isSelectionMode = false,
            onBackClick = {},
            onCompareToggle = {},
        )
    }
}
