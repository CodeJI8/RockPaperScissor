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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamebd.rpsduel.model.Move
import com.gamebd.rpsduel.ui.components.ArcadeBackground
import com.gamebd.rpsduel.ui.components.ArcadeButton
import com.gamebd.rpsduel.ui.components.MoveIcon
import com.gamebd.rpsduel.ui.theme.AccentGold
import com.gamebd.rpsduel.ui.theme.ArcadeCard
import com.gamebd.rpsduel.ui.theme.Player1Cyan
import com.gamebd.rpsduel.ui.theme.Player2Pink
import com.gamebd.rpsduel.ui.theme.TextWhite

@Composable
fun HowToPlayScreen(
    onBackClick: () -> Unit,
    hapticsEnabled: Boolean,
    modifier: Modifier = Modifier
) {
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(ArcadeCard)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back",
                        tint = Player1Cyan
                    )
                }

                Text(
                    text = "HOW TO PLAY",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentGold,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Rules Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Rule 1: Move Logic
                RuleCard(
                    title = "1. MOVES & BEATS LOGIC",
                    description = "Choose your move strategically:"
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        RuleRow(move = Move.ROCK, beatsText = "beats SCISSORS ✌️", color = Player1Cyan)
                        RuleRow(move = Move.SCISSORS, beatsText = "beats PAPER ✋", color = Player2Pink)
                        RuleRow(move = Move.PAPER, beatsText = "beats ROCK ✊", color = AccentGold)
                    }
                }

                // Rule 2: Pass & Play
                RuleCard(
                    title = "2. PASS & PLAY MECHANIC",
                    description = "Player 1 picks a move secretly, then passes the device to Player 2 behind the cover screen. Player 2 then picks their move!"
                )

                // Rule 3: Win Conditions
                RuleCard(
                    title = "3. MATCH WINNER",
                    description = "Reach majority wins first (Best of 3 -> 2 wins, Best of 5 -> 3 wins, Best of 7 -> 4 wins). Draws do not award points and replay the round!"
                )
            }

            // Back Button
            ArcadeButton(
                text = "GOT IT!",
                onClick = onBackClick,
                accentColor = Player1Cyan,
                secondaryColor = Player2Pink,
                hapticsEnabled = hapticsEnabled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RuleCard(
    title: String,
    description: String,
    content: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ArcadeCard)
            .border(1.dp, Player1Cyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                color = AccentGold,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = TextWhite,
                fontSize = 14.sp
            )
            content?.invoke()
        }
    }
}

@Composable
private fun RuleRow(
    move: Move,
    beatsText: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MoveIcon(move = move, size = 32.dp, tint = color)
        Text(
            text = beatsText,
            color = TextWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
