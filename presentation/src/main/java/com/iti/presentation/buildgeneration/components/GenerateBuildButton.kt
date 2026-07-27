package com.iti.presentation.buildgeneration.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.iti.presentation.R
import com.iti.presentation.core.uicomponents.PrimaryButton
import com.iti.presentation.ui.theme.AppTheme

@Composable
fun GenerateBuildButton(
    isGenerating: Boolean,
    hasSelection: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryButton(
        text = if (hasSelection) {
            stringResource(R.string.generate_remaining_button)
        } else {
            stringResource(R.string.generate_build_button)
        },
        icon = Icons.Default.AutoAwesome,
        isLoading = isGenerating,
        onClick = onClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun GenerateBuildButtonPreview() {
    AppTheme {
        GenerateBuildButton(isGenerating = false, hasSelection = false, onClick = {})
    }
}

@Preview(name = "Has selection", showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun GenerateBuildButtonHasSelectionPreview() {
    AppTheme {
        GenerateBuildButton(isGenerating = false, hasSelection = true, onClick = {})
    }
}

@Preview(name = "Loading", showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun GenerateBuildButtonLoadingPreview() {
    AppTheme {
        GenerateBuildButton(isGenerating = true, hasSelection = false, onClick = {})
    }
}