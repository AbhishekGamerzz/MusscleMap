package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val exerciseId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val exerciseIdsCsv: String, // Comma separated list of exercise IDs
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val totalVolumeKg: Float,
    val durationSeconds: Int,
    val completedSetsCount: Int,
    val notes: String = ""
)
