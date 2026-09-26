package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        InterventionEntity::class,
        MissionAlertEntity::class,
        VitalsLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AstroMindDatabase : RoomDatabase() {
    abstract fun dao(): AstroMindDao

    companion object {
        @Volatile
        private var INSTANCE: AstroMindDatabase? = null

        fun getDatabase(context: Context): AstroMindDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AstroMindDatabase::class.java,
                    "astromind_mission.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
