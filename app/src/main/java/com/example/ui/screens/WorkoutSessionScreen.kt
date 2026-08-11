package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import kotlinx.coroutines.delay

data class SetLog(
    val setIndex: Int,
    var weightKg: String = "50",
    var reps: String = "10",
    var isCompleted: Boolean = false
)

data class ExerciseLogState(
    val exercise: Exercise,
    val sets: MutableList<SetLog> = mutableListOf(
        SetLog(1), SetLog(2), SetLog(3)
    )
)

@Composable
fun WorkoutSessionScreen(
    routineName: String,
    exercises: List<Exercise>,
    onFinishWorkout: (routineName: String, totalVolumeKg: Float, durationSeconds: Int, completedSetsCount: Int) -> Unit,
    onCancelWorkout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var restSecondsLeft by remember { mutableIntStateOf(0) }
    var isRestTimerRunning by remember { mutableStateOf(false) }

    val exerciseLogs = remember {
        mutableStateListOf<ExerciseLogState>().apply {
            addAll(exercises.map { ExerciseLogState(it) })
        }
    }

    // Stopwatch Ticker
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsedSeconds++
        }
    }

    // Rest Timer Ticker
    LaunchedEffect(isRestTimerRunning, restSecondsLeft) {
        if (isRestTimerRunning && restSecondsLeft > 0) {
            delay(1000)
            restSecondsLeft--
            if (restSecondsLeft == 0) {
                isRestTimerRunning = false
            }
        }
    }

    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    // Calculate live volume
    var totalVolume = 0f
    var completedSetsCount = 0
    exerciseLogs.forEach { exLog ->
        exLog.sets.forEach { setLog ->
            if (setLog.isCompleted) {
                completedSetsCount++
                val w = setLog.weightKg.toFloatOrNull() ?: 0f
                val r = setLog.reps.toIntOrNull() ?: 0
                totalVolume += (w * r)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("workout_session_screen")
    ) {
        // Header Bar with Stopwatch and Volume
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = routineName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Elapsed: $timeFormatted",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${totalVolume.toInt()} kg",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "$completedSetsCount Sets Done",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onCancelWorkout,
                    modifier = Modifier.testTag("cancel_workout_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel Workout")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Rest Timer Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isRestTimerRunning) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceContainer
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRestTimerRunning) "Resting: ${restSecondsLeft}s" else "Rest Timer Quick Start",
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val presets = listOf(60, 90, 120)
                    presets.forEach { sec ->
                        Surface(
                            onClick = {
                                restSecondsLeft = sec
                                isRestTimerRunning = true
                            },
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.testTag("rest_timer_${sec}s")
                        ) {
                            Text(
                                text = "${sec}s",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Exercises Set Logger List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(exerciseLogs) { exLog ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = exLog.exercise.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Target: ${exLog.exercise.recommendedSetsReps}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        exLog.sets.forEachIndexed { setIdx, setLog ->
                            var isChecked by remember { mutableStateOf(setLog.isCompleted) }
                            var weightVal by remember { mutableStateOf(setLog.weightKg) }
                            var repsVal by remember { mutableStateOf(setLog.reps) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Set ${setIdx + 1}",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(50.dp)
                                )

                                OutlinedTextField(
                                    value = weightVal,
                                    onValueChange = {
                                        weightVal = it
                                        setLog.weightKg = it
                                    },
                                    label = { Text("kg") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(90.dp),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = repsVal,
                                    onValueChange = {
                                        repsVal = it
                                        setLog.reps = it
                                    },
                                    label = { Text("Reps") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(90.dp),
                                    singleLine = true
                                )

                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        isChecked = checked
                                        setLog.isCompleted = checked
                                        if (checked) {
                                            // Auto-trigger 60s rest timer
                                            restSecondsLeft = 60
                                            isRestTimerRunning = true
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.testTag("set_check_${exLog.exercise.id}_$setIdx")
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Complete Workout Button
        Button(
            onClick = {
                onFinishWorkout(routineName, totalVolume, elapsedSeconds, completedSetsCount)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("finish_workout_btn"),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(imageVector = Icons.Default.DoneAll, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Finish Workout & Log Progress", fontWeight = FontWeight.Bold)
        }
    }
}
