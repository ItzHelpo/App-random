package com.repon.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.repon.app.R
import com.repon.app.ads.BannerAd
import com.repon.app.data.ConsumableItem
import com.repon.app.data.RestockCalculator
import com.repon.app.data.RestockStatus
import com.repon.app.data.SortMode
import com.repon.app.ui.components.colorFor
import com.repon.app.ui.components.daysLeftText
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddItem: () -> Unit,
    onOpenItem: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val sortMode by viewModel.sortMode.collectAsStateWithLifecycle()
    val today = LocalDate.now().toEpochDay()

    val ranked = items.map { it to RestockCalculator.statusFor(it, today) }
    val sorted = when (sortMode) {
        SortMode.URGENCY -> ranked.sortedBy { it.second.daysLeft }
        SortMode.NAME -> ranked.sortedBy { it.first.name.lowercase() }
        SortMode.RECENT -> ranked.sortedByDescending { it.first.createdEpochDay }
    }
    val soon = sorted.filter { it.second.status != RestockStatus.OK }
    val ok = sorted.filter { it.second.status == RestockStatus.OK }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name), fontWeight = FontWeight.Bold) },
                actions = {
                    SortMenu(current = sortMode, onSelect = { viewModel.setSortMode(it) })
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            androidx.compose.material3.ExtendedFloatingActionButton(
                onClick = onAddItem,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.add_item)) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        bottomBar = { BannerAd() }
    ) { padding ->
        if (items.isEmpty()) {
            EmptyState(padding)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp, 10.dp, 16.dp, 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (soon.isNotEmpty()) {
                    item(key = "h_soon") {
                        SectionHeader(stringResource(R.string.home_section_soon), soon.size, Modifier.animateItem())
                    }
                    items(soon, key = { it.first.id }) { (item, status) ->
                        ItemCard(
                            item, status.status, status.progress, daysLeftText(status),
                            onRestock = { viewModel.restock(item) },
                            onOpen = { onOpenItem(item.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
                if (ok.isNotEmpty()) {
                    item(key = "h_ok") {
                        SectionHeader(stringResource(R.string.home_section_ok), ok.size, Modifier.animateItem())
                    }
                    items(ok, key = { it.first.id }) { (item, status) ->
                        ItemCard(
                            item, status.status, status.progress, daysLeftText(status),
                            onRestock = { viewModel.restock(item) },
                            onOpen = { onOpenItem(item.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortMenu(current: SortMode, onSelect: (SortMode) -> Unit) {
    var open by remember { mutableStateOf(false) }
    IconButton(onClick = { open = true }) {
        Icon(Icons.Filled.SwapVert, contentDescription = stringResource(R.string.sort_label))
    }
    DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
        val options = listOf(
            SortMode.URGENCY to R.string.sort_urgency,
            SortMode.NAME to R.string.sort_name,
            SortMode.RECENT to R.string.sort_recent
        )
        options.forEach { (mode, label) ->
            DropdownMenuItem(
                text = { Text(stringResource(label)) },
                onClick = { onSelect(mode); open = false },
                leadingIcon = {
                    RadioButton(selected = current == mode, onClick = { onSelect(mode); open = false })
                }
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String, count: Int, modifier: Modifier = Modifier) {
    Text(
        text = "$text · $count",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(top = 6.dp, bottom = 2.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemCard(
    item: ConsumableItem,
    status: RestockStatus,
    progress: Float,
    daysText: String,
    onRestock: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = colorFor(status)
    Card(
        onClick = onOpen,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(46.dp),
                    shape = CircleShape,
                    color = accent.copy(alpha = 0.14f)
                ) {
                    Box(contentAlignment = Alignment.Center) { Text(item.emoji, fontSize = 22.sp) }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        daysText,
                        color = accent,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                FilledTonalButton(onClick = onRestock) {
                    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(
                        stringResource(R.string.action_bought),
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
            ProgressBar(
                progress = progress,
                color = accent,
                modifier = Modifier.padding(top = 14.dp)
            )
        }
    }
}

@Composable
private fun ProgressBar(progress: Float, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.16f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}

@Composable
private fun EmptyState(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(96.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(44.dp)
                )
            }
        }
        Text(
            stringResource(R.string.home_empty_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            stringResource(R.string.home_empty_body),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
