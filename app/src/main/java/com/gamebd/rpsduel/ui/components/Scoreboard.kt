package com.gamebd.rpsduel.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamebd.rpsduel.ui.theme.AccentGold
import com.gamebd.rpsduel.ui.theme.ArcadeCard
import com.gamebd.rpsduel.ui.theme.Player1Cyan
import com.gamebd.rpsduel.ui.theme.Player2Pink
import com.gamebd.rpsduel.ui.theme.TextMuted
import com.gamebd.rpsduel.ui.theme.TextWhite

@Composable
fun Scoreboard(
    p1Name: String,
    p2Name: String,
    p1Score: Int,
    p2Score: Int,
    currentRound: Int,
    totalRounds: Int,
    soundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val requiredWins = (totalRounds / 2) + 1

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(ArcadeCard.copy(alpha = 0.85f))
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(Player1Cyan.copy(alpha = 0.6f), Player2Pink.copy(alpha = 0.6f))
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Player 1 Column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = p1Name,
                    color = Player1Cyan,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                AnimatedScore(
                    score = p1Score,
                    color = Player1Cyan
                )
            }

            // Center Match Status
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "VS",
                    color = AccentGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Round $currentRound",
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Best of $totalRounds (First to $requiredWins)",
                    color = TextMuted,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Player 2 Column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = p2Name,
                    color = Player2Pink,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                AnimatedScore(
                    score = p2Score,
                    color = Player2Pink
                )
            }

            // Quick Actions (Sound + Settings)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onToggleSound,
                    modifier = Modifier.semantics {
                        contentDescription = if (soundEnabled) "Mute sound" else "Unmute sound"
                    }
                ) {
                    Icon(
                        imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = null,
                        tint = if (soundEnabled) AccentGold else TextMuted
                    )
                }
                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.semantics {
                        contentDescription = "Open Settings"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = TextWhite
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedScore(
    score: Int,
    color: Color
) {
    AnimatedContent(
        targetState = score,
        transitionSpec = {
            slideInVertically { height -> height } togetherWith slideOutVertically { height -> -height }
        },
        label = "score_anim"
    ) { targetScore ->
        Text(
            text = "$targetScore",
            color = color,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
