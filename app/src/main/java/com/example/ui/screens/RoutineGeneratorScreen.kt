package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.generator.WorkoutGenerator
import com.example.data.model.Equipment
import com.example.data.model.EquipmentPreset
import com.example.data.model.Exercise
import com.example.data.model.FitnessGoal
import com.example.data.model.FitnessLevel
import com.example.data.model.GeneratedDayPlan
import com.example.data.model.GeneratedWorkoutProgram
import com.example.data.model.GeneratorType
import com.example.data.model.Gender
import com.example.data.model.MuscleGroup
import com.example.data.model.TrainingSplit
import com.example.ui.components.BodyCanvasGraphic
import kotlinx.coroutines.delay

enum class GeneratorStep {
    MODE_SELECTION,
    PROFILE,
    TRAINING,
    SPLIT,
    EQUIPMENT,
    GENERATING,
    SUMMARY
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RoutineGeneratorScreen(
    onDismiss: () -> Unit,
    onSaveRoutine: (name: String, description: String, exerciseIds: List<String>) -> Unit,
    onStartWorkout: (routineName: String, exercises: List<Exercise>) -> Unit
) {
    var currentStep by remember { mutableStateOf(GeneratorStep.MODE_SELECTION) }

    // User Selection States
    var selectedType by remember { mutableStateOf(GeneratorType.WEEKLY_ROUTINE) }
    var selectedGender by remember { mutableStateOf(Gender.MALE) }
    var selectedAge by remember { mutableIntStateOf(25) }
    var selectedLevel by remember { mutableStateOf(FitnessLevel.BEGINNER) }
    var selectedGoal by remember { mutableStateOf(FitnessGoal.GAIN_MUSCLE) }
    var selectedDays by remember { mutableStateOf(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat")) }
    var selectedSplit by remember { mutableStateOf(TrainingSplit.UPPER_LOWER) }
    var selectedSingleMuscle by remember { mutableStateOf<MuscleGroup?>(MuscleGroup.CHEST) }
    var selectedEquipmentPreset by remember { mutableStateOf(EquipmentPreset.FULL_GYM) }
    var selectedCustomEquipment by remember { mutableStateOf(setOf<Equipment>()) }

    // Generated Result
    var generatedProgram by remember { mutableStateOf<GeneratedWorkoutProgram?>(null) }
    var selectedDayTabIndex by remember { mutableIntStateOf(0) }
    var showSavedToast by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (currentStep == GeneratorStep.SUMMARY) "Generated Program" else "Routine Generator",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (currentStep == GeneratorStep.MODE_SELECTION || currentStep == GeneratorStep.SUMMARY) {
                                onDismiss()
                            } else {
                                currentStep = when (currentStep) {
                                    GeneratorStep.PROFILE -> GeneratorStep.MODE_SELECTION
                                    GeneratorStep.TRAINING -> GeneratorStep.PROFILE
                                    GeneratorStep.SPLIT -> GeneratorStep.TRAINING
                                    GeneratorStep.EQUIPMENT -> GeneratorStep.SPLIT
                                    else -> GeneratorStep.MODE_SELECTION
                                }
                            }
                        },
                        modifier = Modifier.testTag("generator_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Step Navigation Header Bar (Visible on Steps 1 to 4)
            if (currentStep in listOf(
                    GeneratorStep.PROFILE,
                    GeneratorStep.TRAINING,
                    GeneratorStep.SPLIT,
                    GeneratorStep.EQUIPMENT
                )
            ) {
                StepProgressHeader(currentStep = currentStep)
            }

            // Screen Content Based on Current Step
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (currentStep) {
                    GeneratorStep.MODE_SELECTION -> {
                        ModeSelectionView(
                            onSelectMode = { type ->
                                selectedType = type
                                currentStep = GeneratorStep.PROFILE
                            }
                        )
                    }

                    GeneratorStep.PROFILE -> {
                        ProfileStepView(
                            gender = selectedGender,
                            age = selectedAge,
                            onGenderChange = { selectedGender = it },
                            onAgeChange = { selectedAge = it },
                            onNext = { currentStep = GeneratorStep.TRAINING }
                        )
                    }

                    GeneratorStep.TRAINING -> {
                        TrainingStepView(
                            generatorType = selectedType,
                            level = selectedLevel,
                            goal = selectedGoal,
                            selectedDays = selectedDays,
                            onLevelChange = { selectedLevel = it },
                            onGoalChange = { selectedGoal = it },
                            onDaysChange = { selectedDays = it },
                            onNext = { currentStep = GeneratorStep.SPLIT },
                            onBack = { currentStep = GeneratorStep.PROFILE }
                        )
                    }

                    GeneratorStep.SPLIT -> {
                        SplitStepView(
                            generatorType = selectedType,
                            selectedSplit = selectedSplit,
                            selectedSingleMuscle = selectedSingleMuscle,
                            onSplitChange = { selectedSplit = it },
                            onSingleMuscleChange = { selectedSingleMuscle = it },
                            onNext = { currentStep = GeneratorStep.EQUIPMENT },
                            onBack = { currentStep = GeneratorStep.TRAINING }
                        )
                    }

                    GeneratorStep.EQUIPMENT -> {
                        EquipmentStepView(
                            preset = selectedEquipmentPreset,
                            customEquipment = selectedCustomEquipment,
                            onPresetChange = { selectedEquipmentPreset = it },
                            onToggleCustom = { eq ->
                                selectedCustomEquipment = if (selectedCustomEquipment.contains(eq)) {
                                    selectedCustomEquipment - eq
                                } else {
                                    selectedCustomEquipment + eq
                                }
                            },
                            onGenerate = {
                                currentStep = GeneratorStep.GENERATING
                            },
                            onBack = { currentStep = GeneratorStep.SPLIT }
                        )
                    }

                    GeneratorStep.GENERATING -> {
                        GeneratingLoadingView {
                            generatedProgram = WorkoutGenerator.generateProgram(
                                type = selectedType,
                                gender = selectedGender.label,
                                age = selectedAge,
                                level = selectedLevel,
                                goal = selectedGoal,
                                selectedDays = selectedDays,
                                split = selectedSplit,
                                targetMuscleSingle = selectedSingleMuscle,
                                equipmentPreset = selectedEquipmentPreset,
                                customEquipment = selectedCustomEquipment
                            )
                            currentStep = GeneratorStep.SUMMARY
                        }
                    }

                    GeneratorStep.SUMMARY -> {
                        generatedProgram?.let { program ->
                            ProgramSummaryView(
                                program = program,
                                selectedTabIndex = selectedDayTabIndex,
                                showSavedToast = showSavedToast,
                                onSelectTab = { selectedDayTabIndex = it },
                                onSaveRoutine = {
                                    showSavedToast = true
                                    program.days.forEach { day ->
                                        onSaveRoutine(
                                            day.dayName,
                                            "${program.title} • ${day.muscleFocus}",
                                            day.exercises.map { it.id }
                                        )
                                    }
                                },
                                onStartWorkout = { day ->
                                    onStartWorkout(day.dayName, day.exercises)
                                    onDismiss()
                                },
                                onRegenerate = {
                                    currentStep = GeneratorStep.GENERATING
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// STEP PROGRESS HEADER
// -----------------------------------------------------------------------------
@Composable
private fun StepProgressHeader(currentStep: GeneratorStep) {
    val steps = listOf(
        GeneratorStep.PROFILE to "Profile",
        GeneratorStep.TRAINING to "Training",
        GeneratorStep.SPLIT to "Split",
        GeneratorStep.EQUIPMENT to "Equipment"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, (step, label) ->
                val isCompleted = step.ordinal < currentStep.ordinal
                val isActive = step == currentStep

                val iconColor = when {
                    isCompleted -> MaterialTheme.colorScheme.primary
                    isActive -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (isActive) MaterialTheme.colorScheme.primary
                                else if (isCompleted) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        color = iconColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar
        val progress = when (currentStep) {
            GeneratorStep.PROFILE -> 0.25f
            GeneratorStep.TRAINING -> 0.50f
            GeneratorStep.SPLIT -> 0.75f
            GeneratorStep.EQUIPMENT -> 1.0f
            else -> 0f
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    }
}

// -----------------------------------------------------------------------------
// STEP 0: MODE SELECTION
// -----------------------------------------------------------------------------
@Composable
private fun ModeSelectionView(
    onSelectMode: (GeneratorType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Icon Banner
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "⚡ Routine Generator",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Build Your Perfect Routine",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Answer 4 quick questions to get a personalized workout plan matched to your body and goals.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        // Single Workout Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectMode(GeneratorType.SINGLE_WORKOUT) }
                .testTag("mode_single_workout"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "⚡ QUICK & FOCUSED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }

                    Icon(
                        Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Single Workout",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Generate a targeted workout for today. Perfect when you're short on time but want maximum results.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    FeatureCheckRow(text = "Ready in 30 seconds")
                    FeatureCheckRow(text = "Focus on specific muscles")
                    FeatureCheckRow(text = "Start immediately")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onSelectMode(GeneratorType.SINGLE_WORKOUT) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Get Started →", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekly Routine Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                .clickable { onSelectMode(GeneratorType.WEEKLY_ROUTINE) }
                .testTag("mode_weekly_routine"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "RECOMMENDED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "COMPLETE PROGRAM",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Weekly Routine",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Build a structured multi-day program tailored to your goals. Ideal for consistent, long-term progress.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    FeatureCheckRow(text = "3-6 days per week")
                    FeatureCheckRow(text = "Balanced muscle coverage")
                    FeatureCheckRow(text = "Progressive overload built-in")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onSelectMode(GeneratorType.WEEKLY_ROUTINE) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Get Started →", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FeatureCheckRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}

// -----------------------------------------------------------------------------
// STEP 1: PROFILE SETUP
// -----------------------------------------------------------------------------
@Composable
private fun ProfileStepView(
    gender: Gender,
    age: Int,
    onGenderChange: (Gender) -> Unit,
    onAgeChange: (Int) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Let's personalize your routine",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "We'll show you exercises matched to your body type & experience level.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Gender Selection
        Text(
            text = "SELECT YOUR GENDER",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenderSelectCard(
                gender = Gender.MALE,
                isSelected = gender == Gender.MALE,
                onClick = { onGenderChange(Gender.MALE) },
                modifier = Modifier.weight(1f)
            )

            GenderSelectCard(
                gender = Gender.FEMALE,
                isSelected = gender == Gender.FEMALE,
                onClick = { onGenderChange(Gender.FEMALE) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Age Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "YOUR AGE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "$age years old",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = age.toFloat(),
            onValueChange = { onAgeChange(it.toInt()) },
            valueRange = 13f..80f,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("13 yrs", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("80 yrs", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("profile_next_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Next →", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun GenderSelectCard(
    gender: Gender,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = gender.label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// -----------------------------------------------------------------------------
// STEP 2: TRAINING SETUP
// -----------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TrainingStepView(
    generatorType: GeneratorType,
    level: FitnessLevel,
    goal: FitnessGoal,
    selectedDays: List<String>,
    onLevelChange: (FitnessLevel) -> Unit,
    onGoalChange: (FitnessGoal) -> Unit,
    onDaysChange: (List<String>) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val allDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Training Setup",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Customize your experience and fitness goals.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // FITNESS LEVEL
        Text(
            text = "YOUR FITNESS LEVEL?",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FitnessLevel.entries.forEach { lvl ->
            val isSelected = level == lvl
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onLevelChange(lvl) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(lvl.displayName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(lvl.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // FITNESS GOAL
        Text(
            text = "YOUR FITNESS GOAL?",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FitnessGoal.entries.forEach { g ->
            val isSelected = goal == g
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onGoalChange(g) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(g.displayName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(g.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        if (generatorType == GeneratorType.WEEKLY_ROUTINE) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TRAINING DAYS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "${selectedDays.size} day(s) selected",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                allDays.forEach { day ->
                    val isSelected = selectedDays.contains(day)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newList = if (isSelected) {
                                if (selectedDays.size > 1) selectedDays - day else selectedDays
                            } else {
                                selectedDays + day
                            }
                            onDaysChange(newList)
                        },
                        label = { Text(day, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text("← Go Back")
            }

            Button(
                onClick = onNext,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("training_next_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Next →", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -----------------------------------------------------------------------------
// STEP 3: SPLIT SETUP / SINGLE MUSCLE
// -----------------------------------------------------------------------------
@Composable
private fun SplitStepView(
    generatorType: GeneratorType,
    selectedSplit: TrainingSplit,
    selectedSingleMuscle: MuscleGroup?,
    onSplitChange: (TrainingSplit) -> Unit,
    onSingleMuscleChange: (MuscleGroup) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = if (generatorType == GeneratorType.WEEKLY_ROUTINE) "Choose Training Split" else "Target Muscle Group",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (generatorType == GeneratorType.WEEKLY_ROUTINE)
                "Select a split structure that fits your weekly recovery style."
            else "Pick the primary muscle focus for today's session.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        if (generatorType == GeneratorType.WEEKLY_ROUTINE) {
            TrainingSplit.entries.forEach { split ->
                val isSelected = selectedSplit == split
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onSplitChange(split) }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(split.displayName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            if (isSelected) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(split.description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            // Single Workout Muscle Selector
            MuscleGroup.entries.forEach { muscle ->
                val isSelected = selectedSingleMuscle == muscle
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onSingleMuscleChange(muscle) }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            muscle.displayName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text("← Go Back")
            }

            Button(
                onClick = onNext,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("split_next_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Next →", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -----------------------------------------------------------------------------
// STEP 4: EQUIPMENT SETUP
// -----------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EquipmentStepView(
    preset: EquipmentPreset,
    customEquipment: Set<Equipment>,
    onPresetChange: (EquipmentPreset) -> Unit,
    onToggleCustom: (Equipment) -> Unit,
    onGenerate: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Equipment Setup",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Select the equipment you have access to.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        EquipmentPreset.entries.forEach { p ->
            val isSelected = preset == p
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onPresetChange(p) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(p.displayName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(p.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        if (preset == EquipmentPreset.HOME_GYM) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SELECT YOUR HOME EQUIPMENT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val options = listOf(
                Equipment.DUMBBELL,
                Equipment.BARBELL,
                Equipment.KETTLEBELL,
                Equipment.BAND,
                Equipment.BODYWEIGHT
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { eq ->
                    val isChecked = customEquipment.contains(eq)
                    FilterChip(
                        selected = isChecked,
                        onClick = { onToggleCustom(eq) },
                        label = { Text(eq.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "✓ Targeting major muscle groups for a complete, injury-free routine.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text("← Back")
            }

            Button(
                onClick = onGenerate,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("generate_routine_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Generate →", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// STEP 5: GENERATING LOADING VIEW
// -----------------------------------------------------------------------------
@Composable
private fun GeneratingLoadingView(
    onComplete: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1500) // Simulated AI calculation delay
        onComplete()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 6.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Building Your Routine...",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Selecting optimal exercise biomechanics and sets & reps",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

// -----------------------------------------------------------------------------
// STEP 6: PROGRAM SUMMARY VIEW
// -----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgramSummaryView(
    program: GeneratedWorkoutProgram,
    selectedTabIndex: Int,
    showSavedToast: Boolean,
    onSelectTab: (Int) -> Unit,
    onSaveRoutine: () -> Unit,
    onStartWorkout: (GeneratedDayPlan) -> Unit,
    onRegenerate: () -> Unit
) {
    val activeDayPlan = program.days.getOrNull(selectedTabIndex) ?: program.days.first()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scrollable Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = program.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Personalized program built for your goals",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Stat Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatBadge(text = "✓ ${program.daysPerWeek} Days/Wk", modifier = Modifier.weight(1f))
                StatBadge(text = "✓ ${program.totalExercises} Exercises", modifier = Modifier.weight(1f))
                StatBadge(text = "✓ ${program.targetMuscleCount} Muscles", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Day Selector Tabs
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                program.days.forEachIndexed { index, day ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { onSelectTab(index) },
                        text = {
                            Text(
                                text = "Day ${index + 1}",
                                fontSize = 13.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Muscle Highlight Map Card for current day
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = activeDayPlan.dayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = activeDayPlan.muscleFocus,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Muscle Body Diagram Diagram
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        BodyCanvasGraphic(
                            gender = Gender.MALE,
                            bodyView = activeDayPlan.targetMuscles.firstOrNull()?.defaultView ?: com.example.data.model.BodyView.FRONT,
                            selectedMuscle = activeDayPlan.targetMuscles.firstOrNull(),
                            onMuscleTapped = {}
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "EXERCISES (${activeDayPlan.exercises.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Exercise List for Active Day
            activeDayPlan.exercises.forEachIndexed { idx, ex ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(ex.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(
                                text = "${ex.primaryMuscle.displayName} • ${ex.recommendedSetsReps}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(ex.equipment.displayName, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            if (showSavedToast) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Routine Saved to My Routines!", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = onSaveRoutine,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_generated_routine_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("💾 Save to My Routines", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onStartWorkout(activeDayPlan) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("start_generated_workout_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("▶ Start Day ${selectedTabIndex + 1} Workout Now", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onRegenerate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("🔄 Regenerate Routine")
            }
        }
    }
}

@Composable
private fun StatBadge(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}
