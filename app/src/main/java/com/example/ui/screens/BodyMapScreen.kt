package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.BodyView
import com.example.data.model.Equipment
import com.example.data.model.Exercise
import com.example.data.model.Gender
import com.example.data.model.MuscleGroup
import com.example.ui.components.ExerciseCard
import com.example.ui.components.InteractiveBodyMap

@Composable
fun BodyMapScreen(
    gender: Gender,
    bodyView: BodyView,
    selectedMuscle: MuscleGroup?,
    selectedEquipment: Equipment,
    searchQuery: String,
    exercises: List<Exercise>,
    favoriteIds: List<String>,
    onGenderSelected: (Gender) -> Unit,
    onViewSelected: (BodyView) -> Unit,
    onMuscleSelected: (MuscleGroup?) -> Unit,
    onEquipmentSelected: (Equipment) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onExerciseClicked: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("body_map_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search Bar
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text("Search exercises (e.g. Bench Press, Squat, Lat Pulldown)") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("exercise_search_bar"),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }

        // Interactive Body Map Canvas & Muscle Controls
        item {
            InteractiveBodyMap(
                gender = gender,
                bodyView = bodyView,
                selectedMuscle = selectedMuscle,
                onGenderSelected = onGenderSelected,
                onViewSelected = onViewSelected,
                onMuscleSelected = onMuscleSelected
            )
        }

        // Equipment Filter Row
        item {
            Column {
                Text(
                    text = "Filter Equipment:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Equipment.entries) { eq ->
                        val isSelected = selectedEquipment == eq
                        FilterChip(
                            selected = isSelected,
                            onClick = { onEquipmentSelected(eq) },
                            label = { Text(eq.displayName, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.testTag("equipment_chip_${eq.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // Filter Results Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedMuscle != null) {
                        "${selectedMuscle.displayName} Exercises (${exercises.size})"
                    } else {
                        "All Exercises (${exercises.size})"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (selectedMuscle != null || selectedEquipment != Equipment.ALL || searchQuery.isNotEmpty()) {
                    Surface(
                        onClick = {
                            onMuscleSelected(null)
                            onEquipmentSelected(Equipment.ALL)
                            onSearchQueryChanged("")
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        Text(
                            text = "Reset Filters",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Exercise Cards
        if (exercises.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No exercises matched your current filters",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Try switching equipment or clearing the search bar.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(exercises, key = { it.id }) { exercise ->
                val isFav = favoriteIds.contains(exercise.id)
                ExerciseCard(
                    exercise = exercise,
                    isFavorite = isFav,
                    onFavoriteToggle = { onFavoriteToggle(exercise.id) },
                    onClick = { onExerciseClicked(exercise) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
