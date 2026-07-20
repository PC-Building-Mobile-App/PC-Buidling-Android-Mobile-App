package com.iti.presentation.auth.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import com.iti.presentation.auth.AuthContract
import com.iti.presentation.auth.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    AuthScreenContent(
        state = state,
        effectFlow = viewModel.effect,
        onEvent = viewModel::onEvent,
        onNavigateToHome = onNavigateToHome,
    )
}

@Composable
fun AuthScreenContent(
    state: AuthContract.State,
    effectFlow: Flow<AuthContract.Effect>,
    onEvent: (AuthContract.Event) -> Unit,
    onNavigateToHome: () -> Unit,
) {
    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is AuthContract.Effect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    val colorScheme = MaterialTheme.colorScheme
    val gradientBrush = Brush.linearGradient(
        colors = listOf(colorScheme.primary, colorScheme.secondary),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(horizontal = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
        ) {
            // Logo badge
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(gradientBrush),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Memory,
                    contentDescription = null,
                    tint = colorScheme.onPrimary,
                    modifier = Modifier.size(26.dp),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (state.isLoginMode) "Welcome back" else "Create account",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (state.isLoginMode) {
                    "Sign in to your BuildIQ account"
                } else {
                    "Create your BuildIQ account"
                },
                fontSize = 15.sp,
                color = colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (!state.isLoginMode) {
                FieldLabel("NAME")
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onEvent(AuthContract.Event.NameChanged(it)) },
                    placeholder = { Text("Jane Doe") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            FieldLabel("EMAIL")
            OutlinedTextField(
                value = state.email,
                onValueChange = { onEvent(AuthContract.Event.EmailChanged(it)) },
                placeholder = { Text("depo@example.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))

            FieldLabel("PASSWORD")
            OutlinedTextField(
                value = state.password,
                onValueChange = { onEvent(AuthContract.Event.PasswordChanged(it)) },
                placeholder = { Text("••••••••") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth(),
            )

            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = state.errorMessage,
                    color = colorScheme.error,
                    fontSize = 13.sp,
                )
            }

            if (state.isLoginMode) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Forgot password?",
                    color = colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(AuthContract.Event.Submit) },
                enabled = state.isSubmitEnabled,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Transparent),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (state.isSubmitEnabled) gradientBrush else Brush.linearGradient(listOf(colorScheme.surfaceVariant, colorScheme.surfaceVariant))),
                    contentAlignment = Alignment.Center,
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp),
                        )
                    } else {
                        Text(
                            text = if (state.isLoginMode) "Sign In" else "Create Account",
                            color = colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (state.isLoginMode) "Don't have an account? " else "Already have an account? ",
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                )
                TextButton(
                    onClick = { onEvent(AuthContract.Event.ToggleMode) },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    modifier = Modifier.height(20.dp),
                ) {
                    Text(
                        text = if (state.isLoginMode) "Create one" else "Sign in",
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
    focusedBorderColor = MaterialTheme.colorScheme.primary,
)