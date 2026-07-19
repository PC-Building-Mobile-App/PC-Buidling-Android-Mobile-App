package com.iti.presentation.buildgeneration.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.iti.presentation.R
import com.iti.presentation.ui.theme.AppTheme

@Composable
fun SaveBuildDialog(
    buildName: String,
    isSaving: Boolean,
    onNameChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.medium,
        title = {
            Text(
                text = stringResource(R.string.save_build_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            OutlinedTextField(
                value = buildName,
                onValueChange = onNameChanged,
                label = { Text(text = stringResource(R.string.save_build_name_label)) },
                placeholder = { Text(text = stringResource(R.string.save_build_name_placeholder)) },
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = buildName.isNotBlank() && !isSaving) {
                Text(text = stringResource(R.string.action_confirm), color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun SaveBuildDialogPreview() {
    AppTheme {
        SaveBuildDialog(buildName = "My Gaming Rig", isSaving = false, onNameChanged = {}, onConfirm = {}, onDismiss = {})
    }
}