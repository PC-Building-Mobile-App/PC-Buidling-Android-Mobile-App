package com.iti.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Custom bottom navigation bar that matches the Figma design.
 *
 * Layout:
 * - Semi-transparent dark bar (76 dp) pinned to the bottom
 * - 4 standard nav items: Home, Parts, My PCs, Profile
 * - 1 floating gradient FAB in the center for AI (extends above the bar)
 * - Active-state indicator dot beneath the selected item
 * - iOS-style home indicator pill at the very bottom
 *
 * All colours come from [MaterialTheme.colorScheme] — nothing is hard-coded.
 *
 * @param currentRoute  Currently selected [TopLevelRoute].
 * @param onItemClick   Callback when any tab (including AI) is tapped.
 */
@Composable
fun BottomNavBar(
    currentRoute: TopLevelRoute,
    onItemClick: (TopLevelRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    // ── Outer container: bar height + FAB overhang ──────────────────────
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(104.dp), // 76 dp bar + 28 dp FAB extension above
        contentAlignment = Alignment.BottomCenter,
    ) {

        // ── Bar background ─────────────────────────────────────────────
        val outlineColor = colorScheme.outline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .background(colorScheme.surfaceContainer.copy(alpha = 0.95f))
                .drawBehind {
                    // Subtle top border matching Figma (0.8 px)
                    drawLine(
                        color = outlineColor,
                        start = Offset.Zero,
                        end = Offset(size.width, 0f),
                        strokeWidth = 0.8.dp.toPx(),
                    )
                },
        )

        // ── Nav items row ──────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TopLevelRoute.entries.forEach { tab ->
                if (tab == TopLevelRoute.AI) {
                    // Reserve space for the center FAB
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    NavItem(
                        tab = tab,
                        isSelected = currentRoute == tab,
                        onClick = { onItemClick(tab) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        // ── Center AI FAB ──────────────────────────────────────────────
        AiFab(
            isSelected = currentRoute == TopLevelRoute.AI,
            onClick = { onItemClick(TopLevelRoute.AI) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 0.dp), // sits at the very top of the 104 dp box
        )

        // ── Bottom home-indicator pill ─────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .width(112.dp)
                .height(4.dp)
                .background(
                    color = colorScheme.onSurface.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50),
                ),
        )
    }
}

// ────────────────────────────────────────────────────────────────────────────
// Private composables
// ────────────────────────────────────────────────────────────────────────────

/**
 * A single bottom-nav item (icon + label + optional active dot).
 */
@Composable
private fun NavItem(
    tab: TopLevelRoute,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    val tint by animateColorAsState(
        targetValue = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant,
        label = "navItemTint",
    )

    val dotHeight by animateDpAsState(
        targetValue = if (isSelected) 4.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "dotHeight",
    )

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // remove ripple for a cleaner look
                onClick = onClick,
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Icon
        Icon(
            painter = painterResource(
                id = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
            ),
            contentDescription = tab.label,
            tint = tint,
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Label
        Text(
            text = tab.label,
            color = tint,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Active indicator dot
        Box(
            modifier = Modifier
                .size(width = 4.dp, height = dotHeight)
                .background(color = colorScheme.primary, shape = CircleShape),
        )
    }
}

/**
 * The center floating AI button with a gradient background and glow.
 *
 * Gradient: [MaterialTheme.colorScheme.primary] → [MaterialTheme.colorScheme.secondary] at 135°.
 * Glow: colored shadow using the same primary/secondary palette.
 */
@Composable
private fun AiFab(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    val gradientBrush = Brush.linearGradient(
        colors = listOf(colorScheme.primary, colorScheme.secondary),
        start = Offset.Zero,
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    Box(
        modifier = modifier.size(56.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Glow layer — radial gradient that fades to transparent (works on all APIs)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colorScheme.primary.copy(alpha = 0.45f),
                                colorScheme.secondary.copy(alpha = 0.15f),
                                colorScheme.background.copy(alpha = 0f),
                            ),
                            center = center,
                            radius = size.minDimension * 0.85f,
                        ),
                    )
                },
        )

        // Button body
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = colorScheme.primary.copy(alpha = 0.6f),
                    spotColor = colorScheme.secondary.copy(alpha = 0.3f),
                )
                .clip(CircleShape)
                .background(brush = gradientBrush)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = TopLevelRoute.AI.selectedIcon),
                contentDescription = TopLevelRoute.AI.label,
                tint = colorScheme.onPrimary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
