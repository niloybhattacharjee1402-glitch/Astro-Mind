package com.example.data.engine

import com.example.data.AlertSeverity
import com.example.data.AnomalyType
import com.example.data.HealthMetricsSample
import com.example.data.InterventionStatus
import com.example.data.PersonalizedIntervention
import com.example.data.PredictiveRisk72h
import com.example.data.RiskAnomalyAssessment
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.roundToInt

data class CrewBaseline(
    val baselineRestingHr: Int = 68,
    val baselineHrv: Int = 68,
    val baselineSpO2: Int = 98,
    val baselineRespiration: Int = 13
)

/**
 * Predictive Risk Engine
 * Processes incoming real-time telemetry (Heart Rate, HRV, SpO2, Sleep, Respiration)
 * to flag potential fatigue or stress anomalies for individual crew members,
 * calculate sympathovagal autonomic tone, and trigger closed-loop interventions.
 */
class PredictiveRiskEngine {

    // Pre-configured individualized baseline profiles for the 5 Mars astronauts
    private val baselines = ConcurrentHashMap<String, CrewBaseline>().apply {
        put("alex_chen", CrewBaseline(baselineRestingHr = 70, baselineHrv = 66, baselineSpO2 = 98, baselineRespiration = 14))
        put("sarah_khan", CrewBaseline(baselineRestingHr = 66, baselineHrv = 72, baselineSpO2 = 99, baselineRespiration = 13))
        put("michael_ross", CrewBaseline(baselineRestingHr = 72, baselineHrv = 62, baselineSpO2 = 98, baselineRespiration = 14))
        put("emma_li", CrewBaseline(baselineRestingHr = 65, baselineHrv = 74, baselineSpO2 = 99, baselineRespiration = 12))
        put("david_kim", CrewBaseline(baselineRestingHr = 72, baselineHrv = 65, baselineSpO2 = 98, baselineRespiration = 14))
    }

    // Rolling history buffer (max 30 samples per crew member) for trend evaluation
    private val historyBuffer = ConcurrentHashMap<String, ArrayDeque<HealthMetricsSample>>()

