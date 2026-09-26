package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.LiveEcgWaveform
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BioGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpacePanelBg
import com.example.ui.theme.SpacePanelBorder
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AstroMindViewModel

@Composable
fun BioMonitoringScreen(
    viewModel: AstroMindViewModel,
    onBack: () -> Unit
) {
    val selectedCrew = viewModel.getSelectedCrew()
    val isVestConnected by viewModel.wearableVestConnected.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .padding(14.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NeonCyan
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "MODULE 1: REAL-TIME BIO-MONITORING",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NeonCyan
                )
                Text(
                    text = "${selectedCrew.name} (${selectedCrew.role}) • Telemetry Feed",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Smart Wearable Vest Integration Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = if (isVestConnected) BioGreen.copy(alpha = 0.5f) else AlertRed.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Sensors,
                        contentDescription = "Smart Vest",
                        tint = if (isVestConnected) BioGreen else AlertRed,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "SMART WEARABLE VEST INTEGRATION",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = TextWhite
                        )
                        Text(
                            text = if (isVestConnected) "12-Lead Continuous ECG • Multi-point Piezo Respiration" else "Vest Telemetry Disconnected",
                            fontSize = 10.sp,
                            color = if (isVestConnected) BioGreen else AlertRed
                        )
                    }
                }

                Button(
                    onClick = { viewModel.toggleWearableVest() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVestConnected) SpacePanelBorder else BioGreen,
                        contentColor = TextWhite
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isVestConnected) "SYNCED" else "CONNECT",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Primary Vitals Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VitalTile(
                modifier = Modifier.weight(1f),
                title = "HEART RATE",
                value = "${selectedCrew.heartRate}",
                unit = "BPM",
                status = "Nominal",
                color = BioGreen
            )
            VitalTile(
                modifier = Modifier.weight(1f),
                title = "HRV (rMSSD)",
                value = "${selectedCrew.hrv}",
                unit = "ms",
                status = if (selectedCrew.hrv < 45) "Strained" else "Balanced",
                color = if (selectedCrew.hrv < 45) SolarYellow else BioGreen
            )
            VitalTile(
                modifier = Modifier.weight(1f),
                title = "BLOOD OXYGEN",
                value = "${selectedCrew.spO2}",
                unit = "%",
                status = "Optimal",
                color = NeonCyan
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VitalTile(
                modifier = Modifier.weight(1f),
                title = "RESPIRATION",
                value = "${selectedCrew.respiration}",
                unit = "/min",
                status = "Steady",
                color = NeonCyan
            )
            VitalTile(
                modifier = Modifier.weight(1f),
                title = "TEMPERATURE",
                value = "${selectedCrew.bodyTemp}",
                unit = "°C",
                status = "Normothermia",
                color = BioGreen
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live ECG Oscilloscope Graph
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = BioGreen.copy(alpha = 0.5f)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "ECG",
                            tint = AlertRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LIVE ECG OSCILLOSCOPE (LEAD II)",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = TextWhite
                        )
                    }
                    Text(
                        text = "FILTER: 0.05-150Hz",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = TextDim
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LiveEcgWaveform(
                    height = 110.dp,
                    heartRateBpm = selectedCrew.heartRate,
                    lineColor = BioGreen
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "QRS Duration: 88 ms • QT Interval: 390 ms • PR: 154 ms",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Sinus Rhythm",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = BioGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stress Score & Fatigue Index
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = SpacePanelBorder
        ) {
            Column {
                Text(
                    text = "AUTONOMIC LOAD & RECOVERY INDEX",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = NeonCyan
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stress Score Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Stress Score", fontSize = 11.sp, color = TextWhite)
                    Text(
                        text = "${selectedCrew.stressScore}% (${if (selectedCrew.stressScore > 50) "High" else "Low-Moderate"})",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedCrew.stressScore > 50) AlertRed else BioGreen
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { selectedCrew.stressScore / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (selectedCrew.stressScore > 50) AlertRed else BioGreen,
                    trackColor = SpaceDarkBg,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Fatigue Index Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Fatigue Index", fontSize = 11.sp, color = TextWhite)
                    Text(
                        text = "${selectedCrew.fatigueIndex}% (${if (selectedCrew.fatigueIndex > 60) "Elevated" else "Stable"})",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedCrew.fatigueIndex > 60) SolarYellow else BioGreen
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { selectedCrew.fatigueIndex / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (selectedCrew.fatigueIndex > 60) SolarYellow else BioGreen,
                    trackColor = SpaceDarkBg,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun VitalTile(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    status: String,
    color: Color
) {
    Surface(
        modifier = modifier,
        color = SpacePanelBg,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title, fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = unit, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = TextDim)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = status,
                fontSize = 9.5.sp,
                fontFamily = FontFamily.Monospace,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
