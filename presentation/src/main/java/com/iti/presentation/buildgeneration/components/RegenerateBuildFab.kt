package com.iti.presentation.buildgeneration.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.presentation.R
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.ElectricBlue
import com.iti.presentation.ui.theme.IconOnGradient
import com.iti.presentation.ui.theme.RoyalPurple

@Composable
fun RegenerateBuildFab(
    isRegenerating: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "regenerateFabRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotation",
    )

    val gradientBrush = Brush.linearGradient(
        colors = listOf(ElectricBlue, RoyalPurple),
        start = Offset.Zero,
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    val shape = RoundedCornerShape(50)

    Row(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = ElectricBlue.copy(alpha = 0.45f),
                spotColor = RoyalPurple.copy(alpha = 0.35f),
            )
            .clip(shape)
            .background(brush = gradientBrush)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !isRegenerating,
                onClick = onClick,
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(IconOnGradient.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
                tint = IconOnGradient,
                modifier = Modifier
                    .size(18.dp)
                    .then(if (isRegenerating) Modifier.rotate(rotation) else Modifier),
            )
        }
        Text(
            text = stringResource(R.string.regenerate_build_button),
            fontWeight = FontWeight.SemiBold,
            color = IconOnGradient,
            modifier = Modifier.padding(start = 10.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun RegenerateBuildFabPreview() {
    AppTheme {
        RegenerateBuildFab(isRegenerating = false, onClick = {})
    }
}

@Preview(name = "Regenerating", showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun RegenerateBuildFabRegeneratingPreview() {
    AppTheme {
        RegenerateBuildFab(isRegenerating = true, onClick = {})
    }
}
