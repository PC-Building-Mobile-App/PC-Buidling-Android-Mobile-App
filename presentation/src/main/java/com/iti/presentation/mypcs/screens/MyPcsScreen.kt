package com.iti.presentation.mypcs.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.iti.presentation.core.uicomponents.ErrorScreen
import com.iti.presentation.mypcs.MyPcsContract.Effect
import com.iti.presentation.mypcs.MyPcsContract.Event
import com.iti.presentation.mypcs.MyPcsContract.State
import com.iti.presentation.mypcs.components.BuildCategoryCard
import com.iti.presentation.mypcs.components.BuildCategoryCardSkeleton
import com.iti.presentation.mypcs.components.NewBuildButton
import com.iti.presentation.mypcs.model.BuildCategoryUiModel
import com.iti.presentation.mypcs.viewmodel.MyPcsViewModel
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.TextSecondary

@Composable
fun MyPcsScreen(
    onNewBuildClick: () -> Unit,
    onCategoryClick: (BuildCategoryUiModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPcsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is Effect.NavigateToCategory -> onCategoryClick(effect.category)
                is Effect.NavigateToNewBuild -> onNewBuildClick()
            }
        }
    }

    MyPcsScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
private fun MyPcsHeader(onNewBuildClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = stringResource(R.string.my_pcs),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )
            Text(
                text = stringResource(R.string.browse_builds_by_category),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
            )
        }

        NewBuildButton(onClick = onNewBuildClick)
    }
}

@Composable
private fun MyPcsScreenContent(
    state: State,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item(span = { GridItemSpan(2) }) {
                MyPcsHeader(onNewBuildClick = { onEvent(Event.NewBuildClicked) })
            }

            when {
                state.errorMessage != null -> Unit

                state.isLoading -> {
                    items(6) {
                        BuildCategoryCardSkeleton()
                    }
                }

                else -> {
                    items(state.categories, key = { it.id }) { category ->
                        BuildCategoryCard(
                            category = category,
                            onClick = { onEvent(Event.CategoryClicked(category)) },
                        )
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
                ErrorScreen(
                    message = state.errorMessage.asString(LocalContext.current),
                )
            }
        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun MyPcsScreenPreview() {
    AppTheme {
        MyPcsScreenContent(
            state = State(
                categories = listOf(
                    BuildCategoryUiModel(
                        id = "gaming",
                        name = "Gaming",
                        description = "High FPS, max settings",
                        buildsCount = 3,
                        type = BuildCategoryType.GAMING
                    ),
                    BuildCategoryUiModel(
                        id = "programming",
                        name = "Programming",
                        description = "Fast compile, multitasking",
                        buildsCount = 5,
                        type = BuildCategoryType.PROGRAMMING
                    ),
                    BuildCategoryUiModel(
                        id = "content_creation",
                        name = "Content Creation",
                        description = "4K editing, rendering",
                        buildsCount = 2,
                        type = BuildCategoryType.CONTENT_CREATION
                    ),
                    BuildCategoryUiModel(
                        id = "office",
                        name = "Office",
                        description = "Productivity & speed",
                        buildsCount = 1,
                        type = BuildCategoryType.OFFICE
                    ),
                    BuildCategoryUiModel(
                        id = "ai_workstation",
                        name = "AI Workstation",
                        description = "Deep learning, tensor computation",
                        buildsCount = 4,
                        type = BuildCategoryType.AI_WORKSTATION
                    ),
                    BuildCategoryUiModel(
                        id = "dream_builds",
                        name = "Dream Builds",
                        description = "No budget limits, custom loops",
                        buildsCount = 7,
                        type = BuildCategoryType.DREAM_BUILDS
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun MyPcsScreenLoadingPreview() {
    AppTheme {
        MyPcsScreenContent(
            state = State(isLoading = true),
            onEvent = {},
        )
    }
}