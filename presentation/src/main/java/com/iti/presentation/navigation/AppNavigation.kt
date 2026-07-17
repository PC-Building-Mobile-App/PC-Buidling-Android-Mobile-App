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
    onAuthComplete: () -> Unit,
) {
    if (isAuthenticated) {
        MainNavigation(
            onNavigateToAuth = {
                // TODO:  Handle sign-out by flipping isAuthenticated in your ViewModel

            },
        )
    } else {
        AuthNavigation(onAuthComplete = onAuthComplete)
    }
}

@Composable
private fun AuthNavigation(
    onAuthComplete: () -> Unit,
) {
    val authBackStack: SnapshotStateList<Any> = remember {
        mutableListOf<Any>(OnboardingRoute).toMutableStateList()
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
                            authBackStack.navigateSingleTop(LoginRoute)
                        },
                    )
                }

                entry<LoginRoute> {
                    // TODO: Replace with your LoginScreen composable
                    ScreenPlaceholder(
                        title = "Login",
                        subtitle = "Tap to sign in (or swipe back for Register)",
                        onAction = onAuthComplete,
                    )
                }

                entry<RegisterRoute> {
                    // TODO: Replace with your RegisterScreen composable
                    ScreenPlaceholder(
                        title = "Register",
                        subtitle = "Tap to complete registration",
                        onAction = onAuthComplete,
                    )
                }
            },
        )
    }
}
