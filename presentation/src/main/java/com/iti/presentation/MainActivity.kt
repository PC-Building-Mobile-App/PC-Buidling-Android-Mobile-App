package com.iti.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.presentation.core.locale.LocaleContextWrapper
import com.iti.presentation.core.locale.LocaleEntryPoint
import com.iti.presentation.core.navigation.AppNavigation
import com.iti.presentation.splash.SplashScreen
import com.iti.presentation.ui.theme.AppTheme
import dagger.hilt.EntryPoints
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {

        val localeRepository = EntryPoints.get(
            newBase.applicationContext,
            LocaleEntryPoint::class.java,
        ).localeRepository()
        val languageTag = localeRepository.getLanguageTag()
        super.attachBaseContext(LocaleContextWrapper.wrap(newBase, languageTag))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val nativeSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        nativeSplash.setKeepOnScreenCondition { false }

        setContent {
            AppTheme {
                val state by mainViewModel.state.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { innerPadding ->
                    if (state.isLoading) {
                        SplashScreen(modifier = Modifier.padding(innerPadding))
                    } else {
                        AppNavigation(
                            isAuthenticated = state.isAuthenticated,
                            hasSeenOnboarding = state.hasSeenOnboarding,
                            onAuthComplete = {

                            },
                        )
                    }
                }
            }
        }
    }
}