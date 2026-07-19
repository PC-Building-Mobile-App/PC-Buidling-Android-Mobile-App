package com.iti.presentation.buildgeneration.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.presentation.R
import com.iti.presentation.buildgeneration.BuildGenerationContract.Effect
import com.iti.presentation.buildgeneration.BuildGenerationContract.Event
import com.iti.presentation.buildgeneration.BuildGenerationContract.State
import com.iti.presentation.buildgeneration.components.BrandPreferenceSelector
import com.iti.presentation.buildgeneration.components.BudgetSliderCard
import com.iti.presentation.buildgeneration.components.BuildGenerationHeader
import com.iti.presentation.buildgeneration.components.ComponentPickerBottomSheet
import com.iti.presentation.buildgeneration.components.ComponentSlotCard
import com.iti.presentation.buildgeneration.components.GenerateBuildButton
import com.iti.presentation.buildgeneration.components.PurposeSelector
import com.iti.presentation.buildgeneration.components.SaveBuildDialog
import com.iti.presentation.buildgeneration.viewmodel.BuildGenerationViewModel
import com.iti.presentation.categorybuilds.model.BuildUiModel
import com.iti.presentation.components.AppSnackbar
import com.iti.presentation.components.LocalSnackbarController
import com.iti.presentation.components.SnackbarController
import com.iti.presentation.core.components.PrimaryButton
import com.iti.presentation.mypcs.model.BuildCategoryUiModel
import com.iti.presentation.ui.theme.AppTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildGenerationScreen(
    modifier: Modifier = Modifier,
    category: BuildCategoryUiModel? = null,
    editingBuild: BuildUiModel? = null,
    onBackClick: () -> Unit,
    viewModel: BuildGenerationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarController = remember(snackbarHostState) { SnackbarController(snackbarHostState) }

    LaunchedEffect(category, editingBuild) {
        viewModel.onEvent(Event.Initialize(category, editingBuild))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is Effect.NavigateBack -> onBackClick()
                is Effect.ShowMessage -> {
                    if (effect.isError) snackbarController.showError(effect.message.asString(context))
                    else snackbarController.showSuccess(effect.message.asString(context))
                }
            }
        }
    }

    CompositionLocalProvider(LocalSnackbarController provides snackbarController) {
        Box(modifier = modifier.fillMaxSize()) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                modifier = Modifier.fillMaxSize(),
            ) { paddingValues ->
                BuildGenerationScreenContent(
                    state = state,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.padding(paddingValues),
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.TopCenter),
            ) { data -> AppSnackbar(data) }
        }

        val pickerCategory = state.pickerCategory
        if (state.isPickerVisible && pickerCategory != null) {
            ComponentPickerBottomSheet(
                category = pickerCategory,
                components = state.pickerComponents,
                isLoading = state.isPickerLoading,
                onComponentSelected = { viewModel.onEvent(Event.ComponentPicked(it)) },
                onDismiss = { viewModel.onEvent(Event.PickerDismissed) },
            )
        }

        if (state.isSaveDialogVisible) {
            SaveBuildDialog(
                buildName = state.buildName,
                isSaving = state.isSaving,
                onNameChanged = { viewModel.onEvent(Event.BuildNameChanged(it)) },
                onConfirm = { viewModel.onEvent(Event.ConfirmSaveClicked) },
                onDismiss = { viewModel.onEvent(Event.SaveDialogDismissed) },
            )
        }
    }
}

@Composable
private fun BuildGenerationScreenContent(
    state: State,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isComponentsExpanded by remember { mutableStateOf(true) }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            BuildGenerationHeader(
                onBackClick = { onEvent(Event.BackClicked) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            BudgetSliderCard(
                budget = state.budget,
                onBudgetChanged = { onEvent(Event.BudgetChanged(it)) },
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            PurposeSelector(
                selectedCategoryTypes = state.selectedCategoryTypes,
                onCategoryTypeToggled = { onEvent(Event.CategoryTypeToggled(it)) },
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            BrandPreferenceSelector(
                selectedBrands = state.selectedBrands,
                onBrandToggled = { onEvent(Event.BrandToggled(it)) },
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            ComponentsSectionHeader(
                filledCount = state.filledSlotsCount,
                totalCount = state.slots.size,
                isExpanded = isComponentsExpanded,
                onToggleExpand = { isComponentsExpanded = !isComponentsExpanded },
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            AnimatedVisibility(
                visible = isComponentsExpanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    state.slots.forEach { slot ->
                        ComponentSlotCard(
                            category = slot.category,
                            component = slot.component,
                            warningMessage = slot.warningMessage,
                            onClick = { onEvent(Event.SlotClicked(slot.category)) },
                            onRemoveClick = { onEvent(Event.SlotCleared(slot.category)) },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        )
                    }
                }
            }
        }

        if (!state.allSlotsFilled) {
            item {
                GenerateBuildButton(
                    isGenerating = state.isGenerating,
                    hasSelection = state.filledSlotsCount > 0,
                    onClick = { onEvent(Event.GenerateClicked) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }

        if (state.allSlotsFilled) {
            val components = state.slots.mapNotNull { it.component }
            val totalPriceFormatted = NumberFormat.getNumberInstance(Locale.US).format(components.sumOf { it.price }.toLong())

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.total_price_label),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(R.string.price_format, totalPriceFormatted),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            item {
                PrimaryButton(
                    text = stringResource(R.string.save_build_button),
                    isLoading = state.isSaving,
                    onClick = { onEvent(Event.SaveClicked) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }
}

@Composable
private fun ComponentsSectionHeader(
    filledCount: Int,
    totalCount: Int,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggleExpand
            )
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp),
            )
            Text(
                text = stringResource(R.string.components_section_title),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = stringResource(R.string.components_section_subtitle_format, filledCount, totalCount),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun BuildGenerationScreenContentPreview() {
    AppTheme {
        BuildGenerationScreenContent(state = State(), onEvent = {})
    }
}