package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AstroMindDao {
    @Query("SELECT * FROM interventions ORDER BY timestamp DESC")
    fun getAllInterventions(): Flow<List<InterventionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntervention(intervention: InterventionEntity)

    @Update
    suspend fun updateIntervention(intervention: InterventionEntity)

    @Query("SELECT * FROM mission_alerts ORDER BY id DESC")
    fun getAllAlerts(): Flow<List<MissionAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: MissionAlertEntity)

    @Query("UPDATE mission_alerts SET isResolved = 1 WHERE id = :alertId")
    suspend fun resolveAlert(alertId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun logVitals(log: VitalsLogEntity)

    @Query("SELECT * FROM vitals_logs WHERE astronautId = :astronautId ORDER BY timestamp DESC LIMIT 20")
    fun getVitalsHistory(astronautId: String): Flow<List<VitalsLogEntity>>
}
