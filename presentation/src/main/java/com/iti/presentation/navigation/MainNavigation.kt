package com.iti.presentation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.iti.presentation.mypcs.screens.MyPcsScreen

@Composable
fun MainNavigation(
    onNavigateToAuth: () -> Unit = {},
) {
    var currentTab by rememberSaveable { mutableStateOf(TopLevelRoute.HOME) }

    val backStacks: Map<TopLevelRoute, SnapshotStateList<Route>> = remember {
        TopLevelRoute.entries.associateWith { tab -> mutableListOf(tab.route).toMutableStateList() }
    }

    val activeBackStack = backStacks.getValue(currentTab)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavBar(
                currentRoute = currentTab,
                onItemClick = { tab ->
                    if (tab == currentTab) {
                        while (activeBackStack.size > 1) {
                            activeBackStack.removeLastOrNull()
                        }
                    } else {
                        currentTab = tab
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavDisplay(
                backStack = activeBackStack,
                onBack = {
                    if (activeBackStack.size > 1) {
                        activeBackStack.removeLastOrNull()
                    } else if (currentTab != TopLevelRoute.HOME) {
                        currentTab = TopLevelRoute.HOME
                    }
                },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {

                    entry<HomeRoute> {
                        // TODO: Replace with your HomeScreen composable
                        ScreenPlaceholder(title = "Home")
                    }

                    entry<PartsRoute> {
                        // TODO: Replace with your PartsScreen composable
                        ScreenPlaceholder(
                            title = "Parts",
                            subtitle = "Tap a part to see details",
                            onAction = {
                                activeBackStack.navigateSingleTop(
                                    PartsDetailRoute(partId = "sample-part-id"),
                                )
                            },
                        )
                    }

                    entry<AiAssistantRoute> {
                        // TODO: Replace with your AIScreen composable
                        ScreenPlaceholder(title = "AI Assistant")
                    }

                    entry<MyPcsRoute> {
                        MyPcsScreen(
                                onNewBuildClick = {
                                },
                        onCategoryClick = { categoryId ->
                            activeBackStack.navigateSingleTop(
                                BuildCategoryRoute(category = categoryId),
                            )
                        },
                        )
                    }

                    entry<ProfileRoute> {
                        // TODO: Replace with your ProfileScreen composable
                        ScreenPlaceholder(title = "Profile")
                    }

                    entry<PartsDetailRoute> { route ->
                        // TODO: Replace with your PartsDetailScreen composable

                        ScreenPlaceholder(
                            title = "Part Detail",
                            subtitle = "Part ID: ${route.partId}",
                            onAction = {
                                activeBackStack.navigateSingleTop(
                                    ComparisonRoute(
                                        firstPartId = route.partId,
                                        secondPartId = "other-part-id",
                                    ),
                                )
                            },
                        )
                    }

                    entry<BuildCategoryRoute> { route ->
                        // TODO: Replace with your BuildCategoryScreen composable
                        ScreenPlaceholder(
                            title = "Build Category",
                            subtitle = route.category,
                            onAction = {
                                activeBackStack.navigateSingleTop(
                                    BuildGenerationRoute(buildId = "generated-build-123"),
                                )
                            },
                        )
                    }

                    entry<BuildGenerationRoute> { route ->
                        // TODO: Replace with your BuildGenerationScreen composable
                        ScreenPlaceholder(
                            title = "Build Generation",
                            subtitle = "Build ID: ${route.buildId}",
                        )
                    }

                    entry<ComparisonRoute> { route ->
                        // TODO: Replace with your ComparisonScreen composable
                        ScreenPlaceholder(
                            title = "Comparison",
                            subtitle = "${route.firstPartId} vs ${route.secondPartId}",
                        )
                    }
                },
            )
        }
    }
}

@Composable
internal fun ScreenPlaceholder(
    title: String,
    subtitle: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(if (onAction != null) Modifier.clickable { onAction() } else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = buildString {
                append(title)
                if (subtitle != null) append("\n$subtitle")
                if (onAction != null) append("\n(tap anywhere to navigate)")
            },
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}