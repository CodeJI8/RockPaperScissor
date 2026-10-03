package com.gamebd.rpsduel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamebd.rpsduel.ui.components.ArcadeBackground
import com.gamebd.rpsduel.ui.components.ArcadeButton
import com.gamebd.rpsduel.ui.theme.AccentGold
import com.gamebd.rpsduel.ui.theme.ArcadeCard
import com.gamebd.rpsduel.ui.theme.Player1Cyan
import com.gamebd.rpsduel.ui.theme.Player2Pink
import com.gamebd.rpsduel.ui.theme.TextMuted
import com.gamebd.rpsduel.ui.theme.TextWhite

@Composable
fun PlayerNameScreen(
    totalRounds: Int,
    onStartMatchClick: (p1Name: String, p2Name: String) -> Unit,
    hapticsEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    var p1Name by remember { mutableStateOf("Player 1") }
    var p2Name by remember { mutableStateOf("Player 2") }

    ArcadeBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(
                    text = "ENTER PLAYERS",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentGold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Best of $totalRounds Match",
                    fontSize = 16.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Input Fields
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Player 1 Field
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "PLAYER 1",
                            color = Player1Cyan,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${p1Name.length}/12",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = p1Name,
                        onValueChange = { if (it.length <= 12) p1Name = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Player1Cyan,
                            unfocusedBorderColor = Player1Cyan.copy(alpha = 0.5f),
                            focusedContainerColor = ArcadeCard,
                            unfocusedContainerColor = ArcadeCard,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }

                // Swap Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            val temp = p1Name
                            p1Name = p2Name
                            p2Name = temp
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ArcadeCard)
                            .border(1.dp, AccentGold, RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Swap player names",
                            tint = AccentGold
                        )
                    }
                }

                // Player 2 Field
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "PLAYER 2",
                            color = Player2Pink,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${p2Name.length}/12",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = p2Name,
                        onValueChange = { if (it.length <= 12) p2Name = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Player2Pink,
                            unfocusedBorderColor = Player2Pink.copy(alpha = 0.5f),
                            focusedContainerColor = ArcadeCard,
                            unfocusedContainerColor = ArcadeCard,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }
            }

            // Bottom Start Button
            ArcadeButton(
                text = "LET'S DUEL!",
                onClick = {
                    val finalP1 = p1Name.ifBlank { "Player 1" }
                    val finalP2 = p2Name.ifBlank { "Player 2" }
                    onStartMatchClick(finalP1, finalP2)
                },
                accentColor = Player1Cyan,
                secondaryColor = Player2Pink,
                hapticsEnabled = hapticsEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}
