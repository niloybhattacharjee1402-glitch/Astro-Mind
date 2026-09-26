package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AlertCategory
import com.example.data.AlertSeverity
import com.example.data.AnomalyType
import com.example.data.ChatMessage
import com.example.data.CrewMember
import com.example.data.CrewStatus
import com.example.data.HealthMetricsSample
import com.example.data.InterventionStatus
import com.example.data.MissionAlert
import com.example.data.PersonalizedIntervention
import com.example.data.PredictiveRisk72h
import com.example.data.RiskAnomalyAssessment
import com.example.data.SpacecraftEnvironment
import com.example.data.TeamChallenge
import com.example.data.engine.PredictiveRiskEngine
import com.example.data.gemini.AstroMindAiService
import com.example.data.local.AstroMindDatabase
import com.example.data.local.InterventionEntity
import com.example.data.local.MissionAlertEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AstroMindViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AstroMindDatabase.getDatabase(application)
    private val dao = db.dao()
    private val aiService = AstroMindAiService()
    val predictiveRiskEngine = PredictiveRiskEngine()

    private val _latestAssessments = MutableStateFlow<Map<String, RiskAnomalyAssessment>>(emptyMap())
    val latestAssessments: StateFlow<Map<String, RiskAnomalyAssessment>> = _latestAssessments.asStateFlow()

    // 5 Deep Space Crew Members (As shown in reference UI)
    private val _crewMembers = MutableStateFlow<List<CrewMember>>(
        listOf(
            CrewMember(
                id = "alex_chen",
                name = "Alex Chen",
                role = "Commander",
                avatarEmoji = "👨🚀",
                status = CrewStatus.STABLE,
                heartRate = 72,
                hrv = 64,
                spO2 = 98,
                respiration = 14,
                bodyTemp = 36.7f,
                stressScore = 32,
                fatigueIndex = 38,
                mood = "Positive",
                burnoutRisk = 12,
                isolationScore = 18,
                sleepQuality = 89,
                sleepHours = 7.8f,
                visionScore = 96,
                sansRisk = "LOW",
                exerciseCompletedMin = 42,
                exerciseGoalMin = 60,
                caloriesBurned = 420
            ),
            CrewMember(
                id = "sarah_khan",
                name = "Sarah Khan",
                role = "Pilot",
                avatarEmoji = "👩🚀",
                status = CrewStatus.STABLE,
                heartRate = 68,
                hrv = 71,
                spO2 = 99,
                respiration = 13,
                bodyTemp = 36.6f,
                stressScore = 28,
                fatigueIndex = 30,
                mood = "Positive",
                burnoutRisk = 10,
                isolationScore = 15,
                sleepQuality = 92,
                sleepHours = 8.1f,
                visionScore = 98,
                sansRisk = "LOW",
                exerciseCompletedMin = 50,
                exerciseGoalMin = 60,
                caloriesBurned = 460
            ),
            CrewMember(
                id = "michael_ross",
                name = "Michael Ross",
                role = "Mission Specialist",
                avatarEmoji = "👨🚀",
                status = CrewStatus.FATIGUE,
                heartRate = 79,
                hrv = 52,
                spO2 = 97,
                respiration = 16,
                bodyTemp = 36.9f,
                stressScore = 48,
                fatigueIndex = 61,
                mood = "Fatigued",
                burnoutRisk = 28,
                isolationScore = 22,
                sleepQuality = 74,
                sleepHours = 6.2f,
                visionScore = 95,
                sansRisk = "LOW",
                exerciseCompletedMin = 30,
                exerciseGoalMin = 60,
                caloriesBurned = 310
            ),
            CrewMember(
                id = "emma_li",
                name = "Emma Li",
                role = "Science Officer",
                avatarEmoji = "👩🚀",
                status = CrewStatus.STABLE,
                heartRate = 66,
                hrv = 76,
                spO2 = 99,
                respiration = 12,
                bodyTemp = 36.5f,
                stressScore = 22,
                fatigueIndex = 25,
                mood = "Positive",
                burnoutRisk = 8,
                isolationScore = 14,
                sleepQuality = 94,
                sleepHours = 8.0f,
                visionScore = 99,
                sansRisk = "LOW",
                exerciseCompletedMin = 45,
                exerciseGoalMin = 60,
                caloriesBurned = 390
            ),
            CrewMember(
                id = "david_kim",
                name = "David Kim",
                role = "Flight Engineer",
                avatarEmoji = "👨🚀",
                status = CrewStatus.HIGH_STRESS,
                heartRate = 88,
                hrv = 41,
                spO2 = 96,
                respiration = 18,
                bodyTemp = 37.1f,
                stressScore = 71,
                fatigueIndex = 78,
                mood = "High Stress",
                burnoutRisk = 44,
                isolationScore = 34,
                sleepQuality = 62,
                sleepHours = 5.2f,
                visionScore = 93,
                sansRisk = "ELEVATED",
                exerciseCompletedMin = 20,
                exerciseGoalMin = 60,
                caloriesBurned = 210
            )
        )
    )
    val crewMembers: StateFlow<List<CrewMember>> = _crewMembers.asStateFlow()

    private val _selectedCrewId = MutableStateFlow("alex_chen")
    val selectedCrewId: StateFlow<String> = _selectedCrewId.asStateFlow()

    // Environment Telemetry
    private val _environment = MutableStateFlow(SpacecraftEnvironment())
    val environment: StateFlow<SpacecraftEnvironment> = _environment.asStateFlow()

    // Predictive 72-Hour Risk Engine
    private val _predictiveRisk = MutableStateFlow(PredictiveRisk72h())
    val predictiveRisk: StateFlow<PredictiveRisk72h> = _predictiveRisk.asStateFlow()

    // Sol and UTC clock
    val sol = MutableStateFlow(178)
    private val _utcTime = MutableStateFlow("15:24:36")
    val utcTime: StateFlow<String> = _utcTime.asStateFlow()

    // AI Status (Offline On-Device vs Cloud Gemini)
    private val _isOfflineAi = MutableStateFlow(true)
    val isOfflineAi: StateFlow<Boolean> = _isOfflineAi.asStateFlow()

    // Circadian Control State
    val circadianPhase = MutableStateFlow("Wake Cycle")
    val lightSpectrumKelvin = MutableStateFlow(6500)
    val activeLightingMode = MutableStateFlow("Morning") // Morning, Evening, Sleep

    // Module 1 Wearable Vest connection state
    val wearableVestConnected = MutableStateFlow(true)

    // Module 3 Voice Analytics state
    val isRecordingVoice = MutableStateFlow(false)
    val voiceAnalysisResult = MutableStateFlow("Pitch stability 95% • Vocal fatigue biomarkers nominal • Autonomic stress index: 12%")

    // Module 5 Exercise system state
    val activeExerciseDevice = MutableStateFlow("ARED") // ARED, T2 Treadmill, CEVIS Bike

    // Module 8 Diagnostics Lab
    val lastScanResult = MutableStateFlow("Ultrasound (Butterfly iQ) Completed • No visceral fluid or cardiac decompensation detected")

    // Feature 1: Closed-Loop AI-Driven Personalized Interventions
    private val _interventions = MutableStateFlow<List<PersonalizedIntervention>>(
        listOf(
            PersonalizedIntervention(
                id = "int_david_01",
                astronautId = "david_kim",
                astronautName = "David Kim",
                detectedRisk = "HIGH FATIGUE & COGNITIVE STRAIN",
                triggerMetrics = listOf(
                    "Fatigue 78% (Elevated)",
                    "Sleep 5.2 hrs (Deficit)",
                    "HRV 41 ms (Sympathetic Dominance)",
                    "Stress 71% (Acute)"
                ),
                actionPlan = listOf(
                    "Reduce non-critical engineering maintenance workload",
                    "Mandatory 20-minute neuro-acoustic acoustic recovery session",
                    "Hydration reminder (500ml electrolyte replenishment)",
                    "Shift circadian light to 4500K intermediate spectrum",
                    "Sleep priority for next Sol rest cycle (8+ hours planned)"
                ),
                followUpProtocol = "Monitor continuous HRV + Fatigue index every 2 hours (6-hour window)",
                status = InterventionStatus.PROPOSED,
                followUpHoursTotal = 6,
                followUpHoursElapsed = 1.4f
            ),
            PersonalizedIntervention(
                id = "int_alex_02",
                astronautId = "alex_chen",
                astronautName = "Alex Chen",
                detectedRisk = "PRE-EVA AUTONOMIC PREPARATION",
                triggerMetrics = listOf(
                    "EVA Scheduled Sol 179",
                    "Autonomic tone: Stable",
                    "HRV: 64 ms"
                ),
                actionPlan = listOf(
                    "10-minute guided 4-4-6 paced breathing session",
                    "Pre-suit joint mobility calibration",
                    "Dosimeter baseline calibration check"
                ),
                followUpProtocol = "Pre-breathe nitrogen scrub clearance confirmation",
                status = InterventionStatus.ACCEPTED,
                followUpHoursTotal = 2,
                followUpHoursElapsed = 0.5f
            )
        )
    )
    val interventions: StateFlow<List<PersonalizedIntervention>> = _interventions.asStateFlow()

    // Feature 2: Integrated Mission Alert System (Mission Guardian)
    private val _missionAlerts = MutableStateFlow<List<MissionAlert>>(
        listOf(
            MissionAlert(
                id = "alert_rad_01",
                title = "MULTI-SENSOR CORRELATION: SOLAR FLUX ELEVATION",
                severity = AlertSeverity.CRITICAL,
                category = AlertCategory.RADIATION,
                description = "Cosmic ray spectrometer and external hull dosimeters detected transient solar flare particle density increase. Correlated with flight trajectory near Mars orbital magnetic anomaly.",
                multiSensorCorrelation = "Radiation Sensor: 0.34 mSv/h (Elevated) + Solar Activity: Moderate Flare Alert + Exterior Hull Telemetry: High Ion Flux",
                recommendedActions = listOf(
                    "Transition crew to primary storm shelter (Hab Module Central Core)",
                    "Hold all EVA egress preparations immediately",
                    "Verify personal dosimeter sync for all 5 astronauts",
                    "Transmit telemetry burst to Houston Mission Control (Earth)"
                ),
                isResolved = false,
                timestamp = "SOL 178 | 15:10 UTC"
            ),
            MissionAlert(
                id = "alert_env_02",
                title = "ECLSS CO2 SYNERGISTIC HEALTH WATCH",
                severity = AlertSeverity.ELEVATED,
                category = AlertCategory.LIFE_SUPPORT,
                description = "Hab Module CO2 transient peak correlated with David Kim's elevated stress respiration rate (18/min).",
                multiSensorCorrelation = "CO2: 520 ppm + Crew Respiration Peak + Workload Spike",
                recommendedActions = listOf(
                    "Boost Life Support Sabatier scrubber loop B to 110%",
                    "Verify cabin ventilation baffle seals in Engineering bay",
                    "Hydration check for David Kim and Michael Ross"
                ),
                isResolved = false,
                timestamp = "SOL 178 | 14:45 UTC"
            )
        )
    )
    val missionAlerts: StateFlow<List<MissionAlert>> = _missionAlerts.asStateFlow()

    // Feature 3: Dynamic Team-Building AI
    private val _teamChallenges = MutableStateFlow<List<TeamChallenge>>(
        listOf(
            TeamChallenge(
                id = "team_ch_01",
                title = "Mars Orbital Emergency Docking Simulation",
                participants = listOf("Sarah Khan", "David Kim", "Emma Li"),
                durationMinutes = 25,
                objective = "Joint manual alignment scenario of cargo transfer vehicle in high-latency sim.",
                expectedBenefits = listOf(
                    "Enhance verbal communication cadence between Sarah & David",
                    "Build cognitive trust during critical engineering contingencies",
                    "Equalize high-stress operational workload"
                ),
                scheduleRecommendation = "After current Sol duty cycle (19:00 UTC)",
                isAccepted = false
            ),
            TeamChallenge(
                id = "team_ch_02",
                title = "Earth Connection & Orbital Observation",
                participants = listOf("Alex Chen", "Michael Ross", "David Kim", "Sarah Khan", "Emma Li"),
                durationMinutes = 30,
                objective = "Shared Earth high-res observation debrief & family video message uplink.",
                expectedBenefits = listOf(
                    "Alleviate deep space psychosocial isolation (-24%)",
                    "Strengthen collective mission morale and group cohesion",
                    "Facilitate informal crew debrief"
                ),
                scheduleRecommendation = "Pre-sleep recreation window (21:30 UTC)",
                isAccepted = true
            )
        )
    )
    val teamChallenges: StateFlow<List<TeamChallenge>> = _teamChallenges.asStateFlow()

    // AstroMind AI Companion Chat
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "msg_0",
                sender = "AI",
                text = "Good morning Commander Alex. Your sleep quality improved by 8% (89% score, 7.8 hrs). Spacecraft radiation levels are nominal at 0.34 mSv. I recommend a 10-minute guided breathing session before tomorrow's scheduled EVA.",
                timestamp = "08:15 UTC"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()
    val isAiThinking = MutableStateFlow(false)

    init {
        startTelemetrySimulation()
        loadPersistedData()
    }

    private fun loadPersistedData() {
        viewModelScope.launch {
            try {
                // Populate initial DB records if empty
                val alertsEntity = MissionAlertEntity(
                    id = "alert_rad_01",
                    title = "MULTI-SENSOR: SOLAR FLUX ELEVATION",
                    severity = "CRITICAL",
                    category = "RADIATION",
                    description = "Solar particle density increase detected near orbital boundary.",
                    correlation = "Radiation 0.34 mSv/h + Solar Active",
                    actionsRaw = "Move crew to shelter\nPause EVA\nMonitor dosimeters",
                    isResolved = false,
                    timestamp = "SOL 178"
                )
                dao.insertAlert(alertsEntity)
            } catch (e: Exception) {
                // non-blocking
            }
        }
    }

    private fun startTelemetrySimulation() {
        viewModelScope.launch {
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
            while (true) {
                delay(2500)
                _utcTime.value = timeFormat.format(Date())

                // Slight realistic fluctuation in heart rates and HRV
                val updatedCrew = _crewMembers.value.map { member ->
                    val deltaHr = (-1..1).random()
                    val newHr = (member.heartRate + deltaHr).coerceIn(58, 110)
                    val deltaHrv = (-1..1).random()
                    val newHrv = (member.hrv + deltaHrv).coerceIn(35, 95)
                    member.copy(heartRate = newHr, hrv = newHrv)
                }

                // Process every astronaut's live telemetry through the Predictive Risk Engine
                val newAssessments = mutableMapOf<String, RiskAnomalyAssessment>()
                val finalCrew = updatedCrew.map { member ->
                    val sample = HealthMetricsSample(
                        crewId = member.id,
                        heartRate = member.heartRate,
                        hrv = member.hrv,
                        spO2 = member.spO2,
                        respiration = member.respiration,
                        bodyTemp = member.bodyTemp,
                        sleepQuality = member.sleepQuality,
                        sleepHours = member.sleepHours
                    )
                    val assessment = predictiveRiskEngine.processSample(sample, member.name)
                    newAssessments[member.id] = assessment

                    val newStatus = when {
                        assessment.severity == AlertSeverity.CRITICAL -> CrewStatus.HIGH_STRESS
                        assessment.anomalyType == AnomalyType.FATIGUE_ACCUMULATION || assessment.fatigueIndex >= 55 -> CrewStatus.FATIGUE
                        assessment.anomalyType == AnomalyType.ACUTE_STRESS_SPIKE || assessment.stressScore >= 50 -> CrewStatus.HIGH_STRESS
                        else -> CrewStatus.STABLE
                    }

                    member.copy(
                        fatigueIndex = assessment.fatigueIndex,
                        stressScore = assessment.stressScore,
                        status = newStatus
                    )
                }

                _crewMembers.value = finalCrew
                _latestAssessments.value = newAssessments

                // Dynamically update the 72h Predictive Risk Forecast from individual assessments
                _predictiveRisk.value = predictiveRiskEngine.calculate72hForecast(newAssessments.values)

                val curEnv = _environment.value
                val newCo2 = (curEnv.co2Ppm + (-2..3).random()).coerceIn(490, 560)
                _environment.value = curEnv.copy(co2Ppm = newCo2)
            }
        }
    }

    fun selectCrewMember(id: String) {
        _selectedCrewId.value = id
    }

    fun getSelectedCrew(): CrewMember {
        return _crewMembers.value.find { it.id == _selectedCrewId.value }
            ?: _crewMembers.value.first()
    }

    fun toggleAiMode() {
        _isOfflineAi.value = !_isOfflineAi.value
    }

    fun setCircadianMode(mode: String, spectrumK: Int) {
        activeLightingMode.value = mode
        lightSpectrumKelvin.value = spectrumK
        circadianPhase.value = when (mode) {
            "Morning" -> "Wake Cycle"
            "Evening" -> "Pre-Sleep Shift"
            else -> "Deep Rest Cycle"
        }
    }

    fun toggleWearableVest() {
        wearableVestConnected.value = !wearableVestConnected.value
    }

    fun triggerVoiceAnalysis() {
        viewModelScope.launch {
            isRecordingVoice.value = true
            delay(2800)
            isRecordingVoice.value = false
            voiceAnalysisResult.value = "Pitch stability 96% • Vocal energy: Normal • Stress markers: Low • Emotion: 55% Confident, 35% Calm, 10% Fatigued"
        }
    }

    fun acceptIntervention(interventionId: String) {
        _interventions.value = _interventions.value.map { intv ->
            if (intv.id == interventionId) {
                intv.copy(status = InterventionStatus.ACCEPTED)
            } else intv
        }
        viewModelScope.launch {
            val target = _interventions.value.find { it.id == interventionId }
            if (target != null) {
                dao.insertIntervention(
                    InterventionEntity(
                        id = target.id,
                        astronautId = target.astronautId,
                        astronautName = target.astronautName,
                        detectedRisk = target.detectedRisk,
                        triggerMetricsRaw = target.triggerMetrics.joinToString(","),
                        actionPlanRaw = target.actionPlan.joinToString("\n"),
                        followUpProtocol = target.followUpProtocol,
                        status = "ACCEPTED",
                        followUpHoursTotal = target.followUpHoursTotal,
                        followUpHoursElapsed = target.followUpHoursElapsed
                    )
                )
            }
        }
    }

    fun advanceInterventionProgress(interventionId: String) {
        _interventions.value = _interventions.value.map { intv ->
            if (intv.id == interventionId) {
                val newStatus = when (intv.status) {
                    InterventionStatus.PROPOSED -> InterventionStatus.ACCEPTED
                    InterventionStatus.ACCEPTED -> InterventionStatus.IN_PROGRESS
                    InterventionStatus.IN_PROGRESS -> InterventionStatus.COMPLETED
                    InterventionStatus.COMPLETED -> InterventionStatus.COMPLETED
                }
                intv.copy(
                    status = newStatus,
                    followUpHoursElapsed = (intv.followUpHoursElapsed + 1.5f).coerceAtMost(intv.followUpHoursTotal.toFloat())
                )
            } else intv
        }
    }

    fun resolveAlert(alertId: String) {
        _missionAlerts.value = _missionAlerts.value.map { alert ->
            if (alert.id == alertId) alert.copy(isResolved = true) else alert
        }
        viewModelScope.launch {
            dao.resolveAlert(alertId)
        }
    }

    fun acceptTeamChallenge(challengeId: String) {
        _teamChallenges.value = _teamChallenges.value.map { ch ->
            if (ch.id == challengeId) ch.copy(isAccepted = !ch.isAccepted) else ch
        }
    }

    fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = "ASTRONAUT",
            text = userText,
            timestamp = _utcTime.value
        )
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            isAiThinking.value = true
            val aiResponse = aiService.generateMissionAdvice(
                prompt = userText,
                isOfflineMode = _isOfflineAi.value
            )
            isAiThinking.value = false

            val aiMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                sender = "AI",
                text = aiResponse,
                timestamp = _utcTime.value,
                isEmergencyGuidance = userText.lowercase().contains("emergency") || userText.lowercase().contains("sos")
            )
            _chatMessages.value = _chatMessages.value + aiMsg
        }
    }
}
