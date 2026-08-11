package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.RoutineEntity
import com.example.data.db.WorkoutLogEntity
import com.example.data.db.repository.MuscleRepository
import com.example.data.model.BodyView
import com.example.data.model.Equipment
import com.example.data.model.Exercise
import com.example.data.model.ExerciseDatabase
import com.example.data.model.Gender
import com.example.data.model.MuscleGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ActiveWorkoutState {
    data object Idle : ActiveWorkoutState()
    data class Active(
        val routineName: String,
        val exercises: List<Exercise>
    ) : ActiveWorkoutState()
}

class MuscleWikiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MuscleRepository

    init {
        val dao = AppDatabase.getDatabase(application).muscleWikiDao()
        repository = MuscleRepository(dao)

        // Seed initial routines if database is new
        viewModelScope.launch {
            seedInitialRoutines()
        }
    }

    // Filter States
    private val _selectedGender = MutableStateFlow(Gender.MALE)
    val selectedGender: StateFlow<Gender> = _selectedGender.asStateFlow()

    private val _selectedBodyView = MutableStateFlow(BodyView.FRONT)
    val selectedBodyView: StateFlow<BodyView> = _selectedBodyView.asStateFlow()

    private val _selectedMuscle = MutableStateFlow<MuscleGroup?>(null)
    val selectedMuscle: StateFlow<MuscleGroup?> = _selectedMuscle.asStateFlow()

    private val _selectedEquipment = MutableStateFlow(Equipment.ALL)
    val selectedEquipment: StateFlow<Equipment> = _selectedEquipment.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Exercise Detail Dialog State
    private val _selectedExerciseDetail = MutableStateFlow<Exercise?>(null)
    val selectedExerciseDetail: StateFlow<Exercise?> = _selectedExerciseDetail.asStateFlow()

    // Active Workout State
    private val _activeWorkout = MutableStateFlow<ActiveWorkoutState>(ActiveWorkoutState.Idle)
    val activeWorkout: StateFlow<ActiveWorkoutState> = _activeWorkout.asStateFlow()

    // Room DB Reactive Flow
    val favoriteIds: StateFlow<List<String>> = repository.allFavorites
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteExercises: StateFlow<List<Exercise>> = repository.favoriteExercises
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val routines: StateFlow<List<RoutineEntity>> = repository.allRoutines
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val workoutLogs: StateFlow<List<WorkoutLogEntity>> = repository.allWorkoutLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Filtered Exercises Flow
    val filteredExercises: StateFlow<List<Exercise>> = combine(
        _selectedMuscle,
        _selectedEquipment,
        _searchQuery
    ) { muscle, eq, query ->
        repository.getFilteredExercises(
            muscleGroup = muscle,
            equipment = eq,
            query = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExerciseDatabase.exercises
    )

    // User Actions
    fun setGender(gender: Gender) {
        _selectedGender.value = gender
    }

    fun setBodyView(view: BodyView) {
        _selectedBodyView.value = view
        // Reset selected muscle if it belongs to other view
        _selectedMuscle.value?.let { muscle ->
            if (muscle.defaultView != view) {
                _selectedMuscle.value = null
            }
        }
    }

    fun setMuscle(muscle: MuscleGroup?) {
        _selectedMuscle.value = muscle
        if (muscle != null) {
            _selectedBodyView.value = muscle.defaultView
        }
    }

    fun setEquipment(equipment: Equipment) {
        _selectedEquipment.value = equipment
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun showExerciseDetail(exercise: Exercise?) {
        _selectedExerciseDetail.value = exercise
    }

    fun toggleFavorite(exerciseId: String) {
        viewModelScope.launch {
            val isFav = favoriteIds.value.contains(exerciseId)
            repository.toggleFavorite(exerciseId, isFav)
        }
    }

    fun createRoutine(name: String, description: String, exerciseIds: List<String>) {
        viewModelScope.launch {
            repository.createRoutine(name, description, exerciseIds)
        }
    }

    fun deleteRoutine(routineId: Long) {
        viewModelScope.launch {
            repository.deleteRoutine(routineId)
        }
    }

    fun startWorkoutSession(routineName: String, exercises: List<Exercise>) {
        _activeWorkout.value = ActiveWorkoutState.Active(routineName, exercises)
    }

    fun finishWorkoutSession(
        routineName: String,
        totalVolumeKg: Float,
        durationSeconds: Int,
        completedSetsCount: Int
    ) {
        viewModelScope.launch {
            repository.saveWorkoutLog(
                routineName = routineName,
                totalVolumeKg = totalVolumeKg,
                durationSeconds = durationSeconds,
                completedSetsCount = completedSetsCount
            )
            _activeWorkout.value = ActiveWorkoutState.Idle
        }
    }

    fun cancelWorkoutSession() {
        _activeWorkout.value = ActiveWorkoutState.Idle
    }

    fun deleteWorkoutLog(logId: Long) {
        viewModelScope.launch {
            repository.deleteWorkoutLog(logId)
        }
    }

    private suspend fun seedInitialRoutines() {
        // Will populate default starter routines if DB is currently empty
        // Check performed inside repository or via query if needed
    }
}
