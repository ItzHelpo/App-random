package com.repon.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.repon.app.data.SettingsStore
import com.repon.app.ui.edit.AddEditScreen
import com.repon.app.ui.home.HomeScreen
import com.repon.app.ui.settings.SettingsScreen
import com.repon.app.ui.theme.ReponTheme
import com.repon.app.ui.theme.ThemeMode
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settings = SettingsStore(applicationContext)

        setContent {
            val themeMode by settings.themeMode.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
            val remindersEnabled by settings.remindersEnabled.collectAsStateWithLifecycle(initialValue = true)

            // Ask for notification permission once on Android 13+.
            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { /* result ignored; reminders simply stay silent if denied */ }
            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val granted = ContextCompat.checkSelfPermission(
                        this@MainActivity, Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                    if (!granted) permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            ReponTheme(themeMode = themeMode, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                onAddItem = { navController.navigate("edit") },
                                onOpenItem = { id -> navController.navigate("edit?itemId=$id") },
                                onOpenSettings = { navController.navigate("settings") }
                            )
                        }
                        composable(
                            route = "edit?itemId={itemId}",
                            arguments = listOf(navArgument("itemId") {
                                type = NavType.LongType
                                defaultValue = -1L
                            })
                        ) { entry ->
                            val id = entry.arguments?.getLong("itemId") ?: -1L
                            AddEditScreen(
                                itemId = if (id <= 0L) null else id,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                themeMode = themeMode,
                                onThemeModeChange = { mode ->
                                    lifecycleScope.launch { settings.setThemeMode(mode) }
                                },
                                remindersEnabled = remindersEnabled,
                                onRemindersChange = { enabled ->
                                    lifecycleScope.launch { settings.setRemindersEnabled(enabled) }
                                },
                                versionName = BuildConfig.VERSION_NAME,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
