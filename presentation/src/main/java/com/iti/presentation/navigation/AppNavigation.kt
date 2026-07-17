package com.iti.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

@Composable
fun AppNavigation(
    isAuthenticated: Boolean,
    hasSeenOnboarding: Boolean,
    onAuthComplete: () -> Unit,
) {
    if (isAuthenticated) {
        MainNavigation(
            onNavigateToAuth = {
                // TODO:  Handle sign-out by flipping isAuthenticated in your ViewModel
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
                    // TODO: Replace with your OnboardingScreen composable
                    ScreenPlaceholder(
                        title = "Onboarding",
                        subtitle = "Tap to go to Login",
                        onAction = {
                            // TODO: Save to DataStore/SharedPreferences that onboarding is complete
                            authBackStack.navigateSingleTop(LoginRoute)
                        },
                    )
                }

                entry<LoginRoute> {
                    // TODO: Replace with your LoginScreen composable
                    ScreenPlaceholder(
                        title = "Login",
                        subtitle = "Tap to go to Register screen\n(Pretend there's a separate 'Sign In' button that completes auth)",
                        onAction = {
                            // Navigate to Register
                            authBackStack.navigateSingleTop(RegisterRoute)

                            // NOTE: If they actually successfully signed in here, you would call:
                            // onAuthComplete()
                        },
                    )
                }

                entry<RegisterRoute> {
                    // TODO: Replace with your RegisterScreen composable
                    ScreenPlaceholder(
                        title = "Register",
                        subtitle = "Tap to finish registration and go to Home",
                        onAction = {

                            onAuthComplete()
                        },
                    )
                }            },
        )
    }
}