package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CircularHealthGauge
import com.example.ui.components.GlassCard
import com.example.ui.components.VoiceSpectrogram
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BioGreen
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SolarYellow
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpacePanelBg
import com.example.ui.theme.SpacePanelBorder
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSilver
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AstroMindViewModel

enum class ModuleTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    VOICE("Voice Analytics", Icons.Default.Mic),
    EYE("Eye Assessment", Icons.Default.Visibility),
    EXERCISE("Exercise Center", Icons.Default.DirectionsRun),
    CIRCADIAN("Circadian Light", Icons.Default.Lightbulb),
    RADIATION("Radiation Center", Icons.Default.Security),
    DIAGNOSTICS("Diagnostics Lab", Icons.Default.MedicalServices),
    ENVIRONMENT("Cabin ECLSS", Icons.Default.Air),
    CRISIS_ENGINE("72h Forecast", Icons.Default.Timeline)
}

@Composable
fun ModulesHubScreen(
    viewModel: AstroMindViewModel,
    initialTab: ModuleTab = ModuleTab.VOICE,
    onBack: () -> Unit
) {
    var selectedTab by remember { androidx.compose.runtime.mutableStateOf(initialTab) }
    val selectedCrew = viewModel.getSelectedCrew()
    val environment by viewModel.environment.collectAsState()
    val predictiveRisk by viewModel.predictiveRisk.collectAsState()
    val isRecordingVoice by viewModel.isRecordingVoice.collectAsState()
    val voiceResult by viewModel.voiceAnalysisResult.collectAsState()
    val spectrumK by viewModel.lightSpectrumKelvin.collectAsState()
    val lightingMode by viewModel.activeLightingMode.collectAsState()
    val exerciseDevice by viewModel.activeExerciseDevice.collectAsState()
    val scanResult by viewModel.lastScanResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .padding(14.dp)
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
                    text = "DEEP SPACE SUBSYSTEMS & LABS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NeonCyan
                )
                Text(
                    text = "Astronaut: ${selectedCrew.name} • Mars Mission Sol 178",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Category Tab Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ModuleTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                Surface(
                    onClick = { selectedTab = tab },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) NeonCyan.copy(alpha = 0.2f) else SpacePanelBg,
                    border = BorderStroke(1.dp, if (isSelected) NeonCyan else SpacePanelBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = null,
                            tint = if (isSelected) NeonCyan else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tab.title,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp,
                            color = if (isSelected) NeonCyan else TextWhite
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Tab Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            when (selectedTab) {
                ModuleTab.VOICE -> {
                    // MODULE 3: Voice Emotion Analyzer
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("MODULE 3: VOICE EMOTION ANALYZER", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(10.dp))

                            VoiceSpectrogram(height = 72.dp, isActive = isRecordingVoice)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Speech Energy: ${selectedCrew.speechEnergy}", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = TextWhite)
                                Text("Pitch Stability: ${selectedCrew.pitchStability}%", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = BioGreen, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Emotional State Decomposition:", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = TextMuted)
                            Spacer(modifier = Modifier.height(6.dp))
                            EmotionBar("Happy / Optimistic", selectedCrew.emotionHappy, BioGreen)
                            EmotionBar("Neutral / Focused", selectedCrew.emotionNeutral, NeonCyan)
                            EmotionBar("Stress / Tension", selectedCrew.emotionStress, SolarYellow)
                            EmotionBar("Frustration / Anger", selectedCrew.emotionAnger, AlertRed)

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(color = SpaceDarkBg, shape = RoundedCornerShape(6.dp), modifier = Modifier.fillMaxWidth()) {
                                Text(text = voiceResult, fontSize = 10.5.sp, color = TextSilver, modifier = Modifier.padding(10.dp))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { viewModel.triggerVoiceAnalysis() },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = SpaceDarkBg),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isRecordingVoice) "RECORDING & ANALYZING VOCAL BIOMARKERS..." else "RECORD 3-SECOND VOCAL STRESS CHECK",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                ModuleTab.EYE -> {
                    // MODULE 4: Neuro-Ocular Assessment (SANS)
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("MODULE 4: NEURO-OCULAR ASSESSMENT (SANS)", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Spaceflight-Associated Neuro-ocular Syndrome (SANS) continuous optical coherence monitoring.",
                                fontSize = 11.sp,
                                color = TextSilver
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                EyeMetricTile(modifier = Modifier.weight(1f), label = "VISION SCORE", value = "${selectedCrew.visionScore}%", color = BioGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                EyeMetricTile(modifier = Modifier.weight(1f), label = "SANS RISK", value = selectedCrew.sansRisk, color = if (selectedCrew.sansRisk == "LOW") BioGreen else SolarYellow)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                EyeMetricTile(modifier = Modifier.weight(1f), label = "RETINAL PRESSURE", value = selectedCrew.retinalPressure, color = BioGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                EyeMetricTile(modifier = Modifier.weight(1f), label = "PUPIL RESPONSE", value = selectedCrew.pupilResponse, color = NeonCyan)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(color = SpaceDarkBg, shape = RoundedCornerShape(6.dp), modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("OPTICAL ACUITY & FIXATION DRIFT TEST:", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = NeonCyan)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("• Dynamic saccadic gaze latency: 198 ms (Within nominal 180-220ms band)", fontSize = 10.5.sp, color = TextSilver)
                                    Text("• Optic disc edema: None detected", fontSize = 10.5.sp, color = BioGreen)
                                    Text("• Choroidal folds index: 0.02 mm (Baseline nominal)", fontSize = 10.5.sp, color = TextSilver)
                                }
                            }
                        }
                    }
                }

                ModuleTab.EXERCISE -> {
                    // MODULE 5: Exercise & Physical Recovery
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("MODULE 5: EXERCISE & PHYSICAL RECOVERY", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("TODAY'S WORKOUT TARGET", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
                                    Text("${selectedCrew.exerciseCompletedMin} / ${selectedCrew.exerciseGoalMin} min", fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextWhite)
                                    Text("Calories Burned: ${selectedCrew.caloriesBurned} kcal", fontSize = 11.sp, color = BioGreen)
                                    Text("Muscle Load: ${selectedCrew.muscleLoad}", fontSize = 11.sp, color = NeonCyan)
                                }
                                CircularHealthGauge(
                                    percentage = (selectedCrew.exerciseCompletedMin * 100) / selectedCrew.exerciseGoalMin,
                                    label = "Target",
                                    size = 72.dp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("SELECT EXERCISE DEVICE:", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ExerciseDeviceChip(name = "ARED (Resistive)", isSelected = exerciseDevice == "ARED") { viewModel.activeExerciseDevice.value = "ARED" }
                                ExerciseDeviceChip(name = "T2 Treadmill", isSelected = exerciseDevice == "T2 Treadmill") { viewModel.activeExerciseDevice.value = "T2 Treadmill" }
                                ExerciseDeviceChip(name = "CEVIS Bike", isSelected = exerciseDevice == "CEVIS Bike") { viewModel.activeExerciseDevice.value = "CEVIS Bike" }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(color = SpaceDarkBg, shape = RoundedCornerShape(6.dp), modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("COUNTERMEASURE PROTOCOL:", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = NeonCyan)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("NASA microgravity bone-mineral and cardiac preservation: 60-min daily target split between high-load eccentric squats on ARED and interval sprints on T2 harness.", fontSize = 10.5.sp, color = TextSilver)
                                }
                            }
                        }
                    }
                }

                ModuleTab.CIRCADIAN -> {
                    // MODULE 6: Circadian Rhythm Controller
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("MODULE 6: CIRCADIAN RHYTHM CONTROLLER", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Dynamic Spacecraft LED Spectrum Tuning. Optimizes melatonin suppression in wake cycle and facilitates delta sleep during rest cycle.",
                                fontSize = 11.sp,
                                color = TextSilver
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Spectrum Color Temperature:", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = TextWhite)
                                Text("${spectrumK}K", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeonCyan)
                            }

                            Slider(
                                value = spectrumK.toFloat(),
                                onValueChange = { viewModel.lightSpectrumKelvin.value = it.toInt() },
                                valueRange = 2200f..6500f,
                                colors = SliderDefaults.colors(
                                    thumbColor = NeonCyan,
                                    activeTrackColor = NeonCyan,
                                    inactiveTrackColor = SpacePanelBorder
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                CircadianModeButton(
                                    name = "MORNING (6500K)",
                                    isActive = lightingMode == "Morning",
                                    color = NeonCyan
                                ) { viewModel.setCircadianMode("Morning", 6500) }

                                CircadianModeButton(
                                    name = "EVENING (3500K)",
                                    isActive = lightingMode == "Evening",
                                    color = SolarYellow
                                ) { viewModel.setCircadianMode("Evening", 3500) }

                                CircadianModeButton(
                                    name = "SLEEP (2200K)",
                                    isActive = lightingMode == "Sleep",
                                    color = AlertRed
                                ) { viewModel.setCircadianMode("Sleep", 2200) }
                            }
                        }
                    }
                }

                ModuleTab.RADIATION -> {
                    // MODULE 7: Radiation Monitoring
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("MODULE 7: RADIATION MONITORING CENTER", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Current Spacecraft Exposure", fontSize = 11.sp, color = TextMuted)
                                Text("${environment.radiationMsv} mSv", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BioGreen)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (environment.radiationMsv / 2.0f).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = BioGreen,
                                trackColor = SpaceDarkBg
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text("PERSONAL CREW DOSIMETERS (ACTIVE):", fontFamily = FontFamily.Monospace, fontSize = 10.5.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(6.dp))

                            DosimeterRow("Commander Alex Chen", "0.34 mSv", "Nominal", BioGreen)
                            DosimeterRow("Pilot Sarah Khan", "0.32 mSv", "Nominal", BioGreen)
                            DosimeterRow("Specialist Michael Ross", "0.35 mSv", "Nominal", BioGreen)
                            DosimeterRow("Science Officer Emma Li", "0.33 mSv", "Nominal", BioGreen)
                            DosimeterRow("Flight Engineer David Kim", "0.36 mSv", "Nominal", BioGreen)
                        }
                    }
                }

                ModuleTab.DIAGNOSTICS -> {
                    // MODULE 8: In-Flight Diagnostics Lab
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("MODULE 8: IN-FLIGHT DIAGNOSTICS LAB", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(10.dp))

                            Text("Butterfly iQ Portable Ultrasound & Bio-Fluid Lab on chip telemetry.", fontSize = 11.sp, color = TextSilver)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                LabItem("Blood Analysis", "Normal (Electrolytes, Troponin Negative)", BioGreen)
                                LabItem("Urinalysis", "Normal (Specific Gravity 1.018)", BioGreen)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                LabItem("Inflammation (hs-CRP)", "0.8 mg/L (Low)", BioGreen)
                                LabItem("Immune Status", "Healthy / Balanced", BioGreen)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(color = SpaceDarkBg, shape = RoundedCornerShape(6.dp), modifier = Modifier.fillMaxWidth()) {
                                Text(text = scanResult, fontSize = 10.5.sp, color = TextSilver, modifier = Modifier.padding(10.dp))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    viewModel.lastScanResult.value = "Ultrasound Scan Verified: Sol 178 complete. Cardiac stroke volume: 74 ml. Subclavian vein laminar flow nominal."
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = SpaceDarkBg),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("RUN ULTRASOUND CALIBRATION", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }

                ModuleTab.ENVIRONMENT -> {
                    // MODULE 9: Cabin Environment
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("MODULE 9: CABIN ENVIRONMENTAL HEALTH (ECLSS)", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(12.dp))

                            EnvRow("Hab Air Quality", "${environment.airQualityPercent}%", BioGreen)
                            EnvRow("Potable Water Recycler Quality", "${environment.waterQualityPercent}%", BioGreen)
                            EnvRow("CO₂ Sabatier Loop Scrubber", "${environment.co2Ppm} ppm", BioGreen)
                            EnvRow("Cabin Microclimate Temperature", "${environment.cabinTempC}°C", NeonCyan)
                            EnvRow("Relative Humidity", "${environment.humidityPercent}%", NeonCyan)
                            EnvRow("Microbial Surface Contamination", environment.microbialRisk, BioGreen)
                            EnvRow("Life Support Primary Loop", environment.lifeSupportStatus, BioGreen)
                        }
                    }
                }

                ModuleTab.CRISIS_ENGINE -> {
                    // MODULE 11: 72-Hour Predictive Crisis Engine
                    val assessments by viewModel.latestAssessments.collectAsState()

                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text("MODULE 11: 72-HOUR PREDICTIVE CRISIS ENGINE", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(6.dp))

                            Text("AI Engine processing continuous HR & HRV telemetry to flag fatigue or stress anomalies across crew.", fontSize = 11.sp, color = TextSilver)

                            Spacer(modifier = Modifier.height(14.dp))

                            RiskForecastRow("Mental Crisis Risk", "${predictiveRisk.mentalRisk}%", (predictiveRisk.mentalRisk / 100f).coerceIn(0f, 1f), BioGreen)
                            RiskForecastRow("Cognitive Fatigue Risk", "${predictiveRisk.fatigueRisk}%", (predictiveRisk.fatigueRisk / 100f).coerceIn(0f, 1f), SolarYellow)
                            RiskForecastRow("Interpersonal Conflict Risk", "${predictiveRisk.conflictRisk}%", (predictiveRisk.conflictRisk / 100f).coerceIn(0f, 1f), SolarYellow)
                            RiskForecastRow("Overall Mission Performance", "${predictiveRisk.missionPerformance}%", (predictiveRisk.missionPerformance / 100f).coerceIn(0f, 1f), BioGreen)

                            Spacer(modifier = Modifier.height(14.dp))

                            Text("INDIVIDUAL CREW ANOMALY DETECTIONS:", fontFamily = FontFamily.Monospace, fontSize = 10.5.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(6.dp))

                            assessments.values.forEach { assessment ->
                                val assessmentColor = when (assessment.severity) {
                                    com.example.data.AlertSeverity.CRITICAL -> AlertRed
                                    com.example.data.AlertSeverity.ELEVATED -> SolarYellow
                                    com.example.data.AlertSeverity.ADVISORY -> BioGreen
                                }

                                Surface(
                                    color = SpaceDarkBg,
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, assessmentColor.copy(alpha = 0.4f)),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = assessment.crewName,
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.5.sp,
                                                color = TextWhite
                                            )
                                            Surface(
                                                color = assessmentColor.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = if (assessment.isAnomaly) assessment.anomalyType.label.uppercase() else "NOMINAL",
                                                    fontSize = 8.5.sp,
                                                    fontFamily = FontFamily.Monospace,
                                                    fontWeight = FontWeight.Bold,
                                                    color = assessmentColor,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Fatigue: ${assessment.fatigueIndex}% • Stress: ${assessment.stressScore}%",
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 10.sp,
                                                color = TextSilver
                                            )
                                            Text(
                                                text = "ANS Ratio: ${assessment.ansBalanceRatio}",
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 9.5.sp,
                                                color = NeonCyan
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "Triggers: ${assessment.primaryTriggers.take(2).joinToString(" • ")}",
                                            fontSize = 9.sp,
                                            color = TextMuted
                                        )
                                        if (assessment.isAnomaly) {
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Rx: ${assessment.recommendedClinicalAction}",
                                                fontSize = 9.5.sp,
                                                color = assessmentColor
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(color = SpaceDarkBg, shape = RoundedCornerShape(6.dp), modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("TIMELINE FORECAST HORIZON:", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = NeonCyan)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("• 0-24h (Now): Continuous ANS tracking active across 5 wearable vests", fontSize = 10.5.sp, color = TextSilver)
                                    Text("• 24-48h: Dynamic workload balancing to mitigate cumulative fatigue", fontSize = 10.5.sp, color = BioGreen)
                                    Text("• 48-72h: EVA Sol 179 clearance nominal for Alex Chen & Sarah Khan", fontSize = 10.5.sp, color = BioGreen)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmotionBar(label: String, percent: Int, color: Color) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, fontSize = 10.sp, color = TextWhite)
            Text(text = "$percent%", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = SpaceDarkBg
        )
    }
}

@Composable
private fun EyeMetricTile(modifier: Modifier, label: String, value: String, color: Color) {
    Surface(
        modifier = modifier,
        color = SpaceDarkBg,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = label, fontSize = 8.5.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = color)
        }
    }
}

