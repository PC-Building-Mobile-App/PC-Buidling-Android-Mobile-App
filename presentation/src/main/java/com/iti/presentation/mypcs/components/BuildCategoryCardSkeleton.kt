package com.iti.presentation.mypcs.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.presentation.core.uicomponents.shimmerEffect
import com.iti.presentation.ui.theme.AppTheme

@Composable
fun BuildCategoryCardSkeleton(modifier: Modifier = Modifier) {
    val shape = MaterialTheme.shapes.small

    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .height(168.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(11.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect(),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(11.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect(),
            )

            Spacer(modifier = Modifier.weight(1f))

            Row {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun BuildCategoryCardSkeletonPreview() {
    AppTheme {
        BuildCategoryCardSkeleton(modifier = Modifier.width(171.dp))
    }
}