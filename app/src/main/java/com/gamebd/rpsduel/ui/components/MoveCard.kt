package com.gamebd.rpsduel.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamebd.rpsduel.model.Move
import com.gamebd.rpsduel.ui.theme.AccentGold
import com.gamebd.rpsduel.ui.theme.ArcadeCard
import com.gamebd.rpsduel.ui.theme.Player1Cyan
import com.gamebd.rpsduel.ui.theme.TextWhite

@Composable
fun MoveCard(
    move: Move,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    playerColor: Color = Player1Cyan,
    hapticsEnabled: Boolean = true,
    enabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            isSelected -> 1.08f
            isPressed && enabled -> 0.95f
            else -> 1.0f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )

    val moveName = when (move) {
        Move.ROCK -> "Rock"
        Move.PAPER -> "Paper"
        Move.SCISSORS -> "Scissors"
    }

    Surface(
        modifier = modifier
            .scale(scale)
            .defaultMinSize(minWidth = 100.dp, minHeight = 130.dp)
            .semantics {
                contentDescription = "Select $moveName"
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button
            ) {
                if (hapticsEnabled) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                onSelect()
            },
        shape = RoundedCornerShape(24.dp),
        color = ArcadeCard,
        border = BorderStroke(
            width = if (isSelected) 3.5.dp else 2.dp,
            brush = if (isSelected) {
                Brush.horizontalGradient(listOf(AccentGold, playerColor))
            } else {
                Brush.verticalGradient(listOf(playerColor.copy(alpha = 0.6f), ArcadeCard))
            }
        ),
        shadowElevation = if (isSelected) 16.dp else 6.dp
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = if (isSelected) {
                        Brush.verticalGradient(
                            colors = listOf(
                                playerColor.copy(alpha = 0.35f),
                                AccentGold.copy(alpha = 0.2f)
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                playerColor.copy(alpha = 0.12f),
                                Color.Transparent
                            )
                        )
                    }
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MoveIcon(
                move = move,
                size = 64.dp,
                tint = if (isSelected) AccentGold else playerColor
            )

            Text(
                text = moveName,
                color = if (isSelected) AccentGold else TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}
