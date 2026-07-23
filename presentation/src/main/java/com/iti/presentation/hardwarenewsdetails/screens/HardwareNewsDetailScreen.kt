package com.iti.presentation.hardwarenewsdetails.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.iti.presentation.R
import com.iti.presentation.core.uicomponents.ErrorScreen
import com.iti.presentation.hardwarenewsdetails.HardwareNewsDetailContract.Effect
import com.iti.presentation.hardwarenewsdetails.HardwareNewsDetailContract.Event
import com.iti.presentation.hardwarenewsdetails.HardwareNewsDetailContract.State
import com.iti.presentation.hardwarenewsdetails.viewmodel.HardwareNewsDetailViewModel
import com.iti.presentation.home.model.HardwareNewsUiModel
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.ElectricBlue
import com.iti.presentation.ui.theme.PrimaryGradient
import com.iti.presentation.ui.theme.RoyalPurple
import com.iti.presentation.ui.theme.TextPrimary
import com.iti.presentation.ui.theme.TextSecondary
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HardwareNewsDetailScreen(
    articleId: String,
    viewModel: HardwareNewsDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(articleId) {
        viewModel.onEvent(Event.LoadArticle(articleId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.OpenUrl -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(effect.url))
                    context.startActivity(intent)
                }
            }
        }
    }

    HardwareNewsDetailContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
private fun HardwareNewsDetailContent(
    state: State,
    onEvent: (Event) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                state.errorMessage != null -> {
                    ErrorScreen(
                        message = state.errorMessage.asString(),
                        modifier = Modifier.padding(padding)
                    )
                }

                state.article != null -> {
                    ArticleContent(
                        article = state.article,
                        onOpenInBrowser = { onEvent(Event.OpenInBrowser) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(top = padding.calculateTopPadding() + 8.dp, start = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(100)
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ArticleContent(
    article: HardwareNewsUiModel,
    onOpenInBrowser: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            if (article.imageUrl.isNotBlank()) {
                SubcomposeAsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(ElectricBlue.copy(alpha = 0.5f), RoyalPurple.copy(alpha = 0.5f))
                                    )
                                )
                        )
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(ElectricBlue.copy(alpha = 0.7f), RoyalPurple.copy(alpha = 0.7f))
                                    )
                                )
                        )
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                listOf(ElectricBlue.copy(alpha = 0.7f), RoyalPurple.copy(alpha = 0.7f))
                            )
                        )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                            startY = 150f
                        )
                    )
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(100)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = article.sourceBadge,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = article.publishedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            if (article.description.isNotBlank()) {
                Spacer(Modifier.height(24.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = article.description,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 26.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onOpenInBrowser,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = PrimaryGradient,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.read_full_article),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null,
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun HardwareNewsDetailPreview() {
    AppTheme {
        HardwareNewsDetailContent(
            state = State(
                isLoading = false,
                article = HardwareNewsUiModel(
                    id = "1",
                    title = "RTX 5090 rumored for Q1 2027 with massive performance gains",
                    description = "Recent leaks suggest the RTX 5090 will feature 32GB of GDDR7 memory and an unprecedented core count, potentially doubling the performance of the RTX 4090 in specific rasterization tasks. Nvidia remains silent on the actual release date, but partners are already preparing next-gen cooling solutions.",
                    sourceBadge = "NVIDIA",
                    imageUrl = "",
                    articleUrl = "https://example.com",
                    publishedDate = "Jul 15, 2026"
                )
            ),
            onEvent = {},
            onBackClick = {}
        )
    }
}
