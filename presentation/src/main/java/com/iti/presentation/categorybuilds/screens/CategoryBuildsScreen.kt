package com.iti.presentation.categorybuilds.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.R
import com.iti.presentation.categorybuilds.CategoryBuildsContract.Effect
import com.iti.presentation.categorybuilds.CategoryBuildsContract.Event
import com.iti.presentation.categorybuilds.CategoryBuildsContract.State
import com.iti.presentation.categorybuilds.components.BuildCard
import com.iti.presentation.categorybuilds.components.BuildsSkeleton
import com.iti.presentation.categorybuilds.components.CategoryBuildsFab
import com.iti.presentation.categorybuilds.components.CategoryBuildsHeader
import com.iti.presentation.categorybuilds.model.BuildUiModel
import com.iti.presentation.categorybuilds.viewmodel.CategoryBuildsViewModel
import com.iti.presentation.core.uicomponents.EmptyScreen
import com.iti.presentation.core.uicomponents.ErrorScreen
import com.iti.presentation.core.UiText
import com.iti.presentation.mypcs.model.BuildCategoryUiModel
import com.iti.presentation.ui.theme.AppTheme

@Composable
fun CategoryBuildsScreen(
    onBackClick: () -> Unit,
    onNewBuildClick: (BuildCategoryUiModel) -> Unit,
    onEditBuildClick: (build: BuildUiModel, category: BuildCategoryUiModel) -> Unit,
    category: BuildCategoryUiModel,
    modifier: Modifier = Modifier,
    viewModel: CategoryBuildsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(category.id) {
        viewModel.onEvent(Event.Initialize(category))
    }

    LaunchedEffect(key1 = true) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is Effect.NavigateBack -> onBackClick()
                is Effect.NavigateToNewBuild -> onNewBuildClick(effect.category)
                is Effect.NavigateToEditBuild -> {
                    state.category?.let { onEditBuildClick(effect.build, it) }
                }
                is Effect.ShareBuild -> {}
                is Effect.ExportBuild -> {}
            }
        }
    }

    CategoryBuildsScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
private fun CategoryBuildsScreenContent(
    state: State,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                CategoryBuildsHeader(
                    categoryName = state.category?.name.orEmpty(),
                    categoryDescription = state.category?.description.orEmpty(),
                    categoryType = state.category?.type ?: BuildCategoryType.GAMING,
                    onBackClick = { onEvent(Event.BackClicked) },
                )
            }

            when {
                state.errorMessage != null -> Unit
                state.isLoading -> {
                    item {
                        BuildsSkeleton(modifier = Modifier.padding(horizontal = 20.dp))
                    }
                }
                state.isEmpty -> Unit
                else -> {
                    items(state.builds, key = { it.id }) { build ->
                        state.category?.let {
                            BuildCard(
                                build = build,
                                categoryType = state.category.type,
                                onEditClick = { onEvent(Event.EditClicked(build)) },
                                onShareClick = { onEvent(Event.ShareClicked(build.id)) },
                                onExportClick = { onEvent(Event.ExportClicked(build.id)) },
                                modifier = Modifier.padding(horizontal = 20.dp),
                            )
                        }
                    }
                }
            }
        }

        if (state.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 88.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                ErrorScreen(message = state.errorMessage.asString(LocalContext.current))
            }
        }

        if (state.isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 88.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                EmptyScreen(
                    title = stringResource(R.string.no_builds_yet_title),
                    message = stringResource(R.string.no_builds_yet_message),
                )
            }
        }

        if (!state.isLoading && state.errorMessage == null) {
            state.category?.let { category ->
                CategoryBuildsFab(
                    categoryType = category.type,
                    onClick = { onEvent(Event.NewBuildClicked) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp),
                )
            }
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun CategoryBuildsScreenLoadingPreview() {
    AppTheme {
        CategoryBuildsScreenContent(
            state = State(
                isLoading = true,
                category = BuildCategoryUiModel(
                    id = "gaming",
                    name = "Gaming",
                    description = "High FPS, max settings",
                    buildsCount = 3,
                    type = BuildCategoryType.GAMING,
                ),
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun CategoryBuildsScreenErrorPreview() {
    AppTheme {
        CategoryBuildsScreenContent(
            state = State(
                category = BuildCategoryUiModel(
                    id = "gaming",
                    name = "Gaming",
                    description = "High FPS, max settings",
                    buildsCount = 3,
                    type = BuildCategoryType.GAMING,
                ),
                errorMessage = UiText.DynamicString("Something went wrong"),
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun CategoryBuildsScreenEmptyPreview() {
    AppTheme {
        CategoryBuildsScreenContent(
            state = State(
                category = BuildCategoryUiModel(
                    id = "gaming",
                    name = "Gaming",
                    description = "High FPS, max settings",
                    buildsCount = 0,
                    type = BuildCategoryType.GAMING,
                ),
                builds = emptyList(),
            ),
            onEvent = {},
        )
    }
}