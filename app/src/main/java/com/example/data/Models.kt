package com.example.data

enum class CrewStatus(val label: String, val colorHex: Long) {
    STABLE("Stable", 0xFF00FF88),
    FATIGUE("Fatigue", 0xFFFFD600),
    HIGH_STRESS("High Stress", 0xFFFF5252),
    CRITICAL("Critical", 0xFFFF2222)
}

enum class AlertSeverity(val label: String, val colorHex: Long) {
    CRITICAL("CRITICAL", 0xFFFF5252),
    ELEVATED("ELEVATED", 0xFFFFD600),
    ADVISORY("ADVISORY", 0xFF00E5FF)
}

enum class AlertCategory {
    RADIATION, BIO_HEALTH, ENVIRONMENTAL, TEAM_CONFLICT, LIFE_SUPPORT
}

enum class InterventionStatus {
    PROPOSED, ACCEPTED, IN_PROGRESS, COMPLETED
}

data class CrewMember(
    val id: String,
    val name: String,
    val role: String,
    val avatarEmoji: String,
    val status: CrewStatus,
    val heartRate: Int = 72,
    val hrv: Int = 64,
    val spO2: Int = 98,
    val respiration: Int = 14,
    val bodyTemp: Float = 36.7f,
    val stressScore: Int = 32,
    val fatigueIndex: Int = 61,
    val mood: String = "Positive",
    val burnoutRisk: Int = 12,
    val isolationScore: Int = 18,
    val sleepQuality: Int = 89,
    val sleepHours: Float = 7.4f,
    val speechEnergy: String = "Normal",
    val pitchStability: Int = 95,
    val emotionHappy: Int = 45,
    val emotionNeutral: Int = 40,
    val emotionStress: Int = 10,
    val emotionAnger: Int = 5,
    val visionScore: Int = 96,
    val sansRisk: String = "LOW",
    val retinalPressure: String = "Normal",
    val pupilResponse: String = "Normal",
    val eyeMovement: String = "Stable",
    val exerciseCompletedMin: Int = 42,
    val exerciseGoalMin: Int = 60,
    val caloriesBurned: Int = 420,
    val muscleLoad: String = "Moderate",
    val dosimeterCurrentMsv: Float = 0.34f,
    val wearableVestConnected: Boolean = true
)

data class SpacecraftEnvironment(
    val co2Ppm: Int = 520,
    val o2Percent: Float = 21.0f,
    val cabinTempC: Float = 23.0f,
    val humidityPercent: Int = 48,
    val radiationMsv: Float = 0.34f,
    val airQualityPercent: Int = 95,
    val waterQualityPercent: Int = 98,
    val microbialRisk: String = "LOW",
    val cabinStatus: String = "SAFE",
    val lifeSupportStatus: String = "NOMINAL"
)

data class PredictiveRisk72h(
    val mentalRisk: Int = 12,
    val fatigueRisk: Int = 25,
    val conflictRisk: Int = 18,
    val missionPerformance: Int = 92,
    val status: String = "STABLE"
)

enum class AnomalyType(val label: String) {
    NONE("Nominal Autonomic State"),
    FATIGUE_ACCUMULATION("Fatigue Accumulation & Sleep Debt"),
    ACUTE_STRESS_SPIKE("Acute Sympathetic Stress Spike"),
    CARDIOVASCULAR_STRAIN("Cardiovascular Autonomic Strain"),
    COGNITIVE_OVERLOAD("Cognitive Exhaustion & ANS Imbalance")
}

data class HealthMetricsSample(
    val crewId: String,
    val heartRate: Int,
    val hrv: Int,
    val spO2: Int = 98,
    val respiration: Int = 14,
    val bodyTemp: Float = 36.7f,
    val sleepQuality: Int = 85,
    val sleepHours: Float = 7.5f,
    val timestamp: Long = System.currentTimeMillis()
)

data class RiskAnomalyAssessment(
    val crewId: String,
    val crewName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val fatigueIndex: Int,
    val stressScore: Int,
    val ansBalanceRatio: Float,
    val isAnomaly: Boolean,
    val anomalyType: AnomalyType,
    val severity: AlertSeverity,
    val confidenceScore: Float,
    val primaryTriggers: List<String>,
    val recommendedClinicalAction: String,
    val predictedNext24hFatigueSlope: Float = 0f
)

data class MissionAlert(
    val id: String,
    val title: String,
    val severity: AlertSeverity,
    val category: AlertCategory,
    val description: String,
    val multiSensorCorrelation: String,
    val recommendedActions: List<String>,
    val isResolved: Boolean = false,
    val timestamp: String = "SOL 178 | 14:32 UTC"
)

data class PersonalizedIntervention(
    val id: String,
    val astronautId: String,
    val astronautName: String,
    val detectedRisk: String,
    val triggerMetrics: List<String>,
    val actionPlan: List<String>,
    val followUpProtocol: String,
    var status: InterventionStatus = InterventionStatus.PROPOSED,
    var followUpHoursTotal: Int = 6,
    var followUpHoursElapsed: Float = 1.2f
)

data class TeamChallenge(
    val id: String,
    val title: String,
    val participants: List<String>,
    val durationMinutes: Int,
    val objective: String,
    val expectedBenefits: List<String>,
    val scheduleRecommendation: String,
    var isAccepted: Boolean = false
)

data class ChatMessage(
    val id: String,
    val sender: String, // "AI" or "ASTRONAUT"
    val text: String,
    val timestamp: String,
    val isEmergencyGuidance: Boolean = false
)
