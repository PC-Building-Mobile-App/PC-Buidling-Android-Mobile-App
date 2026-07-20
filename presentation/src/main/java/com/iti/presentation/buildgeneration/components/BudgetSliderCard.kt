package com.iti.presentation.buildgeneration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.iti.presentation.R
import com.iti.presentation.buildgeneration.BuildGenerationContract
import com.iti.presentation.ui.theme.AppTheme
import com.iti.presentation.ui.theme.PrimaryGradient
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BudgetSliderCard(
    budget: Float,
    onBudgetChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(PrimaryGradient)
            .padding(20.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(R.string.budget_label),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.75f),
            )
            Text(
                text = stringResource(R.string.budget_currency),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.75f),
            )
        }

        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = formatBudget(budget),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
            )
            Text(
                text = stringResource(R.string.budget_currency),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.75f),
                modifier = Modifier.padding(start = 6.dp, bottom = 4.dp),
            )
        }

        Slider(
            value = budget,
            onValueChange = onBudgetChanged,
            valueRange = BuildGenerationContract.MIN_BUDGET..BuildGenerationContract.MAX_BUDGET,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f),
            ),
            modifier = Modifier.padding(top = 8.dp),
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = formatBudget(BuildGenerationContract.MIN_BUDGET),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.75f),
            )
            Text(
                text = stringResource(R.string.budget_balanced),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.75f),
            )
            Text(
                text = formatBudget(BuildGenerationContract.MAX_BUDGET),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.75f),
            )
        }
    }
}

private fun formatBudget(value: Float): String = NumberFormat.getNumberInstance(Locale.US).format(value.toLong())

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun BudgetSliderCardPreview() {
    AppTheme {
        BudgetSliderCard(budget = 65_000f, onBudgetChanged = {})
    }
}