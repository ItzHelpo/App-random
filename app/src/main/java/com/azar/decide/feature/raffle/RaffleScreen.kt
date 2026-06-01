package com.azar.decide.feature.raffle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.azar.decide.R
import com.azar.decide.ui.components.GenerateButton
import com.azar.decide.ui.components.ToolContent
import com.azar.decide.ui.components.ToolScaffold

@Composable
fun RaffleScreen(onBack: () -> Unit, onAction: () -> Unit) {
    var namesText by remember { mutableStateOf("") }
    var winner by remember { mutableStateOf<String?>(null) }
    var removeWinner by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }

    ToolScaffold(title = stringResource(R.string.tool_raffle), onBack = onBack) { padding ->
        ToolContent(padding) {
            OutlinedTextField(
                value = namesText,
                onValueChange = { namesText = it },
                label = { Text(stringResource(R.string.raffle_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(checked = removeWinner, onCheckedChange = { removeWinner = it })
                Text(stringResource(R.string.raffle_remove_winner))
            }

            if (error) {
                Text(
                    text = stringResource(R.string.raffle_need_names),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            winner?.let { name ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EmojiEvents,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            GenerateButton(text = stringResource(R.string.action_pick)) {
                val names = namesText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
                if (names.size < 2) {
                    error = true
                } else {
                    error = false
                    val chosen = names.random()
                    winner = chosen
                    if (removeWinner) {
                        namesText = names.filter { it != chosen }.joinToString("\n")
                    }
                    onAction()
                }
            }
        }
    }
}
