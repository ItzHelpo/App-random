package com.azar.decide.feature.password

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azar.decide.R
import com.azar.decide.ui.components.GenerateButton
import com.azar.decide.ui.components.ToolContent
import com.azar.decide.ui.components.ToolScaffold
import java.security.SecureRandom

private const val UPPER = "ABCDEFGHJKLMNPQRSTUVWXYZ"
private const val LOWER = "abcdefghijkmnopqrstuvwxyz"
private const val DIGITS = "23456789"
private const val SYMBOLS = "!@#$%&*?-_+="

private val secureRandom = SecureRandom()

@Composable
fun PasswordScreen(onBack: () -> Unit, onAction: () -> Unit) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    var length by remember { mutableFloatStateOf(16f) }
    var useUpper by remember { mutableStateOf(true) }
    var useLower by remember { mutableStateOf(true) }
    var useNumbers by remember { mutableStateOf(true) }
    var useSymbols by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    ToolScaffold(title = stringResource(R.string.tool_password), onBack = onBack) { padding ->
        ToolContent(padding) {
            if (password.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = password,
                            modifier = Modifier.weight(1f),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        IconButton(onClick = {
                            clipboard.setText(AnnotatedString(password))
                            Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = stringResource(R.string.action_copy))
                        }
                    }
                }
            }

            Text(
                text = stringResource(R.string.password_length, length.toInt()),
                style = MaterialTheme.typography.titleLarge
            )
            Slider(
                value = length,
                onValueChange = { length = it },
                valueRange = 6f..32f,
                steps = 25
            )

            ToggleRow(stringResource(R.string.password_uppercase), useUpper) { useUpper = it }
            ToggleRow(stringResource(R.string.password_lowercase), useLower) { useLower = it }
            ToggleRow(stringResource(R.string.password_numbers), useNumbers) { useNumbers = it }
            ToggleRow(stringResource(R.string.password_symbols), useSymbols) { useSymbols = it }

            if (error) {
                Text(
                    text = stringResource(R.string.password_need_option),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            GenerateButton(text = stringResource(R.string.action_generate)) {
                val pool = buildString {
                    if (useUpper) append(UPPER)
                    if (useLower) append(LOWER)
                    if (useNumbers) append(DIGITS)
                    if (useSymbols) append(SYMBOLS)
                }
                if (pool.isEmpty()) {
                    error = true
                } else {
                    error = false
                    password = (1..length.toInt())
                        .map { pool[secureRandom.nextInt(pool.length)] }
                        .joinToString("")
                    onAction()
                }
            }
        }
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
