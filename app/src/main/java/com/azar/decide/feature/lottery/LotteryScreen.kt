package com.azar.decide.feature.lottery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azar.decide.R
import com.azar.decide.ui.components.GenerateButton
import com.azar.decide.ui.components.ToolContent
import com.azar.decide.ui.components.ToolScaffold

@Composable
fun LotteryScreen(onBack: () -> Unit, onAction: () -> Unit) {
    var count by remember { mutableIntStateOf(6) }
    var max by remember { mutableIntStateOf(49) }
    var numbers by remember { mutableStateOf<List<Int>>(emptyList()) }

    // Keep count valid relative to max so we never request more uniques than exist.
    if (count > max) count = max

    ToolScaffold(title = stringResource(R.string.tool_lottery), onBack = onBack) { padding ->
        ToolContent(padding) {
            Stepper(
                label = stringResource(R.string.lottery_count),
                value = count,
                onDecrease = { if (count > 1) count-- },
                onIncrease = { if (count < max && count < 20) count++ }
            )
            Stepper(
                label = stringResource(R.string.lottery_max),
                value = max,
                onDecrease = { if (max > 2) max-- },
                onIncrease = { if (max < 999) max++ }
            )

            if (numbers.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    numbers.forEach { Ball(it) }
                }
            }

            GenerateButton(text = stringResource(R.string.action_generate)) {
                numbers = (1..max).shuffled().take(count).sorted()
                onAction()
            }
        }
    }
}

@Composable
private fun Stepper(label: String, value: Int, onDecrease: () -> Unit, onIncrease: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.titleLarge)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilledTonalIconButton(onClick = onDecrease) {
                Icon(Icons.Filled.Remove, contentDescription = "-")
            }
            Text(value.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            FilledTonalIconButton(onClick = onIncrease) {
                Icon(Icons.Filled.Add, contentDescription = "+")
            }
        }
    }
}

@Composable
private fun Ball(value: Int) {
    Surface(
        modifier = Modifier.size(52.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = value.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
