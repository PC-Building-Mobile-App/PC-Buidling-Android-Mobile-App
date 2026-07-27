package com.iti.presentation.core.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Semantic type driving both color and icon, instead of a raw boolean flag. */
enum class SnackbarType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO,
}

private class AppSnackbarVisuals(
    override val message: String,
    val type: SnackbarType,
) : SnackbarVisuals {
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
    override val duration: SnackbarDuration = SnackbarDuration.Short
}

class SnackbarController(private val snackbarHostState: SnackbarHostState) {
    suspend fun show(message: String, type: SnackbarType = SnackbarType.INFO) {
        snackbarHostState.showSnackbar(AppSnackbarVisuals(message = message, type = type))
    }

    suspend fun showSuccess(message: String) = show(message, SnackbarType.SUCCESS)
    suspend fun showError(message: String) = show(message, SnackbarType.ERROR)
    suspend fun showWarning(message: String) = show(message, SnackbarType.WARNING)
    suspend fun showInfo(message: String) = show(message, SnackbarType.INFO)
}

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    error("No SnackbarController provided")
}

private data class SnackbarStyle(
    val container: Color,
    val content: Color,
    val icon: ImageVector,
)

@Composable
private fun styleFor(type: SnackbarType): SnackbarStyle = when (type) {
    SnackbarType.SUCCESS -> SnackbarStyle(
        container = Color(0xFF16342A),
        content = Color(0xFF6FE3A6),
        icon = Icons.Default.CheckCircle,
    )
    SnackbarType.ERROR -> SnackbarStyle(
        container = MaterialTheme.colorScheme.errorContainer,
        content = MaterialTheme.colorScheme.onErrorContainer,
        icon = Icons.Default.Error,
    )
    SnackbarType.WARNING -> SnackbarStyle(
        container = Color(0xFF3A2F14),
        content = Color(0xFFE8B65C),
        icon = Icons.Default.Warning,
    )
    SnackbarType.INFO -> SnackbarStyle(
        container = MaterialTheme.colorScheme.surfaceVariant,
        content = MaterialTheme.colorScheme.onSurfaceVariant,
        icon = Icons.Default.Info,
    )
}

@Composable
fun AppSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val visuals = snackbarData.visuals as? AppSnackbarVisuals
    val type = visuals?.type ?: SnackbarType.INFO
    val style = styleFor(type)

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(style.container)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = style.icon,
                contentDescription = null,
                tint = style.content,
                modifier = Modifier.size(22.dp),
            )
            Text(
                text = snackbarData.visuals.message,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = style.content,
            )
        }
    }
}