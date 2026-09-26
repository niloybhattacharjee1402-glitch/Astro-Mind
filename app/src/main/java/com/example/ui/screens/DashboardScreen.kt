package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CrewMember
import com.example.data.CrewStatus
import com.example.ui.components.CircularHealthGauge
import com.example.ui.components.GlassCard
import com.example.ui.components.LiveEcgWaveform
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BioGreen
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpacePanelBg
import com.example.ui.theme.SpacePanelBorder
import com.example.ui.theme.SpacePanelLight
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSilver
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AstroMindViewModel

@Composable
fun DashboardScreen(
    viewModel: AstroMindViewModel,
    onNavigateToBio: () -> Unit,
    onNavigateToIntervention: () -> Unit,
    onNavigateToGuardian: () -> Unit,
    onNavigateToTeam: () -> Unit,
    onNavigateToCompanion: () -> Unit,
    onNavigateToCircadian: () -> Unit,
    onNavigateToDiagnostics: () -> Unit,
    onNavigateToExercise: () -> Unit,
    onNavigateToEye: () -> Unit,
    onNavigateToVoice: () -> Unit,
    onNavigateToRadiation: () -> Unit,
    onNavigateToEnvironment: () -> Unit,
    onNavigateToCrisis: () -> Unit
) {
    val crewList by viewModel.crewMembers.collectAsState()
    val selectedCrewId by viewModel.selectedCrewId.collectAsState()
    val environment by viewModel.environment.collectAsState()
    val predictiveRisk by viewModel.predictiveRisk.collectAsState()
    val selectedCrew = viewModel.getSelectedCrew()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        // TOP MISSION BANNER (Status, Health Index 92%, Predictive Risk, Deep Space Inspiration)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Mission Status
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = BioGreen.copy(alpha = 0.4f)
            ) {
                Column {
                    Text(
                        text = "MISSION STATUS",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(BioGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ACTIVE",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = BioGreen
                        )
                    }
                    Text(
                        text = "All systems nominal",
                        fontSize = 10.5.sp,
                        color = TextDim
                    )
                }
            }

            // Crew Health Index 92%
            GlassCard(
                modifier = Modifier.weight(1.1f),
                borderColor = NeonCyan.copy(alpha = 0.4f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "CREW HEALTH",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "92%",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = TextWhite
                        )
                        Text(
                            text = "Good • 5/5 Tracked",
                            fontSize = 10.sp,
                            color = BioGreen
                        )
                    }
                    CircularHealthGauge(
                        percentage = 92,
                        label = "Good",
                        size = 56.dp,
                        strokeWidth = 5.dp
                    )
                }
            }

            // Predictive Risk
            GlassCard(
                modifier = Modifier.weight(1.2f),
                borderColor = SolarYellow.copy(alpha = 0.4f),
                onClick = onNavigateToCrisis
            ) {
                Column {
                    Text(
                        text = "PREDICTIVE RISK (72h)",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RiskItem(label = "Mental", value = "${predictiveRisk.mentalRisk}%", color = BioGreen)
                        RiskItem(label = "Fatigue", value = "${predictiveRisk.fatigueRisk}%", color = SolarYellow)
                        RiskItem(label = "Conflict", value = "${predictiveRisk.conflictRisk}%", color = SolarYellow)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // CREW MEMBERS HORIZONTAL ROW (Alex, Sarah, Michael, Emma, David)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CREW TELEMETRY VESTS",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp,
                color = NeonCyan
            )
            Text(
                text = "4 Stable • 1 Fatigue",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            crewList.forEach { member ->
                val isSelected = member.id == selectedCrewId
                CrewMemberCard(
                    member = member,
                    isSelected = isSelected,
                    onClick = { viewModel.selectCrewMember(member.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SPACECRAFT ENVIRONMENT (CO2, Radiation, Temp, Humidity, O2)
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = SpacePanelBorder,
            onClick = onNavigateToEnvironment
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SPACECRAFT ENVIRONMENT (ECLSS)",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                    Text(
                        text = "CABIN STATUS: SAFE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = BioGreen
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    EnvTelemetryItem(icon = Icons.Default.Air, label = "CO₂", value = "${environment.co2Ppm} ppm", statusColor = BioGreen)
                    EnvTelemetryItem(icon = Icons.Default.Security, label = "Radiation", value = "${environment.radiationMsv} mSv", statusColor = BioGreen)
                    EnvTelemetryItem(icon = Icons.Default.Thermostat, label = "Temp", value = "${environment.cabinTempC}°C", statusColor = NeonCyan)
                    EnvTelemetryItem(icon = Icons.Default.WaterDrop, label = "Humidity", value = "${environment.humidityPercent}%", statusColor = NeonCyan)
                    EnvTelemetryItem(icon = Icons.Default.CheckCircle, label = "Oxygen", value = "${environment.o2Percent}%", statusColor = BioGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 12 CORE MODULES GRID
        Text(
            text = "MISSION HEALTH & SUPPORT MODULES (12)",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            letterSpacing = 0.5.sp,
            color = NeonCyan
        )

        Spacer(modifier = Modifier.height(10.dp))

        // ROW 1: Module 1 (Bio-Monitoring) & Module 2 (Mental Wellness)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Module 1: Bio-Monitoring
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = NeonCyan.copy(alpha = 0.5f),
                onClick = onNavigateToBio
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "1. BIO-MONITORING",
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
                            Text("LIVE", fontFamily = FontFamily.Monospace, fontSize = 9.sp, color = BioGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LiveEcgWaveform(
                        height = 42.dp,
                        heartRateBpm = selectedCrew.heartRate,
                        lineColor = BioGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricLine("Heart Rate", "${selectedCrew.heartRate} BPM")
                        MetricLine("HRV", "${selectedCrew.hrv} ms")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricLine("SpO₂", "${selectedCrew.spO2}%")
                        MetricLine("Respiration", "${selectedCrew.respiration}/min")
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "View Bio Graph →")
                }
            }

            // Module 2: Mental Wellness
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToIntervention
            ) {
                Column {
                    Text(
                        text = "2. MENTAL WELLNESS",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricLine("Mood Index", selectedCrew.mood)
                        MetricLine("Stress Level", if (selectedCrew.stressScore > 50) "High" else "Moderate")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricLine("Burnout Risk", "${selectedCrew.burnoutRisk}%")
                        MetricLine("Sleep Quality", "${selectedCrew.sleepQuality}%")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = SpaceDarkBg,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "AI: Try a 5-min breathing session.",
                            fontSize = 10.sp,
                            color = NeonCyan,
                            modifier = Modifier.padding(6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "View Details →")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ROW 2: Module 3 (Voice Analytics) & Module 4 (Eye Assessment)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Module 3: Voice Analytics
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToVoice
            ) {
                Column {
                    Text(
                        text = "3. VOICE ANALYTICS",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MetricLine("Speech Energy", selectedCrew.speechEnergy)
                    MetricLine("Pitch Stability", "${selectedCrew.pitchStability}%")

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Emotion: Happy ${selectedCrew.emotionHappy}% • Stress ${selectedCrew.emotionStress}%",
                        fontSize = 9.5.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    ModuleActionLink(label = "Vocal Stress Test →")
                }
            }

            // Module 4: Eye Assessment (SANS)
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToEye
            ) {
                Column {
                    Text(
                        text = "4. EYE ASSESSMENT",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MetricLine("Vision Score", "${selectedCrew.visionScore}%")
                    MetricLine("SANS Risk", selectedCrew.sansRisk)
                    MetricLine("Pupil Response", selectedCrew.pupilResponse)
                    MetricLine("Eye Movement", selectedCrew.eyeMovement)

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "SANS Optical Scan →")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ROW 3: Module 5 (Exercise & Physical) & Module 6 (Circadian Control)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Module 5: Exercise & Physical
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToExercise
            ) {
                Column {
                    Text(
                        text = "5. EXERCISE & PHYSICAL",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MetricLine("Today's Goal", "${selectedCrew.exerciseGoalMin} min")
                    MetricLine("Completed", "${selectedCrew.exerciseCompletedMin} min (${(selectedCrew.exerciseCompletedMin * 100) / selectedCrew.exerciseGoalMin}%)")
                    MetricLine("Calories", "${selectedCrew.caloriesBurned} kcal")
                    MetricLine("Devices", "✓ ARED ✓ T2 ✓ CEVIS")

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "Workout Systems →")
                }
            }

            // Module 6: Circadian Control
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToCircadian
            ) {
                Column {
                    Text(
                        text = "6. CIRCADIAN CONTROL",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MetricLine("Current Phase", "Wake Cycle")
                    MetricLine("Light Spectrum", "6500K")
                    MetricLine("Sleep Quality", "${selectedCrew.sleepQuality}%")

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = NeonCyan.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("MORNING", fontSize = 8.sp, color = NeonCyan, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold)
                        }
                        Surface(
                            color = SpaceDarkBg,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("EVENING", fontSize = 8.sp, color = TextDim, modifier = Modifier.padding(4.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "Spectrum Control →")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ROW 4: Module 7 (Radiation Monitoring) & Module 8 (Diagnostics Lab)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Module 7: Radiation Monitoring
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToRadiation
            ) {
                Column {
                    Text(
                        text = "7. RADIATION CENTER",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MetricLine("Current Exposure", "${environment.radiationMsv} mSv")
                    MetricLine("Daily Limit", "2.0 mSv")
                    MetricLine("Shield Status", "OPTIMAL")
                    MetricLine("Solar Storm Risk", "LOW")

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "Dosimeter Grid →")
                }
            }

            // Module 8: In-Flight Diagnostics Lab
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToDiagnostics
            ) {
                Column {
                    Text(
                        text = "8. DIAGNOSTICS LAB",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MetricLine("Blood Test", "Normal")
                    MetricLine("Urine Test", "Normal")
                    MetricLine("Inflammation", "Low")
                    MetricLine("Ultrasound", "Completed")

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "Run Diagnostics →")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ROW 5: Module 9 (Environmental Health) & Module 10 (Team Dynamics)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Module 9: Environmental Health
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToEnvironment
            ) {
                Column {
                    Text(
                        text = "9. CABIN ENVIRONMENT",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MetricLine("Air Quality", "${environment.airQualityPercent}%")
                    MetricLine("Water Quality", "${environment.waterQualityPercent}%")
                    MetricLine("Microbial Risk", environment.microbialRisk)
                    MetricLine("CO₂ Level", "${environment.co2Ppm} ppm")

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "ECLSS Telemetry →")
                }
            }

            // Module 10: Team Dynamics
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToTeam
            ) {
                Column {
                    Text(
                        text = "10. TEAM DYNAMICS",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MetricLine("Team Cohesion", "91%")
                    MetricLine("Conflict Risk", "18%")
                    MetricLine("Communication", "88%")
                    MetricLine("Sarah ↔ David", "Weak")

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "Team Challenge →")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ROW 6: Module 11 (Predictive Engine) & Module 12 (AstroMind AI Companion)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Module 11: Predictive Crisis Engine
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = SpacePanelBorder,
                onClick = onNavigateToCrisis
            ) {
                Column {
                    Text(
                        text = "11. 72h PREDICTIVE ENGINE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MetricLine("Mental Risk", "${predictiveRisk.mentalRisk}%")
                    MetricLine("Fatigue Risk", "${predictiveRisk.fatigueRisk}%")
                    MetricLine("Conflict Risk", "${predictiveRisk.conflictRisk}%")
                    MetricLine("Mission Perf.", "${predictiveRisk.missionPerformance}%")

                    Spacer(modifier = Modifier.height(6.dp))
                    ModuleActionLink(label = "Crisis Forecast →")
                }
            }

            // Module 12: AstroMind AI Companion
            GlassCard(
                modifier = Modifier.weight(1f),
                borderColor = NeonCyan.copy(alpha = 0.5f),
                onClick = onNavigateToCompanion
            ) {
                Column {
                    Text(
                        text = "12. ASTROMIND AI",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "\"Good morning Alex. Sleep improved by 8%. Radiation normal. Ready for EVA pre-breathe.\"",
                        fontSize = 10.sp,
                        color = TextSilver,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            color = NeonCyan,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "TALK TO AI",
                                fontSize = 8.5.sp,
                                color = SpaceDarkBg,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
                            )
                        }
                        Surface(
                            color = SpaceDarkBg,
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, NeonCyan),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "SESSION",
                                fontSize = 8.5.sp,
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // QUICK ACTIONS TOOLBAR (Emergency SOS, Intervention, Guardian, Team Challenge)
        Text(
            text = "QUICK MISSION CONTROL ACTIONS",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = NeonCyan
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickActionButton(
                icon = Icons.Default.Emergency,
                label = "Emergency Support",
                color = AlertRed,
                onClick = onNavigateToGuardian
            )
            QuickActionButton(
                icon = Icons.Default.Psychology,
                label = "AI Interventions",
                color = NeonCyan,
                onClick = onNavigateToIntervention
            )
            QuickActionButton(
                icon = Icons.Default.Security,
                label = "Mission Guardian",
                color = SolarYellow,
                onClick = onNavigateToGuardian
            )
            QuickActionButton(
                icon = Icons.Default.DirectionsRun,
                label = "Start Exercise",
                color = BioGreen,
                onClick = onNavigateToExercise
            )
            QuickActionButton(
                icon = Icons.Default.Lightbulb,
                label = "Adjust Lighting",
                color = ElectricBlue,
                onClick = onNavigateToCircadian
            )
            QuickActionButton(
                icon = Icons.Default.MedicalServices,
                label = "Run Diagnostics",
                color = NeonCyan,
                onClick = onNavigateToDiagnostics
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun CrewMemberCard(
    member: CrewMember,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val statusColor = when (member.status) {
        CrewStatus.STABLE -> BioGreen
        CrewStatus.FATIGUE -> SolarYellow
        CrewStatus.HIGH_STRESS -> AlertRed
        CrewStatus.CRITICAL -> AlertRed
    }

    Surface(
        modifier = Modifier
            .width(138.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = if (isSelected) SpacePanelLight else SpacePanelBg,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) NeonCyan else SpacePanelBorder
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = member.avatarEmoji, fontSize = 22.sp)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = member.name,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TextWhite
            )
            Text(
                text = member.role,
                fontSize = 10.sp,
                color = TextDim
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                color = statusColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = member.status.label.uppercase(),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = statusColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun EnvTelemetryItem(
    icon: ImageVector,
    label: String,
    value: String,
    statusColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = statusColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 9.5.sp, color = TextMuted)
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.5.sp,
            color = TextWhite
        )
    }
}

@Composable
private fun MetricLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 10.sp, color = TextMuted)
        Text(text = value, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = TextWhite, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RiskItem(label: String, value: String, color: Color) {
    Column {
        Text(text = label, fontSize = 9.sp, color = TextMuted)
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = color
        )
    }
}

@Composable
private fun ModuleActionLink(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, fontSize = 9.5.sp, color = NeonCyan, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SpacePanelBg,
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = TextWhite
            )
        }
    }
}
