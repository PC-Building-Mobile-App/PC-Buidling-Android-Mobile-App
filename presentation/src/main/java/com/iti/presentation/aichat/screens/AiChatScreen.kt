package com.iti.presentation.aichat.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.presentation.R
import com.iti.presentation.aichat.AiChatContract.Effect
import com.iti.presentation.aichat.AiChatContract.Event
import com.iti.presentation.aichat.AiChatContract.State
import com.iti.presentation.aichat.components.ActionCard
import com.iti.presentation.aichat.components.AiChatBubble
import com.iti.presentation.aichat.components.AiDashboardHeader
import com.iti.presentation.aichat.components.ChatInput
import com.iti.presentation.aichat.components.UserChatBubble
import com.iti.presentation.aichat.viewmodel.AiChatViewModel
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.ui.theme.WarningOrange
import kotlinx.coroutines.flow.collectLatest
import androidx.core.content.FileProvider
import com.iti.presentation.aichat.utils.AiChatPdfUtil

@Composable
fun AiChatScreen(
    viewModel: AiChatViewModel = hiltViewModel(),
    onNavigateToBuild: () -> Unit,
    onNavigateToCompare: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.NavigateToBuild -> onNavigateToBuild()
                is Effect.NavigateToCompare -> onNavigateToCompare()
                is Effect.ShareMessage -> {
                    val file = AiChatPdfUtil.generateAiChatPdf(context, effect.message)
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                    )
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share AI Response PDF"))
                }
            }
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it.asString(context))
            viewModel.onEvent(Event.DismissError)
        }
    }

    AiChatContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        onNavigateToProductDetail = onNavigateToProductDetail,
        onBackClick = onBackClick,
    )
}

@Composable
private fun AiChatContent(
    state: State,
    snackbarHostState: SnackbarHostState,
    onEvent: (Event) -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            ChatInput(
                value = state.inputText,
                onValueChange = { onEvent(Event.UpdateInput(it)) },
                onSend = { onEvent(Event.SendMessage) },
                isLoading = state.isLoading,
                modifier = Modifier.imePadding(),
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            AiDashboardHeader(onBackClick = onBackClick)

            AnimatedVisibility(
                visible = !state.hasStartedChat,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ActionCard(
                        title = "AI Build Generator",
                        subtitle = "Tell me your budget & use case",
                        iconRes = R.drawable.ic_nav_ai,
                        gradientColors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary),
                        onClick = { onEvent(Event.NavigateToBuild) },
                    )

                    ActionCard(
                        title = "Compare Builds",
                        subtitle = "Find the best value build",
                        iconRes = R.drawable.ic_nav_parts,
                        gradientColors = listOf(WarningOrange, WarningOrange.copy(red = 0.9f)),
                        onClick = { onEvent(Event.NavigateToCompare) },
                    )
                }
            }

            if (state.hasStartedChat) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        vertical = 8.dp,
                    ),
                ) {
                    items(
                        items = state.messages,
                        key = { it.id },
                    ) { message ->
                        if (message.isFromUser) {
                            UserChatBubble(message = message)
                        } else {
                            AiChatBubble(
                                message = message,
                                onExport = {
                                    onEvent(Event.ExportMessage(message))
                                },
                                onProductClick = { product ->
                                    try {
                                        val json = kotlinx.serialization.json.Json.encodeToString(
                                            ComponentUiModel.serializer(),
                                            product,
                                        )
                                        onNavigateToProductDetail(json)
                                    } catch (_: Exception) { }
                                },
                            )
                        }
                    }

                    if (state.isLoading) {
                        item {
                            TypingIndicator(
                                modifier = Modifier.padding(
                                    start = 58.dp,
                                    top = 8.dp,
                                    bottom = 8.dp,
                                ),
                            )
                        }
                    }
                }
            } else {
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun TypingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            text = "AI is thinking…",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
    }
}

