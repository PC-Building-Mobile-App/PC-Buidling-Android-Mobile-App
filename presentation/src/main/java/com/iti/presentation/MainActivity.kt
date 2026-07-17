package com.iti.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.iti.presentation.navigation.AppNavigation
import com.iti.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                // Fake state for testing the navigation flow
                // TODO: Replace with your app's state
                var isAuthenticated by remember { mutableStateOf(false) }
                var hasSeenOnboarding by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        isAuthenticated = isAuthenticated,
                        hasSeenOnboarding = hasSeenOnboarding,
                        // TODO: Replace with your app's navigation
                        onAuthComplete = {
                            isAuthenticated = true
                            hasSeenOnboarding = false
                        },
                    )
                }
            }
        }
    }
}