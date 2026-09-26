package com.example

import com.example.data.AlertSeverity
import com.example.data.AnomalyType
import com.example.data.HealthMetricsSample
import com.example.data.engine.PredictiveRiskEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PredictiveRiskEngineTest {

    private lateinit var engine: PredictiveRiskEngine

    @Before
    fun setUp() {
        engine = PredictiveRiskEngine()
    }

    @Test
    fun testNominalCrewSampleProducesNoAnomaly() {
        val nominalSample = HealthMetricsSample(
            crewId = "alex_chen",
            heartRate = 70,
            hrv = 66,
            spO2 = 98,
            respiration = 14,
            sleepQuality = 90,
            sleepHours = 8.0f
        )

        val assessment = engine.processSample(nominalSample, "Alex Chen")

        assertFalse(assessment.isAnomaly)
        assertEquals(AnomalyType.NONE, assessment.anomalyType)
        assertEquals(AlertSeverity.ADVISORY, assessment.severity)
        assertTrue(assessment.stressScore in 5..45)
        assertTrue(assessment.fatigueIndex in 8..50)

        val intervention = engine.generateInterventionIfAnomaly(assessment)
        assertNull(intervention)
    }

    @Test
    fun testAcuteStressAnomalyTriggeredByHighHrAndSuppressedHrv() {
        // High resting heart rate with severely suppressed HRV and high respiration
        val stressSample = HealthMetricsSample(
            crewId = "david_kim",
            heartRate = 92,
            hrv = 38,
            spO2 = 97,
            respiration = 20,
            sleepQuality = 60,
            sleepHours = 5.0f
        )

        val assessment = engine.processSample(stressSample, "David Kim")

        assertTrue("Expected anomaly for David Kim", assessment.isAnomaly)
        assertTrue(
            "Expected stress or fatigue anomaly",
            assessment.anomalyType == AnomalyType.ACUTE_STRESS_SPIKE ||
                    assessment.anomalyType == AnomalyType.FATIGUE_ACCUMULATION
        )
        assertTrue("Expected elevated or critical severity", assessment.severity != AlertSeverity.ADVISORY)
        assertTrue("Stress score should be elevated", assessment.stressScore >= 50)

        // Intervention generation check
        val intervention = engine.generateInterventionIfAnomaly(assessment)
        assertNotNull("Intervention should be generated for anomaly", intervention)
        assertEquals("david_kim", intervention?.astronautId)
        assertTrue(intervention?.actionPlan?.isNotEmpty() == true)
    }

    @Test
    fun testFatigueAccumulationAnomalyTriggeredBySleepDeficitAndHrvDrop() {
        // Michael Ross with fatigue and significant sleep deficit
        val fatigueSample = HealthMetricsSample(
            crewId = "michael_ross",
            heartRate = 78,
            hrv = 44,
            spO2 = 98,
            respiration = 15,
            sleepQuality = 55,
            sleepHours = 4.8f
        )

        val assessment = engine.processSample(fatigueSample, "Michael Ross")

        assertTrue("Fatigue sample should flag anomaly", assessment.isAnomaly)
        assertTrue("Fatigue index should be elevated", assessment.fatigueIndex >= 50)
        assertTrue("Triggers should contain sleep or HRV markers", assessment.primaryTriggers.isNotEmpty())
    }

    @Test
    fun test72hCollectiveForecastCalculation() {
        val sample1 = HealthMetricsSample("alex_chen", 70, 66, sleepHours = 8.0f)
        val sample2 = HealthMetricsSample("sarah_khan", 66, 72, sleepHours = 8.0f)
        val sample3 = HealthMetricsSample("david_kim", 90, 40, sleepHours = 5.0f)

        val a1 = engine.processSample(sample1, "Alex Chen")
        val a2 = engine.processSample(sample2, "Sarah Khan")
        val a3 = engine.processSample(sample3, "David Kim")

        val forecast = engine.calculate72hForecast(listOf(a1, a2, a3))

        assertTrue(forecast.mentalRisk in 5..95)
        assertTrue(forecast.fatigueRisk in 8..95)
        assertTrue(forecast.conflictRisk in 5..90)
        assertTrue(forecast.missionPerformance in 40..99)
        assertEquals("ACTIVE INTERVENTION", forecast.status)
    }

    @Test
    fun testDirectFatigueAndStressScoreCalculations() {
        // Optimal vitals
        val lowFatigue = engine.calculateFatigueScore(heartRateBpm = 64, hrvMs = 70, sleepHours = 8.0f)
        assertTrue("Optimal vitals should have low fatigue score", lowFatigue < 30)

        // Degraded vitals
        val highFatigue = engine.calculateFatigueScore(heartRateBpm = 88, hrvMs = 38, sleepHours = 4.5f)
        assertTrue("Compromised vitals should have elevated fatigue score", highFatigue >= 60)

        val lowStress = engine.calculateStressScore(heartRateBpm = 65, hrvMs = 70, respiration = 13)
        assertTrue("Optimal vitals should have low stress score", lowStress < 35)

        val highStress = engine.calculateStressScore(heartRateBpm = 95, hrvMs = 36, respiration = 20)
        assertTrue("Strained vitals should have high stress score", highStress >= 65)
    }

    @Test
    fun testIdentifyHealthRisksFromVitals() {
        val risks = engine.identifyHealthRisks(
            heartRateBpm = 92,
            hrvMs = 40,
            spO2 = 94,
            respiration = 21
        )

        assertTrue(risks.isNotEmpty())
        assertTrue(risks.any { it.contains("Parasympathetic Suppression") || it.contains("HRV") })
        assertTrue(risks.any { it.contains("Hypoxemia") || it.contains("SpO2") })
        assertTrue(risks.any { it.contains("Elevated Resting Heart Rate") || it.contains("Sympathetic") })
    }
}