    /**
     * Process an incoming health metric sample for a crew member.
     * Evaluates Heart Rate and HRV deviations from personalized baselines,
     * calculates Autonomic Load, and flags stress or fatigue anomalies.
     */
    fun processSample(
        sample: HealthMetricsSample,
        crewName: String
    ): RiskAnomalyAssessment {
        val crewId = sample.crewId
        val baseline = baselines[crewId] ?: CrewBaseline()

        // Append to rolling window buffer
        val buffer = historyBuffer.getOrPut(crewId) { ArrayDeque() }
        synchronized(buffer) {
            if (buffer.size >= 30) {
                buffer.removeFirst()
            }
            buffer.addLast(sample)
        }

        // 1. ANS Balance Ratio (Sympathovagal balance proxy: HR / HRV normalized)
        val hrFactor = sample.heartRate.toFloat() / baseline.baselineRestingHr.coerceAtLeast(40)
        val hrvFactor = sample.hrv.toFloat() / baseline.baselineHrv.coerceAtLeast(20)
        val ansBalanceRatio = (hrFactor / hrvFactor.coerceAtLeast(0.1f)).let {
            (it * 100f).roundToInt() / 100f
        }

        // 2. Stress Score computation (0-100)
        val stressScore = calculateStressScore(
            heartRateBpm = sample.heartRate,
            hrvMs = sample.hrv,
            respiration = sample.respiration,
            sleepQuality = sample.sleepQuality,
            baselineHrv = baseline.baselineHrv,
            baselineHr = baseline.baselineRestingHr,
            baselineRespiration = baseline.baselineRespiration
        )

        // 3. Fatigue Index computation (0-100)
        val fatigueIndex = calculateFatigueScore(
            heartRateBpm = sample.heartRate,
            hrvMs = sample.hrv,
            sleepHours = sample.sleepHours,
            sleepQuality = sample.sleepQuality,
            baselineHrv = baseline.baselineHrv,
            baselineHr = baseline.baselineRestingHr
        )

        // 4. Trend Slope (fatigue delta over sliding window)
        val fatigueSlope = synchronized(buffer) {
            if (buffer.size >= 3) {
                val oldest = buffer.first()
                val deltaHrv = sample.hrv - oldest.hrv
                // If HRV dropped, fatigue slope is positive (worsening)
                (-deltaHrv * 0.4f)
            } else 0f
        }

        // 5. Anomaly Detection Logic
        val triggers = mutableListOf<String>()

        val isFatigueAnomaly = fatigueIndex >= 55 || (sample.hrv < (baseline.baselineHrv * 0.75).toInt() && sample.sleepHours < 6.5f)
        val isStressAnomaly = stressScore >= 50 || (sample.heartRate >= 85 && sample.hrv <= 45)
        val isCardioAnomaly = sample.heartRate >= 95 || sample.spO2 < 95

        val isAnomaly = isFatigueAnomaly || isStressAnomaly || isCardioAnomaly

        val anomalyType = when {
            isCardioAnomaly -> AnomalyType.CARDIOVASCULAR_STRAIN
            isStressAnomaly && isFatigueAnomaly -> {
                if (stressScore > fatigueIndex) AnomalyType.ACUTE_STRESS_SPIKE else AnomalyType.FATIGUE_ACCUMULATION
            }
            isStressAnomaly -> AnomalyType.ACUTE_STRESS_SPIKE
            isFatigueAnomaly -> AnomalyType.FATIGUE_ACCUMULATION
            else -> AnomalyType.NONE
        }

        val severity = when {
            stressScore >= 70 || fatigueIndex >= 75 || sample.heartRate >= 95 || sample.spO2 <= 94 -> AlertSeverity.CRITICAL
            stressScore >= 50 || fatigueIndex >= 55 || sample.hrv < 50 -> AlertSeverity.ELEVATED
            else -> AlertSeverity.ADVISORY
        }

        // Build clinical trigger descriptions
        if (sample.heartRate > baseline.baselineRestingHr + 8) {
            triggers.add("Resting HR ${sample.heartRate} BPM (+${sample.heartRate - baseline.baselineRestingHr} vs baseline)")
        }
        if (sample.hrv < baseline.baselineHrv - 10) {
            triggers.add("HRV suppressed to ${sample.hrv} ms (${baseline.baselineHrv - sample.hrv} ms deficit)")
        }
        if (sample.sleepHours < 6.5f) {
            triggers.add("Sleep duration deficit: ${sample.sleepHours} hrs (Goal: 8.0 hrs)")
        }
        if (sample.respiration > baseline.baselineRespiration + 3) {
            triggers.add("Elevated respiration cadence: ${sample.respiration}/min")
        }
        if (ansBalanceRatio > 1.35f) {
            triggers.add("Sympathetic Autonomic Overactivity (ANS Ratio: $ansBalanceRatio)")
        }
        if (triggers.isEmpty()) {
            triggers.add("Autonomic markers within nominal physiological envelope")
        }

        val recommendedAction = when (anomalyType) {
            AnomalyType.FATIGUE_ACCUMULATION ->
                "Reassign secondary maintenance duties; schedule 20-min neuro-acoustic rest cycle & prioritize 8h sleep."
            AnomalyType.ACUTE_STRESS_SPIKE ->
                "Initiate immediate 5-minute paced breathing (4-4-6 cadence) and shift cabin spectrum to 3500K warm."
            AnomalyType.CARDIOVASCULAR_STRAIN ->
                "Hydration check (500ml electrolyte); check wearable vest 12-lead lead contact and verify SpO2."
            AnomalyType.COGNITIVE_OVERLOAD ->
                "Conduct 15-minute cognitive downtime and rebalance task allocation with Mission Specialist."
            AnomalyType.NONE ->
                "Maintain standard Sol schedule and scheduled exercise countermeasure protocols."
        }

        val confidenceScore = synchronized(buffer) {
            (0.70f + (buffer.size.coerceAtMost(20) / 20f) * 0.28f).coerceIn(0.70f, 0.99f)
        }

        return RiskAnomalyAssessment(
            crewId = crewId,
            crewName = crewName,
            timestamp = sample.timestamp,
            fatigueIndex = fatigueIndex,
            stressScore = stressScore,
            ansBalanceRatio = ansBalanceRatio,
            isAnomaly = isAnomaly,
            anomalyType = anomalyType,
            severity = severity,
            confidenceScore = (confidenceScore * 100f).roundToInt() / 100f,
            primaryTriggers = triggers,
            recommendedClinicalAction = recommendedAction,
            predictedNext24hFatigueSlope = fatigueSlope
        )
    }

