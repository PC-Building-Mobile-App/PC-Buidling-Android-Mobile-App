package com.iti.presentation.mypcs.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.R
import com.iti.presentation.mypcs.model.BuildCategoryUiModel
import com.iti.presentation.mypcs.model.accentColor
import com.iti.presentation.mypcs.model.gradient
import com.iti.presentation.mypcs.model.iconRes
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.BorderSubtle
import com.iti.presentation.ui.theme.CardSurface
import com.iti.presentation.ui.theme.IconOnGradient
import com.iti.presentation.ui.theme.TextMuted
import com.iti.presentation.ui.theme.TextPrimary

@Composable
fun BuildCategoryCard(
    category: BuildCategoryUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = MaterialTheme.shapes.small
    val accent = category.type.accentColor

    Box(
        modifier = modifier
            .clip(shape)
            .background(CardSurface)
            .border(BorderStroke(1.dp, BorderSubtle), shape)
            .clickable(onClick = onClick)
            .height(168.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(130.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(accent.copy(alpha = 0.20f), accent.copy(alpha = 0f)),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(category.type.gradient),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = category.type.iconRes),
                    contentDescription = null,
                    tint = IconOnGradient,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = category.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 14.sp
                ),
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = category.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 11.sp
                ),
                color = TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.builds, category.buildsCount),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = accent,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun BuildCategoryCardPreview() {
    AppTheme {
        BuildCategoryCard(
            category = BuildCategoryUiModel(
                id = "gaming",
                name = "Gaming",
                description = "High FPS, max settings",
                buildsCount = 3,
                type = BuildCategoryType.GAMING,
            ),
            onClick = {},
            modifier = Modifier.width(171.dp),
        )
    }
}
