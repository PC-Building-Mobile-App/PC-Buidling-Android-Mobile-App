package com.iti.presentation.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.iti.presentation.onboarding.OnboardingViewModel
import com.iti.presentation.onboarding.screens.OnboardingScreen

@Composable
fun AppNavigation(
    isAuthenticated: Boolean,
    hasSeenOnboarding: Boolean,
    onAuthComplete: () -> Unit,
) {
    if (isAuthenticated) {
        MainNavigation(
            onNavigateToAuth = {

            },
        )
    } else {
        AuthNavigation(
            hasSeenOnboarding = hasSeenOnboarding,
            onAuthComplete = onAuthComplete
        )
    }
}

@Composable
private fun AuthNavigation(
    hasSeenOnboarding: Boolean,
    onAuthComplete: () -> Unit,
) {
    val authBackStack: SnapshotStateList<Route> = remember(hasSeenOnboarding) {
        val startDestination = if (hasSeenOnboarding) LoginRoute else OnboardingRoute
        mutableListOf<Route>(startDestination).toMutableStateList()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavDisplay(
            backStack = authBackStack,
            onBack = {
                if (authBackStack.size > 1) {
                    authBackStack.removeLastOrNull()
                }
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {

                entry<OnboardingRoute> {
                    val viewModel: OnboardingViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsState()

                    OnboardingScreen(
                        state = state,
                        effectFlow = viewModel.effect,
                        onEvent = viewModel::onEvent,
                        onNavigateToAuth = {
                            authBackStack.navigateSingleTop(LoginRoute)
                        }
                    )
                }

                entry<LoginRoute> {
                    ScreenPlaceholder(
                        title = "Login",
                        subtitle = "Tap to go to Register screen\n(Pretend there's a separate 'Sign In' button that completes auth)",
                        onAction = {
                            authBackStack.navigateSingleTop(RegisterRoute)
                        },
                    )
                }

                entry<RegisterRoute> {
                    ScreenPlaceholder(
                        title = "Register",
                        subtitle = "Tap to finish registration and go to Home",
                        onAction = {
                            onAuthComplete()
                        },
                    )
                }
            },
        )
    }
}