package com.iti.presentation.categorybuilds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.domain.builds.model.BuildCategoryType
import com.iti.presentation.R
import com.iti.presentation.mypcs.model.gradient
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.IconOnGradient

@Composable
fun CategoryBuildsFab(
    categoryType: BuildCategoryType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .shadow(elevation = 8.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(categoryType.gradient)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.new_build),
            tint = IconOnGradient,
        )
    }
}

@Preview
@Composable
private fun CategoryBuildsFabPreview() {
    AppTheme {
        CategoryBuildsFab(categoryType = BuildCategoryType.GAMING, onClick = {})
    }
}