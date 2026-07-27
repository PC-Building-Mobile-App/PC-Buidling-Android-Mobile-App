    package com.iti.presentation.buildgeneration.screens


    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.BoxWithConstraints
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.WindowInsets
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.heightIn
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.FabPosition
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
    import com.iti.presentation.buildgeneration.components.AiGenerateFab
    import com.iti.presentation.buildgeneration.components.BrandPreferenceSelector
    import com.iti.presentation.buildgeneration.components.BudgetSliderCard
    import com.iti.presentation.buildgeneration.components.BuildGenerationHeader
    import com.iti.presentation.buildgeneration.components.ComponentPickerBottomSheet
    import com.iti.presentation.buildgeneration.components.ComponentsSection
    import com.iti.presentation.buildgeneration.components.PurposeSelector
    import com.iti.presentation.buildgeneration.components.SaveBuildDialog
    import com.iti.presentation.buildgeneration.model.totalPrice
    import com.iti.presentation.buildgeneration.viewmodel.BuildGenerationViewModel
    import com.iti.presentation.categorybuilds.model.BuildUiModel
    import com.iti.presentation.core.uicomponents.AppSnackbar
    import com.iti.presentation.core.uicomponents.LocalSnackbarController
    import com.iti.presentation.core.uicomponents.SnackbarController
    import com.iti.presentation.core.uicomponents.PrimaryButton
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
        onBackWithSaveSuccess: () -> Unit = {},
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
                    is Effect.NavigateBackWithSaveSuccess -> onBackWithSaveSuccess()
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
                    floatingActionButton = {
                        if (!state.isSaving) {
                            AiGenerateFab(
                                modifier = Modifier.padding(bottom = 72.dp),
                                isLoading = state.isGenerating,
                                onClick = { viewModel.onEvent(Event.GenerateClicked) },
                            )
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End,
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

        BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = maxHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                BuildGenerationHeader(
                    onBackClick = { onEvent(Event.BackClicked) },
                    modifier = Modifier.fillMaxWidth()
                )

                BudgetSliderCard(
                    budget = state.budget,
                    onBudgetChanged = { onEvent(Event.BudgetChanged(it)) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                PurposeSelector(
                    selectedCategoryType = state.selectedCategoryType,
                    onCategoryTypeSelected = {
                        if (!state.isCategoryLocked) onEvent(Event.CategoryTypeToggled(it))
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                BrandPreferenceSelector(
                    selectedBrands = state.selectedBrands,
                    onBrandToggled = { onEvent(Event.BrandToggled(it)) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                ComponentsSection(
                    filledCount = state.filledSlotsCount,
                    totalCount = state.slots.size,
                    slots = state.slots,
                    isExpanded = isComponentsExpanded,
                    onToggleExpand = { isComponentsExpanded = !isComponentsExpanded },
                    onSlotClick = { category -> onEvent(Event.SlotClicked(category)) },
                    onSlotRemoveClick = { category -> onEvent(Event.SlotCleared(category)) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                Spacer(modifier = Modifier.weight(1f))

                if (state.canSave) {
                    val components = state.slots.mapNotNull { it.component }
                    val totalPriceFormatted = NumberFormat.getNumberInstance(Locale.US)
                        .format(components.totalPrice.toLong())
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
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

                PrimaryButton(
                    text = stringResource(R.string.save_build_button),
                    isLoading = state.isSaving,
                    enabled = state.canSave,
                    onClick = { onEvent(Event.SaveClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )
            }
        }
    }



    @Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
    @Composable
    private fun BuildGenerationScreenContentPreview() {
        AppTheme {
            BuildGenerationScreenContent(state = State(), onEvent = {})
        }
    }