    /**
     * Creates an automated PersonalizedIntervention instance if an anomaly is detected.
     */
    fun generateInterventionIfAnomaly(assessment: RiskAnomalyAssessment): PersonalizedIntervention? {
        if (!assessment.isAnomaly) return null

        val plan = when (assessment.anomalyType) {
            AnomalyType.FATIGUE_ACCUMULATION -> listOf(
                "Transfer non-critical engineering maintenance to available crew",
                "Mandatory 20-minute neuro-acoustic restorative quiet period",
                "Hydration reminder (500ml electrolyte solution)",
                "Shift circadian light to 3500K evening mode 60 min early",
                "Prioritize sleep cycle for next Sol (target 8+ hours)"
            )
            AnomalyType.ACUTE_STRESS_SPIKE -> listOf(
                "Execute 5-10 minute guided 4-4-6 autonomic paced breathing",
                "Audio relaxation protocol with acoustic binaural frequency",
                "Check-in conversation with AstroMind Clinical Assistant",
                "Review critical mission checklists with Commander"
            )
            AnomalyType.CARDIOVASCULAR_STRAIN -> listOf(
                "Pause high-intensity exercise or physical countermeasure load",
                "Conduct manual blood pressure & SpO2 validation check",
                "Hydration protocol with fluid replenishment",
                "Log ECG Lead II strip in In-Flight Diagnostics Lab"
            )
            else -> listOf(
                "Perform standard autonomic decompression routine",
                "Hydration and calorie intake verification",
                "Verify sensor dosimeter and environmental telemetry"
            )
        }

        return PersonalizedIntervention(
            id = "auto_int_${assessment.crewId}_${System.currentTimeMillis() % 10000}",
            astronautId = assessment.crewId,
            astronautName = assessment.crewName,
            detectedRisk = "${assessment.anomalyType.label.uppercase()} (${assessment.severity.name})",
            triggerMetrics = assessment.primaryTriggers,
            actionPlan = plan,
            followUpProtocol = "Continuous HRV + Fatigue tracking every 2 hours over next 6-hour window",
            status = InterventionStatus.PROPOSED,
            followUpHoursTotal = 6,
            followUpHoursElapsed = 0.5f
        )
    }

    /**
     * Aggregates individual crew assessments into collective 72-hour mission risk forecast.
     */
    fun calculate72hForecast(assessments: Collection<RiskAnomalyAssessment>): PredictiveRisk72h {
        if (assessments.isEmpty()) return PredictiveRisk72h()

        val avgStress = assessments.map { it.stressScore }.average()
        val avgFatigue = assessments.map { it.fatigueIndex }.average()
        val anomalyCount = assessments.count { it.isAnomaly }

        val mentalRisk = (avgStress * 0.28).roundToInt().coerceIn(5, 95)
        val fatigueRisk = (avgFatigue * 0.35).roundToInt().coerceIn(8, 95)
        val conflictRisk = ((avgStress + avgFatigue) * 0.16 + anomalyCount * 4).roundToInt().coerceIn(5, 90)
        val performance = (100 - (mentalRisk * 0.3 + fatigueRisk * 0.4 + conflictRisk * 0.3)).roundToInt().coerceIn(40, 99)

        val status = when {
            anomalyCount >= 2 || fatigueRisk > 50 || mentalRisk > 45 -> "ELEVATED RISK"
            anomalyCount == 1 -> "ACTIVE INTERVENTION"
            else -> "STABLE"
        }

        return PredictiveRisk72h(
            mentalRisk = mentalRisk,
            fatigueRisk = fatigueRisk,
            conflictRisk = conflictRisk,
            missionPerformance = performance,
            status = status
        )
    }

    /**
     * Get historical average HRV for a crew member.
     */
    fun getHistoricalAverageHrv(crewId: String): Double {
        val buffer = historyBuffer[crewId] ?: return baselines[crewId]?.baselineHrv?.toDouble() ?: 65.0
        synchronized(buffer) {
            if (buffer.isEmpty()) return baselines[crewId]?.baselineHrv?.toDouble() ?: 65.0
            return buffer.map { it.hrv }.average()
        }
    }

