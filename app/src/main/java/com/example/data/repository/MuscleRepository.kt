package com.example.data.db.repository

import com.example.data.db.FavoriteEntity
import com.example.data.db.MuscleWikiDao
import com.example.data.db.RoutineEntity
import com.example.data.db.WorkoutLogEntity
import com.example.data.model.Difficulty
import com.example.data.model.Equipment
import com.example.data.model.Exercise
import com.example.data.model.ExerciseDatabase
import com.example.data.model.MuscleGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MuscleRepository(private val dao: MuscleWikiDao) {

    val allFavorites: Flow<List<String>> = dao.getAllFavorites().map { list ->
        list.map { it.exerciseId }
    }

    val favoriteExercises: Flow<List<Exercise>> = dao.getAllFavorites().map { favList ->
        favList.mapNotNull { fav -> ExerciseDatabase.getExerciseById(fav.exerciseId) }
    }

    val allRoutines: Flow<List<RoutineEntity>> = dao.getAllRoutines()

    val allWorkoutLogs: Flow<List<WorkoutLogEntity>> = dao.getAllWorkoutLogs()

    fun getFilteredExercises(
        muscleGroup: MuscleGroup? = null,
        equipment: Equipment = Equipment.ALL,
        difficulty: Difficulty? = null,
        query: String = ""
    ): List<Exercise> {
        return ExerciseDatabase.filterExercises(muscleGroup, equipment, difficulty, query)
    }

    suspend fun toggleFavorite(exerciseId: String, isCurrentlyFav: Boolean) {
        if (isCurrentlyFav) {
            dao.removeFavorite(exerciseId)
        } else {
            dao.addFavorite(FavoriteEntity(exerciseId = exerciseId))
        }
    }

    suspend fun createRoutine(name: String, description: String, exerciseIds: List<String>): Long {
        val csv = exerciseIds.joinToString(",")
        return dao.insertRoutine(RoutineEntity(name = name, description = description, exerciseIdsCsv = csv))
    }

    suspend fun deleteRoutine(routineId: Long) {
        dao.deleteRoutine(routineId)
    }

    suspend fun saveWorkoutLog(
        routineName: String,
        totalVolumeKg: Float,
        durationSeconds: Int,
        completedSetsCount: Int,
        notes: String = ""
    ): Long {
        return dao.insertWorkoutLog(
            WorkoutLogEntity(
                routineName = routineName,
                totalVolumeKg = totalVolumeKg,
                durationSeconds = durationSeconds,
                completedSetsCount = completedSetsCount,
                notes = notes
            )
        )
    }

    suspend fun deleteWorkoutLog(logId: Long) {
        dao.deleteWorkoutLog(logId)
    }

    // Default pre-populated starter routines if empty
    suspend fun seedStarterRoutinesIfEmpty() {
        // Will check if routines exist in VM
    }
}
