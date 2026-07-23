package com.iti.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.presentation.R
import com.iti.presentation.core.uicomponents.NewsCard
import com.iti.presentation.core.uicomponents.shimmerEffect
import com.iti.presentation.home.model.HardwareNewsUiModel
import com.iti.presentation.ui.theme.AppTheme

@Composable
fun LatestNewsSection(
    news: List<HardwareNewsUiModel>,
    isLoading: Boolean,
    onSeeAll: () -> Unit,
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SectionHeader(
            title = stringResource(R.string.latest_hardware_news),
            actionText = stringResource(R.string.see_all),
            onActionClick = onSeeAll
        )

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) {
                    NewsCardSkeleton()
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(news, key = { it.id }) { article ->
                    NewsCard(
                        article = article,
                        onClick = { onNewsClick(article.id) },
                        modifier = Modifier.width(260.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NewsCardSkeleton() {
    Column(
        modifier = Modifier
            .width(260.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .shimmerEffect()
        )
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun LatestNewsSectionPreview() {
    AppTheme {
        LatestNewsSection(
            news = listOf(
                HardwareNewsUiModel("1", "RTX 5090 rumored for Q1 2027", "NVIDIA", "", "https://example.com", "","Jul 15, 2026"),
                HardwareNewsUiModel("2", "AMD Zen 6 leaks show massive IPC gains", "AMD", "", "https://example.com", "","Jul 14, 2026"),
            ),
            isLoading = false,
            onSeeAll = {},
            onNewsClick = {}
        )
    }
}
