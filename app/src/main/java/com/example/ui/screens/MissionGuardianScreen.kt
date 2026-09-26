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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.AlertSeverity
import com.example.ui.components.GlassCard
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BioGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpacePanelBorder
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSilver
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AstroMindViewModel

@Composable
fun MissionGuardianScreen(
    viewModel: AstroMindViewModel,
    onBack: () -> Unit
) {
    val alerts by viewModel.missionAlerts.collectAsState()

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
                    text = "ASTROMIND MISSION GUARDIAN",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NeonCyan
                )
                Text(
                    text = "Integrated Multi-Sensor Anomaly & Emergency System",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Guardian Active Sensor Matrix
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = SpacePanelBorder
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "INTEGRATED CORRELATION STREAMS",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(BioGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("7/7 STREAMS ACTIVE", fontFamily = FontFamily.Monospace, fontSize = 9.sp, color = BioGreen)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SensorChip("🫀 Crew Health", BioGreen)
                    SensorChip("🧠 Mental", BioGreen)
                    SensorChip("☢️ Radiation", AlertRed)
                    SensorChip("🌡️ Temp", BioGreen)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SensorChip("🫁 Air Quality", SolarYellow)
                    SensorChip("😴 Fatigue", AlertRed)
                    SensorChip("🚨 Mission Events", BioGreen)
                    SensorChip("🛰️ Telemetry", BioGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ACTIVE MISSION ALERTS",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = AlertRed
        )

        Spacer(modifier = Modifier.height(8.dp))

        alerts.forEach { alert ->
            val alertBorder = when (alert.severity) {
                AlertSeverity.CRITICAL -> AlertRed
                AlertSeverity.ELEVATED -> SolarYellow
                AlertSeverity.ADVISORY -> NeonCyan
            }

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                borderColor = if (alert.isResolved) BioGreen.copy(alpha = 0.4f) else alertBorder.copy(alpha = 0.7f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (alert.isResolved) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (alert.isResolved) BioGreen else alertBorder,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (alert.isResolved) "RESOLVED" else alert.severity.label,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (alert.isResolved) BioGreen else alertBorder
                            )
                        }

                        Text(
                            text = alert.timestamp,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = TextDim
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = alert.title,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = alert.description,
                        fontSize = 11.sp,
                        color = TextSilver,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Multi-sensor correlation badge
                    Surface(
                        color = SpaceDarkBg,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Hub,
                                contentDescription = "Correlation",
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CORRELATION: ${alert.multiSensorCorrelation}",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.5.sp,
                                color = NeonCyan
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "AI ACTION RESPONSE PROTOCOL:",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (alert.isResolved) BioGreen else alertBorder
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    alert.recommendedActions.forEach { action ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "→", color = if (alert.isResolved) BioGreen else alertBorder, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = action, fontSize = 11.sp, color = TextWhite)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!alert.isResolved) {
                        Button(
                            onClick = { viewModel.resolveAlert(alert.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (alert.severity == AlertSeverity.CRITICAL) AlertRed else SolarYellow,
                                contentColor = SpaceDarkBg
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "EXECUTE ACTIONS & ACKNOWLEDGE",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SensorChip(name: String, color: Color) {
    Surface(
        color = SpaceDarkBg,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(0.75.dp, color.copy(alpha = 0.4f))
    ) {
        Text(
            text = name,
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}
