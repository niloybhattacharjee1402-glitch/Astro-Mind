package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BioGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpacePanelBg
import com.example.ui.theme.SpacePanelBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun MissionTopBar(
    sol: Int = 178,
    utcTime: String = "15:24:36",
    isOfflineAi: Boolean = true,
    activeAlertsCount: Int = 2,
    onToggleAiMode: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onEmergencyClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SpacePanelBg.copy(alpha = 0.95f),
        border = BorderStroke(1.dp, SpacePanelBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: NASA insignia + ASTROMIND AI
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0B3D91)), // NASA Blue
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NASA",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp,
                        color = TextWhite,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "ASTROMIND",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            letterSpacing = 1.sp,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = NeonCyan
                        )
                    }
                    Text(
                        text = "SOL $sol | MARS MISSION • UTC $utcTime",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.5.sp,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Right: AI Mode Chip + Alert icon + Emergency Support
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Offline / Online AI Chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SpaceDarkBg,
                    border = BorderStroke(
                        1.dp,
                        if (isOfflineAi) BioGreen.copy(alpha = 0.5f) else NeonCyan.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.clickable { onToggleAiMode() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isOfflineAi) BioGreen else NeonCyan)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (isOfflineAi) "OFFLINE AI" else "CLOUD GEMINI",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = if (isOfflineAi) BioGreen else NeonCyan
                        )
                    }
                }

                // Alerts icon
                BadgedBox(
                    badge = {
                        if (activeAlertsCount > 0) {
                            Badge(
                                containerColor = AlertRed,
                                contentColor = TextWhite
                            ) {
                                Text(text = "$activeAlertsCount", fontSize = 9.sp)
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onAlertsClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Mission Guardian Alerts",
                            tint = if (activeAlertsCount > 0) AlertRed else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Quick Emergency protocol
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AlertRed.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, AlertRed.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { onEmergencyClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Emergency Support",
                            tint = AlertRed,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "SOS",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = AlertRed
                        )
                    }
                }
            }
        }
    }
}
