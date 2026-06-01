package com.azar.decide.ui.navigation

import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azar.decide.ads.InterstitialAdManager
import com.azar.decide.feature.coin.CoinScreen
import com.azar.decide.feature.dice.DiceScreen
import com.azar.decide.feature.home.HomeScreen
import com.azar.decide.feature.lottery.LotteryScreen
import com.azar.decide.feature.number.NumberScreen
import com.azar.decide.feature.password.PasswordScreen
import com.azar.decide.feature.raffle.RaffleScreen
import com.azar.decide.feature.settings.SettingsScreen
import com.azar.decide.feature.yesno.YesNoScreen
import com.azar.decide.ui.theme.ThemeMode

@Composable
fun AppNavHost(
    activity: Activity,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    soundEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    versionName: String
) {
    val navController = rememberNavController()

    // Records a generation and, when frequency caps allow, queues an interstitial.
    val onAction: () -> Unit = { InterstitialAdManager.recordAction() }

    // Leaving a tool is the natural, least-intrusive moment to show a full-screen ad.
    val onBackFromTool: () -> Unit = {
        navController.popBackStack()
        InterstitialAdManager.maybeShow(activity)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) }
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenTool = { route -> navController.navigate(route) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.COIN) { CoinScreen(onBack = onBackFromTool, onAction = onAction) }
        composable(Routes.DICE) { DiceScreen(onBack = onBackFromTool, onAction = onAction) }
        composable(Routes.NUMBER) { NumberScreen(onBack = onBackFromTool, onAction = onAction) }
        composable(Routes.YESNO) { YesNoScreen(onBack = onBackFromTool, onAction = onAction) }
        composable(Routes.RAFFLE) { RaffleScreen(onBack = onBackFromTool, onAction = onAction) }
        composable(Routes.PASSWORD) { PasswordScreen(onBack = onBackFromTool, onAction = onAction) }
        composable(Routes.LOTTERY) { LotteryScreen(onBack = onBackFromTool, onAction = onAction) }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
                soundEnabled = soundEnabled,
                onSoundChange = onSoundChange,
                versionName = versionName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
