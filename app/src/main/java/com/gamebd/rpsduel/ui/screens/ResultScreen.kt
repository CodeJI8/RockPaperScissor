package com.gamebd.rpsduel.ui.screens

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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamebd.rpsduel.model.GameState
import com.gamebd.rpsduel.ui.components.ArcadeBackground
import com.gamebd.rpsduel.ui.components.ArcadeButton
import com.gamebd.rpsduel.ui.components.ConfettiCanvas
import com.gamebd.rpsduel.ui.theme.AccentGold
import com.gamebd.rpsduel.ui.theme.ArcadeCard
import com.gamebd.rpsduel.ui.theme.Player1Cyan
import com.gamebd.rpsduel.ui.theme.Player2Pink
import com.gamebd.rpsduel.ui.theme.TextMuted
import com.gamebd.rpsduel.ui.theme.TextWhite

@Composable
fun ResultScreen(
    state: GameState,
    onPlayAgainClick: () -> Unit,
    onHomeClick: () -> Unit,
    hapticsEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val winnerName = state.matchWinnerName ?: "NOBODY"
    val isP1Winner = winnerName == state.p1Name
    val winnerColor = if (isP1Winner) Player1Cyan else Player2Pink

    ArcadeBackground(modifier = modifier) {
        // Confetti Canvas Particle Overlay
        ConfettiCanvas(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Trophy
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(AccentGold.copy(alpha = 0.2f))
                        .border(3.dp, AccentGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy icon",
                        tint = AccentGold,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "MATCH CHAMPION",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGold,
                    letterSpacing = 2.sp
                )

                Text(
                    text = winnerName.uppercase(),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = winnerColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Score Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(ArcadeCard)
                    .border(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(listOf(Player1Cyan, Player2Pink)),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "FINAL SCORE",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = state.p1Name,
                                color = Player1Cyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${state.p1Score}",
                                color = TextWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 36.sp
                            )
                        }

                        Text(
                            text = "-",
                            color = AccentGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = state.p2Name,
                                color = Player2Pink,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${state.p2Score}",
                                color = TextWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 36.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Best of ${state.totalRounds} Format",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            // Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ArcadeButton(
                    text = "PLAY AGAIN",
                    onClick = onPlayAgainClick,
                    accentColor = Player1Cyan,
                    secondaryColor = Player2Pink,
                    hapticsEnabled = hapticsEnabled,
                    modifier = Modifier.fillMaxWidth()
                )

                ArcadeButton(
                    text = "MAIN MENU",
                    onClick = onHomeClick,
                    accentColor = TextMuted,
                    secondaryColor = ArcadeCard,
                    hapticsEnabled = hapticsEnabled,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
