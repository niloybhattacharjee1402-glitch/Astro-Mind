package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interventions")
data class InterventionEntity(
    @PrimaryKey val id: String,
    val astronautId: String,
    val astronautName: String,
    val detectedRisk: String,
    val triggerMetricsRaw: String, // Comma separated
    val actionPlanRaw: String, // Newline separated
    val followUpProtocol: String,
    val status: String,
    val followUpHoursTotal: Int,
    val followUpHoursElapsed: Float,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mission_alerts")
data class MissionAlertEntity(
    @PrimaryKey val id: String,
    val title: String,
    val severity: String,
    val category: String,
    val description: String,
    val correlation: String,
    val actionsRaw: String,
    val isResolved: Boolean,
    val timestamp: String
)

@Entity(tableName = "vitals_logs")
data class VitalsLogEntity(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0,
    val astronautId: String,
    val sol: Int,
    val heartRate: Int,
    val hrv: Int,
    val spO2: Int,
    val stressScore: Int,
    val fatigueIndex: Int,
    val timestamp: Long = System.currentTimeMillis()
)
