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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.SportsEsports
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
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BioGreen
import com.example.ui.theme.ElectricBlue
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
fun TeamDynamicsScreen(
    viewModel: AstroMindViewModel,
    onBack: () -> Unit
) {
    val challenges by viewModel.teamChallenges.collectAsState()

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
                    text = "MODULE 10: TEAM DYNAMICS & CONFLICT PREDICTION",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = NeonCyan
                )
                Text(
                    text = "Dynamic Psychosocial AI Optimization",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // High-level team scores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TeamScoreTile(modifier = Modifier.weight(1f), label = "TEAM COHESION", value = "91%", color = BioGreen)
            TeamScoreTile(modifier = Modifier.weight(1f), label = "CONFLICT RISK", value = "18%", color = SolarYellow)
            TeamScoreTile(modifier = Modifier.weight(1f), label = "COMMUNICATION", value = "88%", color = NeonCyan)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Crew Interaction Matrix / Map
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
                        text = "CREW INTERACTION & COHESION MAP",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                    Text(text = "5-Node Graph", fontSize = 10.sp, color = TextMuted)
                }

                Spacer(modifier = Modifier.height(10.dp))

                InteractionRow("Alex Chen ↔ Sarah Khan", "Strong", 94, BioGreen)
                InteractionRow("Emma Li ↔ Michael Ross", "Strong", 89, BioGreen)
                InteractionRow("Alex Chen ↔ David Kim", "Moderate", 72, NeonCyan)
                InteractionRow("Emma Li ↔ David Kim", "Moderate", 68, NeonCyan)
                InteractionRow("Sarah Khan ↔ David Kim", "Weak (Workload Friction)", 42, AlertRed)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic Team-Building AI Recommendations
        Text(
            text = "DYNAMIC TEAM-BUILDING AI INTERVENTIONS",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = NeonCyan
        )

        Spacer(modifier = Modifier.height(8.dp))

        challenges.forEach { challenge ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                borderColor = if (challenge.isAccepted) BioGreen.copy(alpha = 0.5f) else NeonCyan.copy(alpha = 0.4f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SportsEsports,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = challenge.title,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TextWhite
                            )
                        }
                        Surface(
                            color = SpaceDarkBg,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${challenge.durationMinutes} MIN",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = NeonCyan,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Participants: ${challenge.participants.joinToString(" • ")}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.5.sp,
                        color = SolarYellow
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Objective: ${challenge.objective}",
                        fontSize = 11.sp,
                        color = TextSilver
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Expected Benefits:",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = BioGreen
                    )
                    challenge.expectedBenefits.forEach { benefit ->
                        Text(
                            text = "✓ $benefit",
                            fontSize = 10.5.sp,
                            color = TextWhite,
                            modifier = Modifier.padding(vertical = 1.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Schedule: ${challenge.scheduleRecommendation}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.5.sp,
                        color = TextDim
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.acceptTeamChallenge(challenge.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (challenge.isAccepted) BioGreen else NeonCyan,
                            contentColor = SpaceDarkBg
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (challenge.isAccepted) "SCHEDULED FOR CREW CYCLE ✓" else "CONFIRM & SCHEDULE SESSION",
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

@Composable
private fun TeamScoreTile(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: Color
) {
    Surface(
        modifier = modifier,
        color = SpaceDarkBg,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 8.5.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = color
            )
        }
    }
}

@Composable
private fun InteractionRow(
    pair: String,
    statusText: String,
    score: Int,
    statusColor: Color
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = pair, fontSize = 11.sp, color = TextWhite)
            Text(text = statusText, fontSize = 10.5.sp, color = statusColor, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = statusColor,
            trackColor = SpaceDarkBg
        )
    }
}