@Composable
private fun ExerciseDeviceChip(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) BioGreen.copy(alpha = 0.2f) else SpaceDarkBg,
        border = BorderStroke(1.dp, if (isSelected) BioGreen else SpacePanelBorder)
    ) {
        Text(
            text = name,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            color = if (isSelected) BioGreen else TextWhite,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun CircadianModeButton(name: String, isActive: Boolean, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) color else SpaceDarkBg,
            contentColor = if (isActive) SpaceDarkBg else color
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(text = name, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DosimeterRow(name: String, dose: String, status: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, fontSize = 10.5.sp, color = TextWhite)
        Row {
            Text(text = dose, fontFamily = FontFamily.Monospace, fontSize = 10.5.sp, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "($status)", fontSize = 9.5.sp, color = TextMuted)
        }
    }
}

@Composable
private fun LabItem(title: String, status: String, color: Color) {
    Column {
        Text(text = title, fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
        Text(text = status, fontSize = 10.5.sp, color = color, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun EnvRow(metric: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = metric, fontSize = 11.sp, color = TextWhite)
        Text(text = value, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = color)
    }
}

@Composable
private fun RiskForecastRow(title: String, value: String, fraction: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = title, fontSize = 11.sp, color = TextWhite)
            Text(text = value, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(2.5.dp)),
            color = color,
            trackColor = SpaceDarkBg
        )
    }
}
