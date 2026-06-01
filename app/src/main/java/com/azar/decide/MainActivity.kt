package com.azar.decide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.azar.decide.data.SettingsStore
import com.azar.decide.ui.navigation.AppNavHost
import com.azar.decide.ui.theme.AzarTheme
import com.azar.decide.ui.theme.ThemeMode
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settings = SettingsStore(applicationContext)

        setContent {
            val themeMode by settings.themeMode.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
            val soundEnabled by settings.soundEnabled.collectAsStateWithLifecycle(initialValue = true)

            AzarTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(
                        activity = this,
                        themeMode = themeMode,
                        onThemeModeChange = { mode ->
                            lifecycleScope.launch { settings.setThemeMode(mode) }
                        },
                        soundEnabled = soundEnabled,
                        onSoundChange = { enabled ->
                            lifecycleScope.launch { settings.setSoundEnabled(enabled) }
                        },
                        versionName = BuildConfig.VERSION_NAME
                    )
                }
            }
        }
    }
}
