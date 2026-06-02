package com.repon.app.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.repon.app.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private val presetEmojis = listOf(
    "📦", "☕", "🪥", "🧻", "🧴", "🧼", "🐶", "🐱", "💊", "🥛", "🍞", "🧂",
    "🧺", "🪒", "🔋", "💄", "🧽", "🚬", "🌿", "👶", "🩹", "🧷"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    itemId: Long?,
    onBack: () -> Unit,
    viewModel: AddEditViewModel = viewModel()
) {
    LaunchedEffect(itemId) { viewModel.load(itemId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (viewModel.isExisting) R.string.edit_item_title else R.string.new_item_title
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    if (viewModel.isExisting) {
                        IconButton(onClick = { viewModel.delete(onBack) }) {
                            Icon(
                                Icons.Filled.DeleteOutline,
                                contentDescription = stringResource(R.string.action_delete)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Emoji picker
            Text(stringResource(R.string.field_emoji), style = MaterialTheme.typography.titleMedium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presetEmojis) { e ->
                    val selected = viewModel.emoji == e
                    Surface(
                        modifier = Modifier
                            .size(48.dp)
                            .selectable(selected = selected, onClick = { viewModel.emoji = e }),
                        shape = CircleShape,
                        color = if (selected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text(e, fontSize = 22.sp) }
                    }
                }
            }

            // Name
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text(stringResource(R.string.field_name)) },
                placeholder = { Text(stringResource(R.string.field_name_hint)) },
                singleLine = true,
                isError = viewModel.nameError,
                supportingText = if (viewModel.nameError) {
                    { Text(stringResource(R.string.error_name_required)) }
                } else null,
                modifier = Modifier.fillMaxWidth()
            )

            // Duration
            OutlinedTextField(
                value = viewModel.durationText,
                onValueChange = { v -> viewModel.durationText = v.filter { it.isDigit() }.take(4) },
                label = { Text(stringResource(R.string.field_duration)) },
                supportingText = {
                    Text(
                        if (viewModel.durationError) stringResource(R.string.error_duration_required)
                        else if (viewModel.restockCount > 0)
                            stringResource(R.string.learned_from, viewModel.restockCount)
                        else stringResource(R.string.field_duration_help)
                    )
                },
                isError = viewModel.durationError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Buffer
            Text(
                stringResource(R.string.field_buffer) + ": ${viewModel.bufferDays}",
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = viewModel.bufferDays.toFloat(),
                onValueChange = { viewModel.bufferDays = it.toInt() },
                valueRange = 0f..14f,
                steps = 13
            )

            // Start date
            StartDateRow(
                epochDay = viewModel.startEpochDay,
                onChange = { viewModel.startEpochDay = it }
            )

            // Notify
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.field_notify),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(checked = viewModel.notify, onCheckedChange = { viewModel.notify = it })
            }

            Button(
                onClick = { viewModel.save(onBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(stringResource(R.string.action_save), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun StartDateRow(epochDay: Long, onChange: (Long) -> Unit) {
    val date = LocalDate.ofEpochDay(epochDay)
    val today = LocalDate.now().toEpochDay()
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())

    Column {
        Text(stringResource(R.string.field_started), style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { onChange(epochDay - 1) }) { Text("−1") }
            Text(
                date.format(formatter),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            TextButton(
                onClick = { onChange((epochDay + 1).coerceAtMost(today)) },
                enabled = epochDay < today
            ) { Text("+1") }
        }
        TextButton(onClick = { onChange(today) }) { Text(stringResource(R.string.action_today)) }
    }
}
