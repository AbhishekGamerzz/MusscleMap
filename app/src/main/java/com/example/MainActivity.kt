package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Exercise
import com.example.ui.screens.BodyMapScreen
import com.example.ui.screens.CalculatorsScreen
import com.example.ui.screens.ExerciseDetailDialog
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.RoutineGeneratorScreen
import com.example.ui.screens.RoutinesScreen
import com.example.ui.screens.WorkoutSessionScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ActiveWorkoutState
import com.example.viewmodel.MuscleWikiViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MuscleWikiApp()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleWikiApp(
    viewModel: MuscleWikiViewModel = viewModel()
) {
    var selectedNavIndex by remember { mutableIntStateOf(0) }

    val gender by viewModel.selectedGender.collectAsStateWithLifecycle()
    val bodyView by viewModel.selectedBodyView.collectAsStateWithLifecycle()
    val selectedMuscle by viewModel.selectedMuscle.collectAsStateWithLifecycle()
    val selectedEquipment by viewModel.selectedEquipment.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredExercises by viewModel.filteredExercises.collectAsStateWithLifecycle()

    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()
    val favoriteExercises by viewModel.favoriteExercises.collectAsStateWithLifecycle()
    val routines by viewModel.routines.collectAsStateWithLifecycle()
    val workoutLogs by viewModel.workoutLogs.collectAsStateWithLifecycle()

    val selectedExerciseDetail by viewModel.selectedExerciseDetail.collectAsStateWithLifecycle()
    val activeWorkout by viewModel.activeWorkout.collectAsStateWithLifecycle()

    when (val workoutState = activeWorkout) {
        is ActiveWorkoutState.Active -> {
            // Live Active Workout Full Screen Session
            WorkoutSessionScreen(
                routineName = workoutState.routineName,
                exercises = workoutState.exercises,
                onFinishWorkout = { name, vol, dur, sets ->
                    viewModel.finishWorkoutSession(name, vol, dur, sets)
                },
                onCancelWorkout = { viewModel.cancelWorkoutSession() }
            )
        }
        ActiveWorkoutState.Idle -> {
            var showGlobalGenerator by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "MuscleWiki",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        actions = {
                            Button(
                                onClick = { showGlobalGenerator = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .testTag("top_bar_generator_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "⚡ Generator",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ) {
                        NavigationBarItem(
                            selected = selectedNavIndex == 0,
                            onClick = { selectedNavIndex = 0 },
                            icon = { Icon(Icons.Default.AccessibilityNew, contentDescription = "Muscle Map") },
                            label = { Text("Muscle Map") },
                            modifier = Modifier.testTag("nav_muscle_map")
                        )

                        NavigationBarItem(
                            selected = selectedNavIndex == 1,
                            onClick = { selectedNavIndex = 1 },
                            icon = { Icon(Icons.Default.ListAlt, contentDescription = "Routines") },
                            label = { Text("Routines") },
                            modifier = Modifier.testTag("nav_routines")
                        )

                        NavigationBarItem(
                            selected = selectedNavIndex == 2,
                            onClick = { selectedNavIndex = 2 },
                            icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculators") },
                            label = { Text("Calculators") },
                            modifier = Modifier.testTag("nav_calculators")
                        )

                        NavigationBarItem(
                            selected = selectedNavIndex == 3,
                            onClick = { selectedNavIndex = 3 },
                            icon = { Icon(Icons.Default.History, contentDescription = "History") },
                            label = { Text("History") },
                            modifier = Modifier.testTag("nav_history")
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (selectedNavIndex) {
                        0 -> BodyMapScreen(
                            gender = gender,
                            bodyView = bodyView,
                            selectedMuscle = selectedMuscle,
                            selectedEquipment = selectedEquipment,
                            searchQuery = searchQuery,
                            exercises = filteredExercises,
                            favoriteIds = favoriteIds,
                            onGenderSelected = viewModel::setGender,
                            onViewSelected = viewModel::setBodyView,
                            onMuscleSelected = viewModel::setMuscle,
                            onEquipmentSelected = viewModel::setEquipment,
                            onSearchQueryChanged = viewModel::setSearchQuery,
                            onFavoriteToggle = viewModel::toggleFavorite,
                            onExerciseClicked = viewModel::showExerciseDetail
                        )

                        1 -> RoutinesScreen(
                            routines = routines,
                            favoriteExercises = favoriteExercises,
                            favoriteIds = favoriteIds,
                            onToggleFavorite = viewModel::toggleFavorite,
                            onStartWorkout = viewModel::startWorkoutSession,
                            onCreateRoutine = viewModel::createRoutine,
                            onDeleteRoutine = viewModel::deleteRoutine,
                            onExerciseClicked = viewModel::showExerciseDetail
                        )

                        2 -> CalculatorsScreen()

                        3 -> HistoryScreen(
                            logs = workoutLogs,
                            onDeleteLog = viewModel::deleteWorkoutLog
                        )
                    }
                }
            }

            if (showGlobalGenerator) {
                Dialog(
                    onDismissRequest = { showGlobalGenerator = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        RoutineGeneratorScreen(
                            onDismiss = { showGlobalGenerator = false },
                            onSaveRoutine = { name, desc, exIds ->
                                viewModel.createRoutine(name, desc, exIds)
                            },
                            onStartWorkout = { name, exercises ->
                                viewModel.startWorkoutSession(name, exercises)
                                showGlobalGenerator = false
                            }
                        )
                    }
                }
            }
        }
    }

    // Exercise Detail Dialog Overlay
    selectedExerciseDetail?.let { exercise ->
        ExerciseDetailDialog(
            exercise = exercise,
            isFavorite = favoriteIds.contains(exercise.id),
            onFavoriteToggle = { viewModel.toggleFavorite(exercise.id) },
            onAddToRoutineClicked = {
                // Instantly create routine with this exercise or add to workout
                viewModel.createRoutine(
                    name = "${exercise.name} Focus",
                    description = "Custom single-exercise session",
                    exerciseIds = listOf(exercise.id)
                )
                viewModel.showExerciseDetail(null)
                selectedNavIndex = 1 // Navigate to Routines
            },
            onDismiss = { viewModel.showExerciseDetail(null) }
        )
    }
}
