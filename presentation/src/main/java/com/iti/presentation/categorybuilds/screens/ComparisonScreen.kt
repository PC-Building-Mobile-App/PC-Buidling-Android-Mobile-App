package com.iti.presentation.categorybuilds.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.presentation.R
import com.iti.presentation.categorybuilds.ComparisonContract.Effect
import com.iti.presentation.categorybuilds.ComparisonContract.Event
import com.iti.presentation.categorybuilds.ComparisonContract.State
import com.iti.presentation.categorybuilds.components.AiRecommendationCard
import com.iti.presentation.categorybuilds.components.CategoryChipsRow
import com.iti.presentation.categorybuilds.components.ComparisonBottomBar
import com.iti.presentation.categorybuilds.components.ComparisonHeader
import com.iti.presentation.categorybuilds.components.DifferenceCard
import com.iti.presentation.categorybuilds.components.SpecRow
import com.iti.presentation.categorybuilds.viewmodel.ComparisonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonScreen(
    buildIds: List<Int>,
    onBackClick: () -> Unit,
    viewModel: ComparisonViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(buildIds) {
        viewModel.onEvent(Event.Initialize(buildIds))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is Effect.NavigateBack -> onBackClick()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.comparison_results_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(Event.BackClicked) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            ComparisonBottomBar(
                onChange = { viewModel.onEvent(Event.ChangeClicked) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.builds.isNotEmpty()) {
                ComparisonContent(
                    state = state,
                    onTabSelected = { viewModel.onEvent(Event.TabSelected(it)) }
                )
            } else if (state.isLoadingDetails) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun ComparisonContent(
    state: State,
    onTabSelected: (Int) -> Unit
) {
    val selectedCategory = state.categories[state.selectedTabIndex]

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ComparisonHeader(builds = state.builds, isLoading = state.isLoadingDetails)
        }

        item {
            CategoryChipsRow(
                categories = state.categories,
                selectedIndex = state.selectedTabIndex,
                onCategorySelected = onTabSelected
            )
        }

        if (selectedCategory == "Overview") {
            item {
                AiRecommendationCard(
                    recommendation = state.comparison?.recommendation,
                    isLoading = state.isAiLoading,
                    errorMessage = state.aiErrorMessage?.asString(),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            val allDifferences = state.comparison?.keyDifferences ?: emptyList()
            if (allDifferences.isNotEmpty()) {
                item {
                    DifferenceCard(
                        differences = allDifferences,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.detailed_comparison_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        } else {
            item {
                val categoryDiff = state.comparison?.keyDifferences?.filter {
                    it.contains(selectedCategory, ignoreCase = true) ||
                            (selectedCategory.equals("MEMORY", true) && it.contains("RAM", true)) ||
                            (selectedCategory.equals("COOLER", true) && it.contains(
                                "Cooling",
                                true
                            ))
                } ?: emptyList()

                if (categoryDiff.isNotEmpty()) {
                    DifferenceCard(
                        differences = categoryDiff,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        val partCategories = if (selectedCategory == "Overview") {
            state.categories.filter { it != "Overview" }
        } else {
            listOf(selectedCategory)
        }

        items(partCategories) { category ->
            SpecRow(
                category = category,
                builds = state.builds,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
