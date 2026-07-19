package com.iti.presentation.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.presentation.core.componentcategories.model.ComponentCategoryUiModel
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.home.HomeContract.Effect
import com.iti.presentation.home.HomeContract.Event
import com.iti.presentation.home.HomeContract.State
import com.iti.presentation.home.components.CategoriesSection
import com.iti.presentation.home.components.FeaturedComponentsSection
import com.iti.presentation.home.components.HeroSection
import com.iti.presentation.home.components.HomeHeader
import com.iti.presentation.home.components.HomeSearchBar
import com.iti.presentation.home.components.LatestNewsSection
import com.iti.presentation.home.model.HardwareNewsUiModel
import com.iti.presentation.home.model.PlatformStatUiModel
import com.iti.presentation.home.viewmodel.HomeViewModel
import com.iti.presentation.ui.theme.AppTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPartsWithQuery: (String) -> Unit,
    onNavigateToParts: () -> Unit,
    onNavigateToGenerateBuild: () -> Unit,
    onNavigateToComponentDetail: (Long) -> Unit,
    onNavigateToPartsWithCategory: (String) -> Unit,
    onNavigateToHardwareNews: () -> Unit,
    onNavigateToNewsDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.NavigateToPartsWithQuery -> onNavigateToPartsWithQuery(effect.query)
                is Effect.NavigateToParts -> onNavigateToParts()
                is Effect.NavigateToGenerateBuild -> onNavigateToGenerateBuild()
                is Effect.NavigateToComponentDetail -> onNavigateToComponentDetail(effect.componentId)
                is Effect.NavigateToPartsWithCategory -> onNavigateToPartsWithCategory(effect.categoryId)
                is Effect.NavigateToHardwareNews -> onNavigateToHardwareNews()
                is Effect.NavigateToNewsDetail -> onNavigateToNewsDetail(effect.articleId)
            }
        }
    }

    HomeScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun HomeScreenContent(
    state: State,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item(key = "header") {
            HomeHeader(
                userName = state.userName,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }

        item(key = "search") {
            HomeSearchBar(
                query = state.searchQuery,
                onQueryChange = { onEvent(Event.UpdateSearchQuery(it)) },
                onSearch = { onEvent(Event.SearchSubmitted) },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item(key = "hero") {
            Spacer(Modifier.height(20.dp))
            HeroSection(
                stats = state.stats,
                isLoading = state.isLoading,
                onGenerateBuild = { onEvent(Event.GenerateBuildClicked) },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item(key = "featured") {
            Spacer(Modifier.height(28.dp))
            FeaturedComponentsSection(
                components = state.featuredComponents,
                isLoading = state.isLoading,
                onSeeAll = { onEvent(Event.SeeAllComponentsClicked) },
                onComponentClick = { onEvent(Event.ComponentClicked(it)) }
            )
        }

        item(key = "categories") {
            Spacer(Modifier.height(28.dp))
            CategoriesSection(
                categories = state.categories,
                isExpanded = state.isCategoriesExpanded,
                isLoading = state.isLoading,
                onToggleExpanded = { onEvent(Event.ToggleCategoriesExpanded) },
                onCategoryClick = { onEvent(Event.CategoryClicked(it)) }
            )
        }

        item(key = "news") {
            Spacer(Modifier.height(28.dp))
            LatestNewsSection(
                news = state.latestNews,
                isLoading = state.isLoading,
                onSeeAll = { onEvent(Event.SeeAllNewsClicked) },
                onNewsClick = { onEvent(Event.NewsClicked(it)) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreenContent(
            state = State(
                isLoading = false,
                userName = "Mock User",
                stats = listOf(
                    PlatformStatUiModel("builds", "12K+", "BUILDS"),
                    PlatformStatUiModel("parts", "4,800", "PARTS"),
                    PlatformStatUiModel("stores", "42", "STORES")
                ),
                featuredComponents = listOf(
                    ComponentUiModel(
                        1,
                        "AMD · CPU",
                        "Ryzen 9 7950X",
                        "18,500 EGP",
                        "",
                        listOf("Top Pick", "In Stock"),
                        true
                    ),
                    ComponentUiModel(2, "NVIDIA · GPU", "RTX 4080", "52,000 EGP", "", listOf("In Stock"), true),
                ),
                categories = listOf(
                    ComponentCategoryUiModel("CPU", "Processors", "472 parts", com.iti.presentation.R.drawable.ic_cpu),
                    ComponentCategoryUiModel("GPU", "Graphics", "200 parts", com.iti.presentation.R.drawable.ic_gpu),
                    ComponentCategoryUiModel("PSU", "Power", "134 parts", com.iti.presentation.R.drawable.ic_psu),
                    ComponentCategoryUiModel("COOLER", "Cooling", "89 parts", com.iti.presentation.R.drawable.ic_cooler),
                ),
                latestNews = listOf(
                    HardwareNewsUiModel("1", "RTX 5090 rumored for Q1 2027", "NVIDIA", "", "https://example.com", "Jul 15, 2026"),
                    HardwareNewsUiModel("2", "AMD Zen 6 leaks show massive IPC gains", "AMD", "", "https://example.com", "Jul 14, 2026"),
                ),
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun HomeScreenLoadingPreview() {
    AppTheme {
        HomeScreenContent(
            state = State(isLoading = true),
            onEvent = {}
        )
    }
}
