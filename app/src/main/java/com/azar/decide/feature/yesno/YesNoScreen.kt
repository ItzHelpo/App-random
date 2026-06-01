package com.azar.decide.feature.yesno

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azar.decide.R
import com.azar.decide.ui.components.GenerateButton
import com.azar.decide.ui.components.ToolContent
import com.azar.decide.ui.components.ToolScaffold
import com.azar.decide.ui.theme.AccentGreen
import com.azar.decide.ui.theme.AccentRed
import kotlin.random.Random

@Composable
fun YesNoScreen(onBack: () -> Unit, onAction: () -> Unit) {
    var answer by remember { mutableStateOf<Boolean?>(null) }

    val targetColor = when (answer) {
        true -> AccentGreen
        false -> AccentRed
        null -> MaterialTheme.colorScheme.surfaceVariant
    }
    val circleColor by animateColorAsState(targetColor, label = "yesNoColor")

    ToolScaffold(title = stringResource(R.string.tool_yesno), onBack = onBack) { padding ->
        ToolContent(padding) {
            Surface(
                modifier = Modifier.size(220.dp),
                shape = CircleShape,
                color = circleColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = when (answer) {
                            true -> stringResource(R.string.yesno_yes)
                            false -> stringResource(R.string.yesno_no)
                            null -> "?"
                        },
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (answer == null) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                    )
                }
            }

            GenerateButton(text = stringResource(R.string.action_generate)) {
                answer = Random.nextBoolean()
                onAction()
            }
        }
    }
}
