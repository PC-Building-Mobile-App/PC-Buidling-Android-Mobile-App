package com.iti.presentation.auth.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.presentation.R
import com.iti.presentation.auth.AuthContract
import com.iti.presentation.auth.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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
            .background(colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        colorScheme.primary.copy(alpha = 0.45f),
                                        colorScheme.secondary.copy(alpha = 0.20f),
                                        colorScheme.background.copy(alpha = 0f),
                                    ),
                                    center = center,
                                    radius = size.minDimension * 0.55f,
                                ),
                                radius = size.minDimension * 0.55f,
                                center = center,
                            )
                        },
                )

                Image(
                    painter = painterResource(R.drawable.pc_building_no_bg),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(160.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (state.isLoginMode) {
                        stringResource(R.string.welcome_back)
                    } else {
                        stringResource(R.string.create_account)
                    },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (state.isLoginMode) {
                        stringResource(R.string.sign_in_to_your_pc_building_account)
                    } else {
                        stringResource(R.string.build_your_dream_pc)
                    },
                    fontSize = 15.sp,
                    color = colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(28.dp))

                if (!state.isLoginMode) {
                    FieldLabel(stringResource(R.string.full_name))
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { onEvent(AuthContract.Event.NameChanged(it)) },
                        placeholder = { Text(stringResource(R.string.enter_your_full_name)) },
                        leadingIcon = {
                            Icon(Icons.Filled.Person, contentDescription = null)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .bringIntoViewOnFocus(),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                FieldLabel(stringResource(R.string.email))
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onEvent(AuthContract.Event.EmailChanged(it)) },
                    placeholder = { Text(stringResource(R.string.enter_your_email)) },
                    leadingIcon = {
                        Icon(Icons.Filled.Email, contentDescription = null)
                    },
                    singleLine = true,
                    isError = state.emailError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewOnFocus(),
                )
                if (state.emailError != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = state.emailError,
                        color = colorScheme.error,
                        fontSize = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                FieldLabel(stringResource(R.string.password))
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { onEvent(AuthContract.Event.PasswordChanged(it)) },
                    placeholder = { Text(stringResource(R.string.enter_your_password)) },
                    leadingIcon = {
                        Icon(Icons.Filled.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { onEvent(AuthContract.Event.TogglePasswordVisibility) }) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) {
                                    Icons.Filled.Visibility
                                } else {
                                    Icons.Filled.VisibilityOff
                                },
                                contentDescription = null,
                            )
                        }
                    },
                    singleLine = true,
                    isError = state.passwordError != null,
                    visualTransformation = if (state.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewOnFocus(),
                )
                if (state.passwordError != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = state.passwordError,
                        color = colorScheme.error,
                        fontSize = 12.sp,
                    )
                }

                if (!state.isLoginMode) {
                    Spacer(modifier = Modifier.height(20.dp))

                    FieldLabel(stringResource(R.string.confirm_password))
                    OutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = { onEvent(AuthContract.Event.ConfirmPasswordChanged(it)) },
                        placeholder = { Text(stringResource(R.string.re_enter_your_password)) },
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, contentDescription = null)
                        },
                        trailingIcon = {
                            IconButton(onClick = { onEvent(AuthContract.Event.ToggleConfirmPasswordVisibility) }) {
                                Icon(
                                    imageVector = if (state.isConfirmPasswordVisible) {
                                        Icons.Filled.Visibility
                                    } else {
                                        Icons.Filled.VisibilityOff
                                    },
                                    contentDescription = null,
                                )
                            }
                        },
                        singleLine = true,
                        isError = state.confirmPasswordError != null,
                        visualTransformation = if (state.isConfirmPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .bringIntoViewOnFocus(),
                    )
                    if (state.confirmPasswordError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = state.confirmPasswordError,
                            color = colorScheme.error,
                            fontSize = 12.sp,
                        )
                    }
                }

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
                        text = stringResource(R.string.forgot_password),
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (state.isSubmitEnabled) {
                                    gradientBrush
                                } else {
                                    Brush.linearGradient(listOf(colorScheme.surfaceVariant, colorScheme.surfaceVariant))
                                },
                            ),
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
                                text = if (state.isLoginMode) {
                                    stringResource(R.string.sign_in)
                                } else {
                                    stringResource(R.string.create_account)
                                },
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
                        text = if (state.isLoginMode) {
                            stringResource(R.string.don_t_have_an_account)
                        } else {
                            stringResource(R.string.already_have_an_account)
                        },
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                    )
                    TextButton(
                        onClick = { onEvent(AuthContract.Event.ToggleMode) },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(20.dp),
                    ) {
                        Text(
                            text = if (state.isLoginMode) {
                                stringResource(R.string.create_one)
                            } else {
                                stringResource(R.string.sign_in)
                            },
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        )
                    }
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
    unfocusedBorderColor = Color.Transparent,
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    errorBorderColor = MaterialTheme.colorScheme.error,
    errorContainerColor = MaterialTheme.colorScheme.surfaceContainer,
)

private fun Modifier.bringIntoViewOnFocus(): Modifier = composed {
    val requester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    this
        .bringIntoViewRequester(requester)
        .onFocusEvent { focusState ->
            if (focusState.isFocused) {
                coroutineScope.launch {
                    delay(300)
                    requester.bringIntoView()
                }
            }
        }
}