package com.gamebd.rpsduel.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.gamebd.rpsduel.ui.theme.ArcadeBackground
import com.gamebd.rpsduel.ui.theme.ArcadeSurface
import com.gamebd.rpsduel.ui.theme.Player1Cyan
import com.gamebd.rpsduel.ui.theme.Player2Pink

@Composable
fun ArcadeBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background_glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ArcadeBackground,
                        ArcadeSurface,
                        ArcadeBackground
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top-left cyan neon ambient glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Player1Cyan.copy(alpha = pulseAlpha * 0.4f),
                        Color.Transparent
                    ),
                    center = Offset(0f, 0f),
                    radius = width * 0.7f
                ),
                radius = width * 0.7f,
                center = Offset(0f, 0f)
            )

            // Bottom-right pink neon ambient glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Player2Pink.copy(alpha = pulseAlpha * 0.4f),
                        Color.Transparent
                    ),
                    center = Offset(width, height),
                    radius = width * 0.7f
                ),
                radius = width * 0.7f,
                center = Offset(width, height)
            )
        }

        content()
    }
}
