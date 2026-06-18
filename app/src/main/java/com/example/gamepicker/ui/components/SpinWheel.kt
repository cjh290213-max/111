package com.example.gamepicker.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.gamepicker.ui.theme.PastelCoral
import com.example.gamepicker.ui.theme.PastelLavender
import com.example.gamepicker.ui.theme.PastelMint
import com.example.gamepicker.ui.theme.PastelPink
import com.example.gamepicker.ui.theme.PastelSky
import com.example.gamepicker.ui.theme.PastelYellow
import com.example.gamepicker.ui.theme.Primary
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SpinWheel(
    gameNames: List<String>,
    isSpinning: Boolean,
    modifier: Modifier = Modifier
) {
    val wheelColors = listOf(
        PastelPink, PastelYellow, PastelMint,
        PastelSky, PastelLavender, PastelCoral,
        PastelPink, PastelYellow
    )

    val rotation = remember { Animatable(0f) }

    // Spinning animation
    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            val targetRotation = rotation.value + 1440f + Random.nextFloat() * 720f
            rotation.animateTo(
                targetValue = targetRotation,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(320.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val diameter = minOf(canvasWidth, canvasHeight)
            val radius = diameter / 2f
            val center = Offset(canvasWidth / 2f, canvasHeight / 2f)

            val segments = if (gameNames.isEmpty()) 4 else gameNames.size
            val sweepAngle = 360f / segments

            val paint = android.graphics.Paint().apply {
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
                this.textSize = 28f
                color = android.graphics.Color.parseColor("#5C4D4D")
            }

            rotate(rotation.value, center) {
                // Draw wheel segments
                for (i in 0 until segments) {
                    val startAngle = i * sweepAngle
                    val color = wheelColors[i % wheelColors.size]

                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle - 0.5f,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(diameter, diameter)
                    )

                    // Draw text labels
                    if (gameNames.isNotEmpty() && i < gameNames.size) {
                        val textAngle = startAngle + sweepAngle / 2
                        val textRadius = radius * 0.6f
                        val textX = center.x + textRadius * cos(Math.toRadians(textAngle.toDouble())).toFloat()
                        val textY = center.y + textRadius * sin(Math.toRadians(textAngle.toDouble())).toFloat()

                        val name = gameNames[i]
                        val displayName = if (name.length > 4) name.take(4) + ".." else name

                        drawContext.canvas.nativeCanvas.drawText(
                            displayName,
                            textX,
                            textY + paint.textSize / 3f,
                            paint
                        )
                    }
                }

                // Center circle
                drawCircle(color = Color.White, radius = radius * 0.22f)
                drawCircle(color = Primary, radius = radius * 0.17f)
            }

            // Pointer (top triangle)
            val pointerPath = Path().apply {
                val pointerSize = 18.dp.toPx()
                moveTo(center.x - pointerSize, center.y - radius + 12.dp.toPx())
                lineTo(center.x + pointerSize, center.y - radius + 12.dp.toPx())
                lineTo(center.x, center.y - radius - pointerSize)
                close()
            }
            drawPath(path = pointerPath, color = Primary)
        }
    }
}