    /**
     * Get historical average Heart Rate for a crew member.
     */
    fun getHistoricalAverageHr(crewId: String): Double {
        val buffer = historyBuffer[crewId] ?: return baselines[crewId]?.baselineRestingHr?.toDouble() ?: 70.0
        synchronized(buffer) {
            if (buffer.isEmpty()) return baselines[crewId]?.baselineRestingHr?.toDouble() ?: 70.0
            return buffer.map { it.heartRate }.average()
        }
    }

    /**
     * Calibrate or update baseline parameters for a crew member.
     */
    fun calibrateBaseline(crewId: String, restingHr: Int, hrv: Int) {
        val current = baselines[crewId] ?: CrewBaseline()
        baselines[crewId] = current.copy(baselineRestingHr = restingHr, baselineHrv = hrv)
    }

    /**
     * Directly calculate fatigue score (0-100) from Heart Rate (BPM) and HRV (ms).
     */
    fun calculateFatigueScore(
        heartRateBpm: Int,
        hrvMs: Int,
        sleepHours: Float = 7.5f,
        sleepQuality: Int = 85,
        baselineHrv: Int = 68,
        baselineHr: Int = 68
    ): Int {
        val sleepDeficit = (8.0 - sleepHours).coerceAtLeast(0.0)
        val hrvDropRatio = (baselineHrv - hrvMs).coerceAtLeast(0).toDouble() / baselineHrv.toDouble()
        val hrExcess = (heartRateBpm - baselineHr).coerceAtLeast(0)

        val rawFatigue = 15.0 +
                (hrvDropRatio * 45.0) +
                (sleepDeficit * 7.5) +
                ((100 - sleepQuality) * 0.28) +
                (hrExcess * 0.7)

        return rawFatigue.coerceIn(8.0, 99.0).roundToInt()
    }

    /**
     * Directly calculate stress score (0-100) from Heart Rate (BPM) and HRV (ms).
     */
    fun calculateStressScore(
        heartRateBpm: Int,
        hrvMs: Int,
        respiration: Int = 14,
        sleepQuality: Int = 85,
        baselineHrv: Int = 68,
        baselineHr: Int = 68,
        baselineRespiration: Int = 13
    ): Int {
        val hrExcess = (heartRateBpm - baselineHr).coerceAtLeast(0)
        val hrvSuppression = (baselineHrv - hrvMs).coerceAtLeast(0)
        val respExcess = (respiration - baselineRespiration).coerceAtLeast(0)

        val rawStress = 20.0 +
                (hrExcess * 1.6) +
                (hrvSuppression * 1.3) +
                (respExcess * 3.0) +
                (if (sleepQuality < 70) 12.0 else 0.0)

        return rawStress.coerceIn(5.0, 98.0).roundToInt()
    }

    /**
     * Identify potential health risks and physiological anomalies from Heart Rate (BPM) and HRV (ms).
     */
    fun identifyHealthRisks(
        heartRateBpm: Int,
        hrvMs: Int,
        spO2: Int = 98,
        respiration: Int = 14,
        baselineHrv: Int = 68,
        baselineHr: Int = 68
    ): List<String> {
        val risks = mutableListOf<String>()
        if (hrvMs < baselineHrv - 15) {
            risks.add("Autonomic Parasympathetic Suppression (HRV $hrvMs ms vs baseline $baselineHrv ms)")
        }
        if (heartRateBpm > baselineHr + 15) {
            risks.add("Elevated Resting Heart Rate ($heartRateBpm BPM vs baseline $baselineHr BPM)")
        }
        if (heartRateBpm >= 85 && hrvMs <= 45) {
            risks.add("Acute Sympathetic Overdrive (High Stress / Cognitive Strain)")
        }
        if (spO2 < 95) {
            risks.add("Hypoxemia Risk: Peripheral Blood Oxygen Depletion (SpO2 $spO2%)")
        }
        if (respiration > 18) {
            risks.add("Hyperventilation / Tachypnea ($respiration breaths/min)")
        }
        if (risks.isEmpty()) {
            risks.add("No critical risks identified: Vitals within stable mission tolerances")
        }
        return risks
    }
}
