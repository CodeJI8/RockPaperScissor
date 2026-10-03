package com.gamebd.rpsduel.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.gamebd.rpsduel.ui.theme.AccentGold
import com.gamebd.rpsduel.ui.theme.Player1Cyan
import com.gamebd.rpsduel.ui.theme.Player2Pink
import com.gamebd.rpsduel.ui.theme.TextWhite
import kotlin.random.Random

private data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var size: Float,
    var color: Color,
    var rotation: Float,
    var vr: Float
)

@Composable
fun ConfettiCanvas(
    modifier: Modifier = Modifier,
    particleCount: Int = 70
) {
    val colors = listOf(AccentGold, Player1Cyan, Player2Pink, TextWhite)
    val particles = remember { mutableStateListOf<Particle>() }

    LaunchedEffect(Unit) {
        // Initialize particles
        for (i in 0 until particleCount) {
            particles.add(
                Particle(
                    x = Random.nextFloat() * 1000f,
                    y = -Random.nextFloat() * 500f,
                    vx = (Random.nextFloat() - 0.5f) * 6f,
                    vy = Random.nextFloat() * 8f + 4f,
                    size = Random.nextFloat() * 16f + 12f,
                    color = colors[Random.nextInt(colors.size)],
                    rotation = Random.nextFloat() * 360f,
                    vr = (Random.nextFloat() - 0.5f) * 12f
                )
            )
        }

        while (true) {
            withFrameNanos {
                for (p in particles) {
                    p.x += p.vx
                    p.y += p.vy
                    p.rotation += p.vr
                    if (p.y > 2200f) {
                        p.y = -100f
                        p.x = Random.nextFloat() * 1000f
                    }
                }
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        for (p in particles) {
            // Normalize coordinate scale to canvas size
            val drawX = (p.x / 1000f) * width
            val drawY = (p.y / 2000f) * height

            rotate(degrees = p.rotation, pivot = Offset(drawX, drawY)) {
                drawRect(
                    color = p.color,
                    topLeft = Offset(drawX - p.size / 2f, drawY - p.size / 2f),
                    size = Size(p.size, p.size * 0.6f)
                )
            }
        }
    }
}
