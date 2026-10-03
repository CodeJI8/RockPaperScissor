package com.gamebd.rpsduel.ui.screens

import android.app.Activity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamebd.rpsduel.model.GamePhase
import com.gamebd.rpsduel.model.GameState
import com.gamebd.rpsduel.model.Move
import com.gamebd.rpsduel.ui.components.ArcadeBackground
import com.gamebd.rpsduel.ui.components.ArcadeButton
import com.gamebd.rpsduel.ui.components.MoveCard
import com.gamebd.rpsduel.ui.components.Scoreboard
import com.gamebd.rpsduel.ui.components.SettingsDialog
import com.gamebd.rpsduel.ui.theme.AccentGold
import com.gamebd.rpsduel.ui.theme.ArcadeCard
import com.gamebd.rpsduel.ui.theme.DrawGray
import com.gamebd.rpsduel.ui.theme.Player1Cyan
import com.gamebd.rpsduel.ui.theme.Player2Pink
import com.gamebd.rpsduel.ui.theme.TextMuted
import com.gamebd.rpsduel.ui.theme.TextWhite
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    state: GameState,
    onSelectP1Move: (Move) -> Unit,
    onReadyP2: () -> Unit,
    onSelectP2Move: (Move) -> Unit,
    onNextRound: () -> Unit,
    onMatchOver: () -> Unit,
    soundEnabled: Boolean,
    hapticsEnabled: Boolean,
    onToggleSound: () -> Unit,
    onHapticsToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Keep screen on during gameplay
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    ArcadeBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Scoreboard at Top
            Scoreboard(
                p1Name = state.p1Name,
                p2Name = state.p2Name,
                p1Score = state.p1Score,
                p2Score = state.p2Score,
                currentRound = state.currentRound,
                totalRounds = state.totalRounds,
                soundEnabled = soundEnabled,
                onToggleSound = onToggleSound,
                onOpenSettings = { showSettingsDialog = true }
            )

            // Dynamic Phase Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                when (state.phase) {
                    GamePhase.P1_TURN -> {
                        TurnSelectionView(
                            playerName = state.p1Name,
                            playerColor = Player1Cyan,
                            onMoveSelected = onSelectP1Move,
                            hapticsEnabled = hapticsEnabled
                        )
                    }

                    GamePhase.PASS_TO_P2 -> {
                        PassPhoneCoverView(
                            nextPlayerName = state.p2Name,
                            onReadyClick = onReadyP2,
                            hapticsEnabled = hapticsEnabled
                        )
                    }

                    GamePhase.P2_TURN -> {
                        TurnSelectionView(
                            playerName = state.p2Name,
                            playerColor = Player2Pink,
                            onMoveSelected = onSelectP2Move,
                            hapticsEnabled = hapticsEnabled
                        )
                    }

                    GamePhase.REVEAL -> {
                        RevealClashView(
                            state = state,
                            onNextRound = {
                                if (state.matchWinnerName != null) {
                                    onMatchOver()
                                } else {
                                    onNextRound()
                                }
                            },
                            hapticsEnabled = hapticsEnabled
                        )
                    }

                    GamePhase.MATCH_OVER -> {
                        LaunchedEffect(Unit) {
                            onMatchOver()
                        }
                    }
                }
            }
        }

        if (showSettingsDialog) {
            SettingsDialog(
                soundEnabled = soundEnabled,
                hapticsEnabled = hapticsEnabled,
                onSoundToggled = { onToggleSound() },
                onHapticsToggled = onHapticsToggled,
                onDismiss = { showSettingsDialog = false }
            )
        }
    }
}

@Composable
private fun TurnSelectionView(
    playerName: String,
    playerColor: Color,
    onMoveSelected: (Move) -> Unit,
    hapticsEnabled: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$playerName, YOUR TURN",
            color = playerColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Pick your move secretly",
            color = TextMuted,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Move.entries.forEach { move ->
                MoveCard(
                    move = move,
                    onSelect = { onMoveSelected(move) },
                    playerColor = playerColor,
                    hapticsEnabled = hapticsEnabled,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun PassPhoneCoverView(
    nextPlayerName: String,
    onReadyClick: () -> Unit,
    hapticsEnabled: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "phone_rotate")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(ArcadeCard)
            .border(2.dp, Player2Pink, RoundedCornerShape(32.dp))
            .padding(28.dp)
    ) {
        // Rotating/Swiping Phone Animation
        Box(
            modifier = Modifier
                .size(100.dp)
                .rotate(rotation)
                .clip(CircleShape)
                .background(Player2Pink.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Smartphone,
                contentDescription = "Pass phone icon",
                tint = Player2Pink,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "PASS THE PHONE",
            color = AccentGold,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Hand the device to $nextPlayerName and tap ready when set!",
            color = TextWhite,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 28.dp)
        )

        ArcadeButton(
            text = "I'M READY, $nextPlayerName!",
            onClick = onReadyClick,
            accentColor = Player2Pink,
            secondaryColor = Player1Cyan,
            hapticsEnabled = hapticsEnabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RevealClashView(
    state: GameState,
    onNextRound: () -> Unit,
    hapticsEnabled: Boolean
) {
    var countdownStep by remember { mutableIntStateOf(3) }
    var countdownText by remember { mutableStateOf("3") }
    var isCountdownFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // 3-2-1 SHOOT sequence
        delay(400)
        countdownText = "2"
        delay(500)
        countdownText = "1"
        delay(500)
        countdownText = "SHOOT!"
        delay(500)
        isCountdownFinished = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!isCountdownFinished) {
            // Countdown overlay animation
            Text(
                text = countdownText,
                fontSize = 54.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentGold,
                textAlign = TextAlign.Center
            )
        } else {
            // Reveal side-by-side moves
            val p1Wins = state.roundWinnerName == state.p1Name
            val p2Wins = state.roundWinnerName == state.p2Name

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // P1 Card (Left Slide)
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.p1Name,
                            color = Player1Cyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        state.p1Move?.let { move ->
                            MoveCard(
                                move = move,
                                onSelect = {},
                                isSelected = p1Wins,
                                playerColor = Player1Cyan,
                                enabled = false,
                                modifier = Modifier
                                    .graphicsLayer {
                                        if (p2Wins) alpha = 0.4f
                                    }
                            )
                        }
                    }
                }

                Text(
                    text = "VS",
                    color = AccentGold,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                // P2 Card (Right Slide)
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.p2Name,
                            color = Player2Pink,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        state.p2Move?.let { move ->
                            MoveCard(
                                move = move,
                                onSelect = {},
                                isSelected = p2Wins,
                                playerColor = Player2Pink,
                                enabled = false,
                                modifier = Modifier
                                    .graphicsLayer {
                                        if (p1Wins) alpha = 0.4f
                                    }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Result Banner
            val resultMessage = when {
                state.isDraw -> "DRAW! Replay Round"
                state.roundWinnerName != null -> "🏆 ${state.roundWinnerName} Wins Round!"
                else -> ""
            }
            val resultColor = when {
                state.isDraw -> DrawGray
                else -> AccentGold
            }

            Text(
                text = resultMessage,
                color = resultColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            ArcadeButton(
                text = if (state.matchWinnerName != null) "SEE RESULTS" else "NEXT ROUND",
                onClick = onNextRound,
                accentColor = AccentGold,
                secondaryColor = Player1Cyan,
                hapticsEnabled = hapticsEnabled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
