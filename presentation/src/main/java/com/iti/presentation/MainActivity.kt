package com.iti.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.iti.presentation.core.navigation.AppNavigation
import com.iti.presentation.splash.SplashScreen
import com.iti.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val nativeSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        nativeSplash.setKeepOnScreenCondition { false }

        setContent {
            AppTheme {
                var isInitializing by remember { mutableStateOf(true) }
                var isAuthenticated by remember { mutableStateOf(false) }
                var hasSeenOnboarding by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(2000)
                    isInitializing = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isInitializing) {
                        SplashScreen(modifier = Modifier.padding(innerPadding))
                    } else {
                        AppNavigation(
                            isAuthenticated = isAuthenticated,
                            hasSeenOnboarding = hasSeenOnboarding,
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
}