package com.gamebd.rpsduel.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamebd.rpsduel.model.Move
import com.gamebd.rpsduel.ui.components.ArcadeBackground
import com.gamebd.rpsduel.ui.components.ArcadeButton
import com.gamebd.rpsduel.ui.components.MoveIcon
import com.gamebd.rpsduel.ui.components.SettingsDialog
import com.gamebd.rpsduel.ui.theme.AccentGold
import com.gamebd.rpsduel.ui.theme.ArcadeCard
import com.gamebd.rpsduel.ui.theme.Player1Cyan
import com.gamebd.rpsduel.ui.theme.Player2Pink
import com.gamebd.rpsduel.ui.theme.TextMuted
import com.gamebd.rpsduel.ui.theme.TextWhite

@Composable
fun HomeScreen(
    onStartGameClick: (totalRounds: Int) -> Unit,
    onHowToPlayClick: () -> Unit,
    soundEnabled: Boolean,
    hapticsEnabled: Boolean,
    onSoundToggled: (Boolean) -> Unit,
    onHapticsToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRounds by remember { mutableIntStateOf(3) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    ArcadeBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { showSettingsDialog = true },
                    modifier = Modifier
                        .semantics { contentDescription = "Open Settings" }
                        .clip(CircleShape)
                        .background(ArcadeCard)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Player1Cyan
                    )
                }
            }

            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated Move Icons Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MoveIcon(move = Move.ROCK, size = 52.dp, tint = Player1Cyan)
                    MoveIcon(move = Move.PAPER, size = 52.dp, tint = AccentGold)
                    MoveIcon(move = Move.SCISSORS, size = 52.dp, tint = Player2Pink)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "RPS DUEL",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentGold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Pass & Play 2-Player Battle",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Round Selector & Actions
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "MATCH FORMAT",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Round Selector Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(ArcadeCard)
                        .border(
                            width = 1.5.dp,
                            brush = Brush.horizontalGradient(listOf(Player1Cyan, Player2Pink)),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(3, 5, 7).forEach { rounds ->
                        val isSelected = selectedRounds == rounds
                        val scale by animateFloatAsState(
                            targetValue = if (isSelected) 1.05f else 1.0f,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                            label = "pill_scale"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .scale(scale)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) {
                                        Brush.horizontalGradient(listOf(Player1Cyan, Player2Pink))
                                    } else {
                                        Brush.horizontalGradient(listOf(ArcadeCard, ArcadeCard))
                                    }
                                )
                                .clickable { selectedRounds = rounds }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Best of $rounds",
                                color = if (isSelected) TextWhite else TextMuted,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Start Game Button
                ArcadeButton(
                    text = "START GAME",
                    onClick = { onStartGameClick(selectedRounds) },
                    accentColor = Player1Cyan,
                    secondaryColor = Player2Pink,
                    hapticsEnabled = hapticsEnabled,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // How To Play Button
                ArcadeButton(
                    text = "HOW TO PLAY",
                    onClick = onHowToPlayClick,
                    accentColor = TextMuted,
                    secondaryColor = ArcadeCard,
                    hapticsEnabled = hapticsEnabled,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Footer info
            Text(
                text = "One Device • Turn-based Pass & Play",
                color = TextMuted.copy(alpha = 0.6f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        if (showSettingsDialog) {
            SettingsDialog(
                soundEnabled = soundEnabled,
                hapticsEnabled = hapticsEnabled,
                onSoundToggled = onSoundToggled,
                onHapticsToggled = onHapticsToggled,
                onDismiss = { showSettingsDialog = false }
            )
        }
    }
}
