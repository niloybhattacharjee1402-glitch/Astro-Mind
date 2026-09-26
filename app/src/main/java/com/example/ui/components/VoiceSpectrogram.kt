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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SpaceDarkBg
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun VoiceSpectrogram(
    modifier: Modifier = Modifier,
    height: Dp = 64.dp,
    isActive: Boolean = true,
    barCount: Int = 36
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spectrogram")
    val animOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spectro_anim"
    )

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(SpaceDarkBg)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val totalWidth = size.width
            val totalHeight = size.height
            val barWidth = totalWidth / (barCount * 1.5f)
            val gap = barWidth * 0.5f

            for (i in 0 until barCount) {
                val x = i * (barWidth + gap) + gap

                // Multi-harmonic wave simulation
                val waveFactor = if (isActive) {
                    val s1 = sin(i * 0.35f + animOffset)
                    val s2 = sin(i * 0.7f - animOffset * 1.5f)
                    val combined = abs(s1 * 0.6f + s2 * 0.4f)
                    combined.coerceIn(0.12f, 0.95f)
                } else {
                    0.08f
                }

                val barH = totalHeight * waveFactor
                val y = (totalHeight - barH) / 2f

                val barBrush = Brush.verticalGradient(
                    colors = listOf(
                        NeonCyan,
                        ElectricBlue
                    ),
                    startY = y,
                    endY = y + barH
                )

                drawRoundRect(
                    brush = barBrush,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barH),
                    cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                )
            }
        }
    }
}
