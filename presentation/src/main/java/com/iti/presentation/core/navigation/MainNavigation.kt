package com.iti.presentation.core.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.iti.presentation.buildgeneration.screens.BuildGenerationScreen
import com.iti.presentation.categorybuilds.SelectionManagerViewModel
import com.iti.presentation.categorybuilds.components.CompareSelectionBar
import com.iti.presentation.categorybuilds.screens.CategoryBuildsScreen
import com.iti.presentation.categorybuilds.screens.ComparisonScreen
import com.iti.presentation.hardwarenews.screens.HardwareNewsScreen
import com.iti.presentation.hardwarenewsdetails.screens.HardwareNewsDetailScreen
import com.iti.presentation.home.screens.HomeScreen
import com.iti.presentation.mypcs.screens.MyPcsScreen
import com.iti.presentation.partdetails.PartDetailsScreen
import com.iti.presentation.parts.screens.PartsScreen
import com.iti.presentation.profile.screens.ProfileScreen

@Composable
fun MainNavigation(
    onNavigateToAuth: () -> Unit = {},
    selectionViewModel: SelectionManagerViewModel = hiltViewModel(),
) {
    val selectionManager = selectionViewModel.manager
    val isSelectionMode by selectionManager.isSelectionMode.collectAsState()
    val selectedBuilds by selectionManager.selectedBuilds.collectAsState()

    var currentTab by rememberSaveable { mutableStateOf(TopLevelRoute.HOME) }

    val backStacks: Map<TopLevelRoute, SnapshotStateList<Route>> = remember {
        TopLevelRoute.entries.associateWith { tab ->
            when (tab) {
                TopLevelRoute.MY_PCS -> mutableListOf(MyPcsRoute()).toMutableStateList()
                else -> mutableListOf(tab.route).toMutableStateList()
            }
        }
    }

    val activeBackStack = backStacks.getValue(currentTab)
    val currentRoute = activeBackStack.lastOrNull()
    val isOnBuildGeneration = currentRoute is BuildGenerationRoute
    val isOnComparison = currentRoute is ComparisonRoute
    val isOnAiChat = currentRoute is AiAssistantRoute

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AnimatedVisibility(
                visible = !isOnBuildGeneration && !isOnAiChat,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                BottomNavBar(
                    currentRoute = currentTab,
                    onItemClick = { tab ->
                        if (tab == currentTab) {
                            while (activeBackStack.size > 1) {
                                activeBackStack.removeLastOrNull()
                            }
                        } else {
                            if (currentTab == TopLevelRoute.MY_PCS) {
                                selectionManager.clearSelection()
                            }
                            val newStack = backStacks.getValue(tab)
                            newStack.clear()
                            newStack.add(tab.route)
                            currentTab = tab
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            NavDisplay(
                backStack = activeBackStack,
                onBack = {
                    if (activeBackStack.size > 1) {
                        activeBackStack.removeLastOrNull()
                    } else if (currentTab != TopLevelRoute.HOME) {
                        val homeStack = backStacks.getValue(TopLevelRoute.HOME)
                        homeStack.clear()
                        homeStack.add(HomeRoute)
                        currentTab = TopLevelRoute.HOME
                    }
                },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {

                    entry<HomeRoute> {
                        HomeScreen(
                            onNavigateToPartsWithQuery = { query ->
                                val partsStack = backStacks.getValue(TopLevelRoute.PARTS)
                                partsStack.clear()
                                partsStack.add(PartsRoute(initialQuery = query))
                                currentTab = TopLevelRoute.PARTS
                            },
                            onNavigateToParts = {
                                val partsStack = backStacks.getValue(TopLevelRoute.PARTS)
                                if (partsStack.firstOrNull() != PartsRoute()) {
                                    partsStack.clear()
                                    partsStack.add(PartsRoute())
                                }
                                currentTab = TopLevelRoute.PARTS
                            },
                            onNavigateToGenerateBuild = {
                                activeBackStack.navigateSingleTop(
                                    BuildGenerationRoute(),
                                )
                            },
                            onNavigateToComponentDetail = { componentJson ->
                                activeBackStack.navigateSingleTop(
                                    PartsDetailRoute(componentJson = componentJson),
                                )
                            },
                            onNavigateToPartsWithCategory = { categoryId ->
                                val partsStack = backStacks.getValue(TopLevelRoute.PARTS)
                                partsStack.clear()
                                partsStack.add(PartsRoute(initialCategoryId = categoryId))
                                currentTab = TopLevelRoute.PARTS
                            },
                            onNavigateToHardwareNews = {
                                activeBackStack.navigateSingleTop(HardwareNewsListRoute)
                            },
                            onNavigateToNewsDetail = { articleId ->
                                activeBackStack.navigateSingleTop(
                                    HardwareNewsDetailRoute(articleId = articleId),
                                )
                            },
                        )
                    }

                    entry<HardwareNewsListRoute> {
                        HardwareNewsScreen(
                            onArticleClick = { articleId ->
                                activeBackStack.navigateSingleTop(
                                    HardwareNewsDetailRoute(articleId = articleId),
                                )
                            },
                            onBackClick = {
                                activeBackStack.navigateBack()
                            },
                        )
                    }

                    entry<HardwareNewsDetailRoute> { route ->
                        HardwareNewsDetailScreen(
                            articleId = route.articleId,
                            onBackClick = {
                                activeBackStack.navigateBack()
                            },
                        )
                    }

                    entry<PartsRoute> { route ->
                        PartsScreen(
                            initialQuery = route.initialQuery,
                            initialCategoryId = route.initialCategoryId,
                            onNavigateToDetail = { componentJson ->
                                activeBackStack.navigateSingleTop(
                                    PartsDetailRoute(componentJson = componentJson),
                                )
                            },
                        )
                    }

                    entry<AiAssistantRoute> {
                        com.iti.presentation.aichat.screens.AiChatScreen(
                            onNavigateToBuild = {
                                activeBackStack.navigateSingleTop(
                                    BuildGenerationRoute(),
                                )
                            },
                            onNavigateToCompare = {
                                // Transition to My PCs for build comparison
                                currentTab = TopLevelRoute.MY_PCS
                                selectionManager.toggleSelectionMode()
                            },
                            onNavigateToProductDetail = { componentJson ->
                                activeBackStack.navigateSingleTop(
                                    PartsDetailRoute(componentJson = componentJson),
                                )
                            },
                            onBackClick = {
                                val homeStack = backStacks.getValue(TopLevelRoute.HOME)
                                homeStack.clear()
                                homeStack.add(HomeRoute)
                                currentTab = TopLevelRoute.HOME
                            },
                        )
                    }

                    entry<MyPcsRoute> { route ->
                        MyPcsScreen(
                            shouldRefresh = route.shouldRefresh,
                            onRefreshHandled = {
                                val myPcsStack = backStacks.getValue(TopLevelRoute.MY_PCS)
                                if (myPcsStack.isNotEmpty() && myPcsStack[0] is MyPcsRoute) {
                                    myPcsStack[0] = MyPcsRoute(shouldRefresh = false)
                                }
                            },
                            onNewBuildClick = {
                                activeBackStack.navigateSingleTop(
                                    BuildGenerationRoute(category = null)
                                )
                            },
                            onCategoryClick = { category ->
                                activeBackStack.navigateSingleTop(
                                    BuildCategoryRoute(category = category),
                                )
                            },
                        )
                    }

                    entry<ProfileRoute> {
                        ProfileScreen(
                            onNavigateToSavedBuilds = {
                                currentTab = TopLevelRoute.MY_PCS
                            },
                        )
                    }

                    entry<PartsDetailRoute> { route ->
                        PartDetailsScreen(
                            componentJson = route.componentJson,
                            onBackClick = {
                                activeBackStack.navigateBack()
                            },
                            onAddToBuildClick = { component ->
                                activeBackStack.navigateSingleTop(BuildGenerationRoute())
                            },
                        )
                    }

                    entry<BuildCategoryRoute> { route ->
                        CategoryBuildsScreen(
                            category = route.category,
                            onBackClick = {
                                activeBackStack.navigateBack()
                            },
                            onEditBuildClick = { build, category ->
                                activeBackStack.navigateSingleTop(
                                    BuildGenerationRoute(
                                        editingBuild = build,
                                        category = category)
                                )
                            },
                            onNewBuildClick = { category ->
                                activeBackStack.navigateSingleTop(
                                    BuildGenerationRoute(category = category)
                                )
                            },
                            onNavigateToComparison = { ids ->
                                activeBackStack.navigateSingleTop(
                                    ComparisonRoute(buildIds = ids)
                                )
                            }
                        )
                    }

                    entry<BuildGenerationRoute>{ route ->
                        BuildGenerationScreen(
                            category = route.category,
                            editingBuild = route.editingBuild,
                            onBackClick = {
                                activeBackStack.navigateBack()
                            },
                            onBackWithSaveSuccess = {
                                val myPcsStack = backStacks.getValue(TopLevelRoute.MY_PCS)
                                if (myPcsStack.isNotEmpty()) {
                                    myPcsStack[0] = MyPcsRoute(shouldRefresh = true)
                                }
                                activeBackStack.navigateBack()
                            }
                        )
                    }

                    entry<ComparisonRoute> { route ->
                        ComparisonScreen(
                            buildIds = route.buildIds,
                            onBackClick = {
                                activeBackStack.navigateBack()
                            }
                        )
                    }
                },
            )

            CompareSelectionBar(
                selectedBuilds = selectedBuilds,
                isVisible = isSelectionMode && currentTab == TopLevelRoute.MY_PCS && !isOnComparison,
                onRemove = { selectionManager.toggleBuildSelection(it) },
                onCancel = { selectionManager.clearSelection() },
                onCompare = {
                    val ids = selectedBuilds.mapNotNull { it.id.toIntOrNull() }
                    if (ids.size >= 2) {
                        activeBackStack.navigateSingleTop(ComparisonRoute(buildIds = ids))
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
