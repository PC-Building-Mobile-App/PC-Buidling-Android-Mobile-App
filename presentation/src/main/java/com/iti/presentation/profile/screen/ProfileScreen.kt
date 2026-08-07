package com.iti.presentation.profile.screens

import android.app.Activity
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.iti.presentation.R
import com.iti.presentation.profile.ProfileContract
import com.iti.presentation.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigateToSavedBuilds: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ProfileContract.Effect.NavigateToSavedBuilds -> onNavigateToSavedBuilds()
                ProfileContract.Effect.RecreateActivity -> context.findActivity()?.recreate()
            }
        }
    }

    ProfileScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun ProfileScreenContent(
    state: ProfileContract.State,
    onEvent: (ProfileContract.Event) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 32.dp, bottom = 24.dp),
    ) {
        ProfileHeaderCard(
            name = state.name,
            handle = state.handle,
            buildsCount = state.buildsCount,
            avatarPath = state.avatarPath,
            onAvatarPicked = { uri -> onEvent(ProfileContract.Event.AvatarPicked(uri)) },
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(R.string.account),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
            color = colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(colorScheme.surfaceContainer),
        ) {
            AccountRow(
                icon = Icons.Filled.Shield,
                iconTint = colorScheme.primary,
                iconBackground = colorScheme.primary.copy(alpha = 0.18f),
                label = stringResource(R.string.saved_builds),
                onClick = { onEvent(ProfileContract.Event.SavedBuildsClicked) },
            )
            RowDivider()
            AccountRow(
                icon = Icons.Filled.Language,
                iconTint = Color(0xFF34D399),
                iconBackground = Color(0xFF34D399).copy(alpha = 0.18f),
                label = stringResource(R.string.language_region),
                trailingText = state.selectedLanguage.displayName,
                onClick = { onEvent(ProfileContract.Event.LanguageAndRegionClicked) },
            )
            RowDivider()
            AccountRow(
                icon = Icons.Filled.DarkMode,
                iconTint = colorScheme.secondary,
                iconBackground = colorScheme.secondary.copy(alpha = 0.18f),
                label = stringResource(R.string.app_theme),
                trailingText = stringResource(state.selectedTheme.displayNameRes),
                onClick = { onEvent(ProfileContract.Event.ThemeClicked) },
            )

            RowDivider()
            AccountRow(
                icon = Icons.AutoMirrored.Filled.Logout,
                iconTint = colorScheme.error,
                iconBackground = colorScheme.error.copy(alpha = 0.18f),
                label = stringResource(R.string.sign_out),
                labelColor = colorScheme.error,
                showChevron = false,
                onClick = { onEvent(ProfileContract.Event.SignOutClicked) },
            )
        }
    }

    if (state.isLanguageDialogVisible) {
        LanguagePickerDialog(
            selectedLanguage = state.selectedLanguage,
            onLanguageSelected = { onEvent(ProfileContract.Event.LanguageSelected(it)) },
            onDismiss = { onEvent(ProfileContract.Event.DismissLanguageDialog) },
        )
    }

    if (state.isThemeDialogVisible) {
        ThemePickerDialog(
            selectedTheme = state.selectedTheme,
            onThemeSelected = { onEvent(ProfileContract.Event.ThemeSelected(it)) },
            onDismiss = { onEvent(ProfileContract.Event.DismissThemeDialog) },
        )
    }
}

@Composable
private fun ThemePickerDialog(
    selectedTheme: com.iti.domain.settings.model.AppThemePreference,
    onThemeSelected: (com.iti.domain.settings.model.AppThemePreference) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.choose_theme)) },
        text = {
            Column {
                com.iti.domain.settings.model.AppThemePreference.entries.forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = theme == selectedTheme,
                                onClick = { onThemeSelected(theme) },
                            )
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = theme == selectedTheme,
                            onClick = { onThemeSelected(theme) },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(theme.displayNameRes))
                    }

                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        },
    )
}

@Composable
private fun LanguagePickerDialog(
    selectedLanguage: ProfileContract.AppLanguage,
    onLanguageSelected: (ProfileContract.AppLanguage) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.choose_language)) },
        text = {
            Column {
                ProfileContract.AppLanguage.entries.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = language == selectedLanguage,
                                onClick = { onLanguageSelected(language) },
                            )
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = language == selectedLanguage,
                            onClick = { onLanguageSelected(language) },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(language.displayName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        },
    )
}

@Composable
private fun ProfileHeaderCard(
    name: String,
    handle: String,
    buildsCount: Int,
    avatarPath: String?,
    onAvatarPicked: (String) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val gradientBrush = Brush.linearGradient(
        colors = listOf(colorScheme.primary, colorScheme.secondary),
    )

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri != null) onAvatarPicked(uri.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(colorScheme.surfaceContainer)
            .padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (avatarPath == null) gradientBrush else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)))
                    .clickable {
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (avatarPath != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = avatarPath),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.PhotoCamera,
                        contentDescription = stringResource(R.string.edit_profile),
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = handle,
                    fontSize = 14.sp,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        StatChip(
            value = buildsCount,
            label = stringResource(R.string.builds_uppercase),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private val com.iti.domain.settings.model.AppThemePreference.displayNameRes: Int
    get() = when (this) {
        com.iti.domain.settings.model.AppThemePreference.SYSTEM -> R.string.theme_system
        com.iti.domain.settings.model.AppThemePreference.LIGHT -> R.string.theme_light
        com.iti.domain.settings.model.AppThemePreference.DARK -> R.string.theme_dark
    }

@Composable
private fun StatChip(

    value: Int,
    label: String,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AccountRow(
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color,
    label: String,
    onClick: () -> Unit,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    showChevron: Boolean = true,
    trailingText: String? = null,
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp),
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = labelColor,
            modifier = Modifier.weight(1f),
        )

        if (trailingText != null) {
            Text(
                text = trailingText,
                fontSize = 13.sp,
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp),
            )
        }

        if (showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
private fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
    )
}

private fun android.content.Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}