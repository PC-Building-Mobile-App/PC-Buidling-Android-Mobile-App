package com.iti.presentation.parts.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.iti.presentation.R
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSearchSheet(
    initialMinPrice: Double?,
    initialMaxPrice: Double?,
    onApply: (Double?, Double?) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    val maxLimit = 100000f
    
    var priceRange by remember {
        mutableStateOf((initialMinPrice?.toFloat() ?: 0f)..(initialMaxPrice?.toFloat() ?: maxLimit)) 
    }

    var minInput by remember { mutableStateOf(priceRange.start.toInt().toString()) }
    var maxInput by remember { mutableStateOf(priceRange.endInclusive.toInt().toString()) }

    val minVal = minInput.toIntOrNull()
    val maxVal = maxInput.toIntOrNull()

    val isMinValid = minVal != null && minVal <= maxLimit
    val isMaxValid = maxVal != null && maxVal <= maxLimit
    val isRangeValid = minVal != null && maxVal != null && isMinValid && isMaxValid && (minVal <= maxVal)

    val numberFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = stringResource(R.string.price_filter),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(R.string.price_range),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(
                        R.string.price_range_format,
                        numberFormat.format(priceRange.start.toInt()),
                        numberFormat.format(priceRange.endInclusive.toInt()),
                        stringResource(R.string.egp_suffix)
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RangeSlider(
                value = if (priceRange.start <= priceRange.endInclusive) priceRange else priceRange.endInclusive..priceRange.start,
                onValueChange = { 
                    priceRange = it
                    minInput = it.start.toInt().toString()
                    maxInput = it.endInclusive.toInt().toString()
                },
                valueRange = 0f..maxLimit,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = minInput,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            minInput = input
                            input.toIntOrNull()?.let { valInt ->
                                if (valInt <= maxLimit) {
                                    priceRange = valInt.toFloat()..priceRange.endInclusive
                                }
                            }
                        }
                    },
                    label = { Text(stringResource(R.string.min_price)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = !isMinValid || (isRangeValid.not() && isMaxValid),
                    singleLine = true,
                    shape = MaterialTheme.shapes.extraSmall
                )

                OutlinedTextField(
                    value = maxInput,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            maxInput = input
                            input.toIntOrNull()?.let { valInt ->
                                if (valInt <= maxLimit) {
                                    priceRange = priceRange.start..valInt.toFloat()
                                }
                            }
                        }
                    },
                    label = { Text(stringResource(R.string.max_price)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = !isMaxValid || (isRangeValid.not() && isMinValid),
                    singleLine = true,
                    shape = MaterialTheme.shapes.extraSmall
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onReset()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = stringResource(R.string.reset),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Button(
                    onClick = {
                        onApply(priceRange.start.toDouble(), priceRange.endInclusive.toDouble())
                    },
                    enabled = isRangeValid,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.apply_filters),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
