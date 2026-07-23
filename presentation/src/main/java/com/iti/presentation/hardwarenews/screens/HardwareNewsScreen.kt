package com.iti.presentation.hardwarenews.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.presentation.R
import com.iti.presentation.core.uicomponents.ErrorScreen
import com.iti.presentation.core.uicomponents.NewsCard
import com.iti.presentation.core.uicomponents.shimmerEffect
import com.iti.presentation.hardwarenews.HardwareNewsContract.Effect
import com.iti.presentation.hardwarenews.HardwareNewsContract.Event
import com.iti.presentation.hardwarenews.HardwareNewsContract.State
import com.iti.presentation.hardwarenews.viewmodel.HardwareNewsViewModel
import com.iti.presentation.home.model.HardwareNewsUiModel
import com.iti.presentation.ui.theme.AppTheme
import kotlinx.coroutines.flow.collectLatest
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun HardwareNewsScreen(
    viewModel: HardwareNewsViewModel = hiltViewModel(),
    onArticleClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.NavigateToDetail -> onArticleClick(effect.articleId)
            }
        }
    }

    HardwareNewsScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HardwareNewsScreenContent(
    state: State,
    onEvent: (Event) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.hardware_news_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { padding ->
        when {
            state.errorMessage != null -> {
                ErrorScreen(
                    message = state.errorMessage.asString(),
                    modifier = Modifier.padding(padding)
                )
            }

            state.isLoading -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(padding)
                ) {
                    items(6) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .shimmerEffect()
                            )
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .height(12.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .shimmerEffect()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height(12.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .shimmerEffect()
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(state.articles, key = { it.id }) { article ->
                        NewsCard(
                            article = article,
                            onClick = { onEvent(Event.ArticleClicked(article.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun HardwareNewsScreenPreview() {
    AppTheme {
        HardwareNewsScreenContent(
            state = State(
                isLoading = false,
                articles = listOf(
                    HardwareNewsUiModel("1", "RTX 5090 rumored for Q1 2027", "NVIDIA", "", "", "","Jul 15, 2026"),
                    HardwareNewsUiModel("2", "AMD Zen 6 leaks show massive IPC gains", "AMD", "", "","", "Jul 14, 2026"),
                    HardwareNewsUiModel("3", "DDR6 RAM standard finalized by JEDEC", "Tech", "", "","", "Jul 13, 2026"),
                    HardwareNewsUiModel("4", "Intel Arrow Lake launch date confirmed", "Intel", "", "","", "Jul 12, 2026"),
                )
            ),
            onEvent = {},
            onBackClick = {}
        )
    }
}
