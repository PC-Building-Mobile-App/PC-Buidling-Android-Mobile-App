package com.iti.presentation.core.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.iti.presentation.home.model.HardwareNewsUiModel
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.ElectricBlue
import com.iti.presentation.ui.theme.RoyalPurple
import com.iti.presentation.ui.theme.TextSecondary

@Composable
fun NewsCard(
    article: HardwareNewsUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
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
                                        listOf(ElectricBlue.copy(alpha = 0.6f), RoyalPurple.copy(alpha = 0.6f))
                                    )
                                )
                        )
                    },
                    error = {
                        GradientFallback()
                    }
                )
            } else {
                GradientFallback()
            }

            // Source badge
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(100)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = article.sourceBadge,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }

        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = article.publishedDate,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun GradientFallback() {
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

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun NewsCardPreview() {
    AppTheme {
        NewsCard(
            article = HardwareNewsUiModel(
                id = "1",
                title = "RTX 5090 rumored for Q1 2027",
                sourceBadge = "NVIDIA",
                imageUrl = "",
                articleUrl = "https://example.com",
                publishedDate = "Jul 15, 2026"
            ),
            onClick = {},
            modifier = Modifier.padding(20.dp)
        )
    }
}
