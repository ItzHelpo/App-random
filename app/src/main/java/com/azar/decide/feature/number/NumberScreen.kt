package com.azar.decide.feature.number

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azar.decide.R
import com.azar.decide.ui.components.GenerateButton
import com.azar.decide.ui.components.ToolContent
import com.azar.decide.ui.components.ToolScaffold
import kotlin.random.Random

@Composable
fun NumberScreen(onBack: () -> Unit, onAction: () -> Unit) {
    var minText by remember { mutableStateOf("1") }
    var maxText by remember { mutableStateOf("100") }
    var result by remember { mutableStateOf<Int?>(null) }
    var error by remember { mutableStateOf(false) }

    ToolScaffold(title = stringResource(R.string.tool_number), onBack = onBack) { padding ->
        ToolContent(padding) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = minText,
                    onValueChange = { minText = it.filterIndexed { i, c -> c.isDigit() || (i == 0 && c == '-') } },
                    label = { Text(stringResource(R.string.number_min)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = maxText,
                    onValueChange = { maxText = it.filterIndexed { i, c -> c.isDigit() || (i == 0 && c == '-') } },
                    label = { Text(stringResource(R.string.number_max)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            if (error) {
                Text(
                    text = stringResource(R.string.number_invalid_range),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = result?.toString() ?: "—",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            GenerateButton(text = stringResource(R.string.action_generate)) {
                val min = minText.toIntOrNull()
                val max = maxText.toIntOrNull()
                if (min == null || max == null || max <= min) {
                    error = true
                } else {
                    error = false
                    result = Random.nextInt(min, max + 1)
                    onAction()
                }
            }
        }
    }
}
