package com.gamebd.rpsduel.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gamebd.rpsduel.ui.screens.GameScreen
import com.gamebd.rpsduel.ui.screens.HomeScreen
import com.gamebd.rpsduel.ui.screens.HowToPlayScreen
import com.gamebd.rpsduel.ui.screens.PlayerNameScreen
import com.gamebd.rpsduel.ui.screens.ResultScreen
import com.gamebd.rpsduel.viewmodel.GameViewModel
import com.gamebd.rpsduel.viewmodel.SettingsViewModel

object NavRoutes {
    const val HOME = "home"
    const val PLAYER_NAMES = "player_names/{totalRounds}"
    const val GAME = "game"
    const val RESULT = "result"
    const val HOW_TO_PLAY = "how_to_play"

    fun createPlayerNamesRoute(totalRounds: Int) = "player_names/$totalRounds"
}

@Composable
fun RPSDuelNavGraph(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val gameState by gameViewModel.state.collectAsState()
    val soundEnabled by settingsViewModel.soundEnabled.collectAsState()
    val hapticsEnabled by settingsViewModel.hapticsEnabled.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME,
        modifier = modifier,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onStartGameClick = { totalRounds ->
                    navController.navigate(NavRoutes.createPlayerNamesRoute(totalRounds))
                },
                onHowToPlayClick = {
                    navController.navigate(NavRoutes.HOW_TO_PLAY)
                },
                soundEnabled = soundEnabled,
                hapticsEnabled = hapticsEnabled,
                onSoundToggled = { settingsViewModel.setSoundEnabled(!soundEnabled) },
                onHapticsToggled = { settingsViewModel.setHapticsEnabled(it) }
            )
        }

        composable(
            route = NavRoutes.PLAYER_NAMES,
            arguments = listOf(navArgument("totalRounds") { type = NavType.IntType })
        ) { backStackEntry ->
            val totalRounds = backStackEntry.arguments?.getInt("totalRounds") ?: 3
            PlayerNameScreen(
                totalRounds = totalRounds,
                onStartMatchClick = { p1, p2 ->
                    gameViewModel.startGame(p1, p2, totalRounds)
                    navController.navigate(NavRoutes.GAME) {
                        popUpTo(NavRoutes.HOME)
                    }
                },
                hapticsEnabled = hapticsEnabled
            )
        }

        composable(NavRoutes.GAME) {
            GameScreen(
                state = gameState,
                onSelectP1Move = { gameViewModel.selectP1Move(it) },
                onReadyP2 = { gameViewModel.readyP2() },
                onSelectP2Move = { gameViewModel.selectP2Move(it) },
                onNextRound = { gameViewModel.nextRound() },
                onMatchOver = {
                    navController.navigate(NavRoutes.RESULT) {
                        popUpTo(NavRoutes.GAME) { inclusive = true }
                    }
                },
                soundEnabled = soundEnabled,
                hapticsEnabled = hapticsEnabled,
                onToggleSound = { settingsViewModel.setSoundEnabled(!soundEnabled) },
                onHapticsToggled = { settingsViewModel.setHapticsEnabled(it) }
            )
        }

        composable(NavRoutes.RESULT) {
            ResultScreen(
                state = gameState,
                onPlayAgainClick = {
                    gameViewModel.resetMatch()
                    navController.navigate(NavRoutes.GAME) {
                        popUpTo(NavRoutes.HOME)
                    }
                },
                onHomeClick = {
                    gameViewModel.resetToHome()
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                },
                hapticsEnabled = hapticsEnabled
            )
        }

        composable(NavRoutes.HOW_TO_PLAY) {
            HowToPlayScreen(
                onBackClick = { navController.popBackStack() },
                hapticsEnabled = hapticsEnabled
            )
        }
    }
}
