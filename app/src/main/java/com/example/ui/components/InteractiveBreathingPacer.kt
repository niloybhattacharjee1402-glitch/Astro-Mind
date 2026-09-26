package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BioGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay

@Composable
fun InteractiveBreathingPacer(
    modifier: Modifier = Modifier,
    onComplete: (() -> Unit)? = null
) {
    var isRunning by remember { mutableStateOf(false) }
    var phase by remember { mutableStateOf("Inhale") } // Inhale (4s), Hold (4s), Exhale (6s)
    var secondsLeftInPhase by remember { mutableIntStateOf(4) }
    var totalRoundsCompleted by remember { mutableIntStateOf(0) }

    val circleScale by animateFloatAsState(
        targetValue = when (phase) {
            "Inhale" -> 1.0f
            "Hold" -> 1.0f
            else -> 0.45f
        },
        animationSpec = tween(
            durationMillis = when (phase) {
                "Inhale" -> 4000
                "Hold" -> 500
                else -> 6000
            },
            easing = FastOutSlowInEasing
        ),
        label = "breath_scale"
    )

    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect
        while (isRunning) {
            // Inhale 4s
            phase = "Inhale"
            for (sec in 4 downTo 1) {
                secondsLeftInPhase = sec
                delay(1000)
            }
            // Hold 4s
            phase = "Hold"
            for (sec in 4 downTo 1) {
                secondsLeftInPhase = sec
                delay(1000)
            }
            // Exhale 6s
            phase = "Exhale"
            for (sec in 6 downTo 1) {
                secondsLeftInPhase = sec
                delay(1000)
            }
            totalRoundsCompleted++
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(180.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.width * 0.42f
                val activeRadius = baseRadius * circleScale

                // Outer guide circle
                drawCircle(
                    color = Color(0x2200E5FF),
                    radius = baseRadius,
                    center = center,
                    style = Stroke(width = 2f)
                )

                // Expanding breathing orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NeonCyan.copy(alpha = 0.55f),
                            BioGreen.copy(alpha = 0.25f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = activeRadius
                    ),
                    radius = activeRadius,
                    center = center
                )

                drawCircle(
                    color = if (phase == "Hold") BioGreen else NeonCyan,
                    radius = activeRadius,
                    center = center,
                    style = Stroke(width = 3f)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isRunning) phase.uppercase() else "READY",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (phase == "Hold") BioGreen else NeonCyan
                )
                if (isRunning) {
                    Text(
                        text = "${secondsLeftInPhase}s",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (isRunning) "Deep Space Autonomic Regulation • Round $totalRoundsCompleted" else "Pre-EVA / Recovery 4-4-6 Respiration Cycle",
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                isRunning = !isRunning
                if (!isRunning && onComplete != null) onComplete()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) Color(0xFFEF4444) else NeonCyan,
                contentColor = SpaceDarkBg
            )
        ) {
            Text(
                text = if (isRunning) "PAUSE SESSION" else "START BREATHING PACER",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
