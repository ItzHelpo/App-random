package com.azar.decide.feature.coin

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azar.decide.R
import com.azar.decide.ui.components.GenerateButton
import com.azar.decide.ui.components.ToolContent
import com.azar.decide.ui.components.ToolScaffold
import kotlin.random.Random

@Composable
fun CoinScreen(onBack: () -> Unit, onAction: () -> Unit) {
    // null = not flipped yet, true = heads, false = tails
    var isHeads by remember { mutableStateOf<Boolean?>(null) }
    var flips by remember { mutableIntStateOf(0) }

    // Each flip adds half-turns so the coin keeps spinning in the same direction.
    val rotation by animateFloatAsState(
        targetValue = flips * 540f,
        animationSpec = tween(durationMillis = 600),
        label = "coinRotation"
    )

    ToolScaffold(title = stringResource(R.string.tool_coin), onBack = onBack) { padding ->
        ToolContent(padding) {
            Surface(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .graphicsLayer { rotationY = rotation },
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = when (isHeads) {
                            true -> stringResource(R.string.coin_heads)
                            false -> stringResource(R.string.coin_tails)
                            null -> "?"
                        },
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = stringResource(R.string.tap_to_start),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            GenerateButton(text = stringResource(R.string.action_flip)) {
                isHeads = Random.nextBoolean()
                flips++
                onAction()
            }
        }
    }
}
