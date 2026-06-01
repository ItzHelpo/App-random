package com.azar.decide.feature.dice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
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
import kotlin.random.Random

private val sideOptions = listOf(4, 6, 8, 10, 12, 20)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiceScreen(onBack: () -> Unit, onAction: () -> Unit) {
    var count by remember { mutableIntStateOf(2) }
    var sides by remember { mutableIntStateOf(6) }
    var results by remember { mutableStateOf<List<Int>>(emptyList()) }

    ToolScaffold(title = stringResource(R.string.tool_dice), onBack = onBack) { padding ->
        ToolContent(padding) {
            // Dice count stepper
            Text(stringResource(R.string.dice_count), style = MaterialTheme.typography.titleLarge)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                FilledTonalIconButton(onClick = { if (count > 1) count-- }) {
                    Icon(Icons.Filled.Remove, contentDescription = "-")
                }
                Text(
                    text = count.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                FilledTonalIconButton(onClick = { if (count < 10) count++ }) {
                    Icon(Icons.Filled.Add, contentDescription = "+")
                }
            }

            // Sides selector
            Text(stringResource(R.string.dice_sides), style = MaterialTheme.typography.titleLarge)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                sideOptions.forEach { option ->
                    FilterChip(
                        selected = sides == option,
                        onClick = { sides = option },
                        label = { Text("d$option") }
                    )
                }
            }

            // Results
            if (results.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    results.forEach { value -> DieFace(value) }
                }
                Text(
                    text = stringResource(R.string.dice_total, results.sum()),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            GenerateButton(text = stringResource(R.string.action_roll)) {
                results = List(count) { Random.nextInt(1, sides + 1) }
                onAction()
            }
        }
    }
}

@Composable
private fun DieFace(value: Int) {
    Surface(
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
            Text(
                text = value.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
