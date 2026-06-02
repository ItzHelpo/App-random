package com.repon.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class ThemeMode { SYSTEM, LIGHT, DARK }

private val LightColors = lightColorScheme(
    primary = tealPrimary,
    onPrimary = tealOnPrimary,
    primaryContainer = tealPrimaryContainer,
    onPrimaryContainer = tealOnPrimaryContainer,
    secondary = lightSecondary,
    secondaryContainer = lightSecondaryContainer,
    background = lightBackground,
    surface = lightSurface,
    surfaceVariant = lightSurfaceVariant,
    onSurface = lightOnSurface,
    onSurfaceVariant = lightOnSurfaceVariant,
    outline = lightOutline
)

private val DarkColors = darkColorScheme(
    primary = tealPrimaryDark,
    onPrimary = tealOnPrimaryDark,
    primaryContainer = tealPrimaryContainerDark,
    onPrimaryContainer = tealOnPrimaryContainerDark,
    secondary = darkSecondary,
    secondaryContainer = darkSecondaryContainer,
    background = darkBackground,
    surface = darkSurface,
    surfaceVariant = darkSurfaceVariant,
    onSurface = darkOnSurface,
    onSurfaceVariant = darkOnSurfaceVariant,
    outline = darkOutline
)

@Composable
fun ReponTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
