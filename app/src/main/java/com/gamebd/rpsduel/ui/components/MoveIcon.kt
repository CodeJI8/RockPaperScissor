package com.gamebd.rpsduel.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamebd.rpsduel.model.Move
import com.gamebd.rpsduel.ui.theme.AccentGold
import com.gamebd.rpsduel.ui.theme.Player1Cyan

@Composable
fun MoveIcon(
    move: Move,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    tint: Color = Player1Cyan,
    useCanvasVectors: Boolean = true
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (useCanvasVectors) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = this.size.width
                val height = this.size.height
                val center = Offset(width / 2f, height / 2f)

                when (move) {
                    Move.ROCK -> {
                        // Stylized Boulder/Fist shape
                        val path = Path().apply {
                            moveTo(width * 0.3f, height * 0.25f)
                            lineTo(width * 0.7f, height * 0.2f)
                            lineTo(width * 0.88f, height * 0.45f)
                            lineTo(width * 0.8f, height * 0.8f)
                            lineTo(width * 0.45f, height * 0.85f)
                            lineTo(width * 0.15f, height * 0.65f)
                            close()
                        }
                        drawPath(
                            path = path,
                            color = tint.copy(alpha = 0.25f)
                        )
                        drawPath(
                            path = path,
                            color = tint,
                            style = Stroke(width = width * 0.08f, cap = StrokeCap.Round)
                        )
                        // Inner facet highlight
                        drawLine(
                            color = tint,
                            start = Offset(width * 0.3f, height * 0.25f),
                            end = Offset(width * 0.5f, height * 0.55f),
                            strokeWidth = width * 0.05f
                        )
                        drawLine(
                            color = tint,
                            start = Offset(width * 0.5f, height * 0.55f),
                            end = Offset(width * 0.8f, height * 0.8f),
                            strokeWidth = width * 0.05f
                        )
                    }
                    Move.PAPER -> {
                        // Stylized Page with Folded Top-Right Corner
                        val foldSize = width * 0.22f
                        val path = Path().apply {
                            moveTo(width * 0.25f, height * 0.15f)
                            lineTo(width * 0.75f - foldSize, height * 0.15f)
                            lineTo(width * 0.75f, height * 0.15f + foldSize)
                            lineTo(width * 0.75f, height * 0.85f)
                            lineTo(width * 0.25f, height * 0.85f)
                            close()
                        }
                        drawPath(
                            path = path,
                            color = tint.copy(alpha = 0.25f)
                        )
                        drawPath(
                            path = path,
                            color = tint,
                            style = Stroke(width = width * 0.08f, cap = StrokeCap.Round)
                        )
                        // Fold line
                        val foldLine = Path().apply {
                            moveTo(width * 0.75f - foldSize, height * 0.15f)
                            lineTo(width * 0.75f - foldSize, height * 0.15f + foldSize)
                            lineTo(width * 0.75f, height * 0.15f + foldSize)
                        }
                        drawPath(
                            path = foldLine,
                            color = tint,
                            style = Stroke(width = width * 0.06f, cap = StrokeCap.Round)
                        )
                        // Content lines
                        drawLine(
                            color = tint,
                            start = Offset(width * 0.35f, height * 0.45f),
                            end = Offset(width * 0.65f, height * 0.45f),
                            strokeWidth = width * 0.05f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = tint,
                            start = Offset(width * 0.35f, height * 0.62f),
                            end = Offset(width * 0.58f, height * 0.62f),
                            strokeWidth = width * 0.05f,
                            cap = StrokeCap.Round
                        )
                    }
                    Move.SCISSORS -> {
                        // Stylized Scissors (Crossed blades & rings)
                        rotate(degrees = -15f, pivot = center) {
                            // Ring Left
                            drawCircle(
                                color = tint,
                                radius = width * 0.16f,
                                center = Offset(width * 0.3f, height * 0.75f),
                                style = Stroke(width = width * 0.07f)
                            )
                            // Ring Right
                            drawCircle(
                                color = tint,
                                radius = width * 0.16f,
                                center = Offset(width * 0.7f, height * 0.75f),
                                style = Stroke(width = width * 0.07f)
                            )
                            // Blade 1
                            drawLine(
                                color = tint,
                                start = Offset(width * 0.3f, height * 0.75f),
                                end = Offset(width * 0.8f, height * 0.2f),
                                strokeWidth = width * 0.08f,
                                cap = StrokeCap.Round
                            )
                            // Blade 2
                            drawLine(
                                color = tint,
                                start = Offset(width * 0.7f, height * 0.75f),
                                end = Offset(width * 0.2f, height * 0.2f),
                                strokeWidth = width * 0.08f,
                                cap = StrokeCap.Round
                            )
                            // Pivot Screw
                            drawCircle(
                                color = AccentGold,
                                radius = width * 0.06f,
                                center = Offset(width * 0.5f, height * 0.52f)
                            )
                        }
                    }
                }
            }
        } else {
            val emoji = when (move) {
                Move.ROCK -> "✊"
                Move.PAPER -> "✋"
                Move.SCISSORS -> "✌️"
            }
            Text(
                text = emoji,
                fontSize = (size.value * 0.55f).sp
            )
        }
    }
}
