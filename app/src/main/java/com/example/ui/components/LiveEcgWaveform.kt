package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BioGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SpaceDarkBg
import kotlin.math.sin

@Composable
fun LiveEcgWaveform(
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    lineColor: Color = BioGreen,
    heartRateBpm: Int = 72
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ecg_wave")
    val sweepProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (60000 / heartRateBpm.coerceIn(50, 140)) * 2,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep_progress"
    )

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(SpaceDarkBg)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val h = size.height
            val midY = h * 0.5f

            // Draw oscilloscope grid lines
            val gridStepX = width / 12f
            for (i in 0..12) {
                val x = i * gridStepX
                drawLine(
                    color = Color(0x1500E5FF),
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 1f
                )
            }
            val gridStepY = h / 4f
            for (i in 0..4) {
                val y = i * gridStepY
                drawLine(
                    color = Color(0x1500E5FF),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
            }

            // Draw continuous ECG waveform
            val path = Path()
            val pointsCount = 120
            val cycleLength = width / 2.5f

            for (i in 0..pointsCount) {
                val x = (i / pointsCount.toFloat()) * width
                // ECG pattern repeating across x with animated shift
                val shiftedX = (x + sweepProgress * cycleLength) % cycleLength
                val normX = shiftedX / cycleLength

                val yOffset = when {
                    // P wave
                    normX in 0.15f..0.22f -> -sin((normX - 0.15f) / 0.07f * Math.PI).toFloat() * (h * 0.18f)
                    // PR segment
                    normX in 0.22f..0.28f -> 0f
                    // Q dip
                    normX in 0.28f..0.30f -> (h * 0.12f)
                    // R peak
                    normX in 0.30f..0.34f -> -(h * 0.44f)
                    // S dip
                    normX in 0.34f..0.38f -> (h * 0.22f)
                    // ST segment
                    normX in 0.38f..0.45f -> 0f
                    // T wave
                    normX in 0.45f..0.58f -> -sin((normX - 0.45f) / 0.13f * Math.PI).toFloat() * (h * 0.24f)
                    else -> sin(normX * Math.PI * 4).toFloat() * 1.5f
                }

                val y = midY + yOffset
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            // Draw ECG Path
            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(
                    width = 2.2f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // Draw leading scanner pulse dot
            val headX = (sweepProgress * width)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, lineColor, Color.Transparent),
                    center = Offset(headX, midY),
                    radius = 12f
                ),
                radius = 6f,
                center = Offset(headX, midY)
            )
        }
    }
}
