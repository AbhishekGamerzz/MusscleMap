package com.example.data.generator

import com.example.data.model.Equipment
import com.example.data.model.EquipmentPreset
import com.example.data.model.Exercise
import com.example.data.model.ExerciseDatabase
import com.example.data.model.FitnessGoal
import com.example.data.model.FitnessLevel
import com.example.data.model.GeneratedDayPlan
import com.example.data.model.GeneratedWorkoutProgram
import com.example.data.model.GeneratorType
import com.example.data.model.MuscleGroup
import com.example.data.model.TrainingSplit

object WorkoutGenerator {

    fun generateProgram(
        type: GeneratorType,
        gender: String,
        age: Int,
        level: FitnessLevel,
        goal: FitnessGoal,
        selectedDays: List<String>, // e.g. ["Mon", "Wed", "Fri"]
        split: TrainingSplit,
        targetMuscleSingle: MuscleGroup?,
        equipmentPreset: EquipmentPreset,
        customEquipment: Set<Equipment>
    ): GeneratedWorkoutProgram {
        val availableExercises = filterExercisesByEquipment(equipmentPreset, customEquipment)
        val dayCount = if (type == GeneratorType.SINGLE_WORKOUT) 1 else selectedDays.size.coerceAtLeast(3)

        val generatedDays = mutableListOf<GeneratedDayPlan>()

        if (type == GeneratorType.SINGLE_WORKOUT) {
            val targetMuscle = targetMuscleSingle ?: MuscleGroup.CHEST
            val dayExercises = getExercisesForMuscles(
                muscles = listOf(targetMuscle),
                availableExercises = availableExercises,
                count = 5
            )
            val secondaryMuscles = dayExercises.flatMap { it.secondaryMuscles }.distinct()
            val allTargetMuscles = (listOf(targetMuscle) + secondaryMuscles).distinct()

            generatedDays.add(
                GeneratedDayPlan(
                    dayName = "Single Day Session",
                    muscleFocus = "${targetMuscle.displayName} Focus",
                    targetMuscles = allTargetMuscles,
                    exercises = dayExercises
                )
            )
        } else {
            // Weekly Routine based on Split
            val dayNames = if (selectedDays.isNotEmpty()) selectedDays else listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            
            when (split) {
                TrainingSplit.FULL_BODY -> {
                    dayNames.take(dayCount).forEachIndexed { index, dayName ->
                        val targetMuscles = listOf(
                            MuscleGroup.CHEST,
                            MuscleGroup.LATS,
                            MuscleGroup.QUADS,
                            MuscleGroup.SHOULDERS,
                            MuscleGroup.ABS
                        )
                        val exList = getExercisesForMuscles(targetMuscles, availableExercises, count = 5)
                        generatedDays.add(
                            GeneratedDayPlan(
                                dayName = "$dayName - Full Body ${index + 1}",
                                muscleFocus = "Full Body Blitz",
                                targetMuscles = targetMuscles,
                                exercises = exList
                            )
                        )
                    }
                }
                TrainingSplit.UPPER_LOWER -> {
                    dayNames.take(dayCount).forEachIndexed { index, dayName ->
                        val isUpper = index % 2 == 0
                        val targetMuscles = if (isUpper) {
                            listOf(MuscleGroup.CHEST, MuscleGroup.LATS, MuscleGroup.SHOULDERS, MuscleGroup.BICEPS, MuscleGroup.TRICEPS)
                        } else {
                            listOf(MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.CALVES, MuscleGroup.ABS)
                        }
                        val title = if (isUpper) "$dayName - Upper Body" else "$dayName - Lower Body"
                        val focus = if (isUpper) "Upper Body Mass & Power" else "Lower Body Strength & Core"
                        val exList = getExercisesForMuscles(targetMuscles, availableExercises, count = 5)
                        generatedDays.add(
                            GeneratedDayPlan(
                                dayName = title,
                                muscleFocus = focus,
                                targetMuscles = targetMuscles,
                                exercises = exList
                            )
                        )
                    }
                }
                TrainingSplit.PUSH_PULL_LEGS -> {
                    dayNames.take(dayCount).forEachIndexed { index, dayName ->
                        val cycleIndex = index % 3
                        val (title, focus, muscles) = when (cycleIndex) {
                            0 -> Triple(
                                "$dayName - Push (Chest/Delts/Triceps)",
                                "Chest, Shoulders & Triceps",
                                listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS)
                            )
                            1 -> Triple(
                                "$dayName - Pull (Back/Biceps)",
                                "Back Width & Thickness, Biceps",
                                listOf(MuscleGroup.LATS, MuscleGroup.UPPER_BACK, MuscleGroup.BICEPS, MuscleGroup.TRAPS)
                            )
                            else -> Triple(
                                "$dayName - Legs & Abs",
                                "Quadriceps, Hamstrings, Glutes & Abs",
                                listOf(MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.CALVES, MuscleGroup.ABS)
                            )
                        }
                        val exList = getExercisesForMuscles(muscles, availableExercises, count = 5)
                        generatedDays.add(
                            GeneratedDayPlan(
                                dayName = title,
                                muscleFocus = focus,
                                targetMuscles = muscles,
                                exercises = exList
                            )
                        )
                    }
                }
                TrainingSplit.BRO_SPLIT -> {
                    val splits = listOf(
                        Triple("Chest & Triceps", "Chest Mass", listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS)),
                        Triple("Back & Biceps", "Back Thickness", listOf(MuscleGroup.LATS, MuscleGroup.UPPER_BACK, MuscleGroup.BICEPS)),
                        Triple("Shoulders & Traps", "Deltoid Width", listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRAPS)),
                        Triple("Legs & Calves", "Quad & Hamstring Power", listOf(MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES)),
                        Triple("Core & Arms", "Arm Pump & Abs", listOf(MuscleGroup.BICEPS, MuscleGroup.TRICEPS, MuscleGroup.ABS))
                    )
                    dayNames.take(dayCount).forEachIndexed { index, dayName ->
                        val splitData = splits[index % splits.size]
                        val exList = getExercisesForMuscles(splitData.third, availableExercises, count = 5)
                        generatedDays.add(
                            GeneratedDayPlan(
                                dayName = "$dayName - ${splitData.first}",
                                muscleFocus = splitData.second,
                                targetMuscles = splitData.third,
                                exercises = exList
                            )
                        )
                    }
                }
            }
        }

        val totalExCount = generatedDays.sumOf { it.exercises.size }
        val allUniqueMuscles = generatedDays.flatMap { it.targetMuscles }.distinct().size

        val title = if (type == GeneratorType.SINGLE_WORKOUT) {
            "Your Single-Day ${targetMuscleSingle?.displayName ?: "Workout"} Blast"
        } else {
            "Your ${dayCount}-Day ${split.displayName} Program"
        }

        return GeneratedWorkoutProgram(
            title = title,
            type = type,
            daysPerWeek = dayCount,
            totalExercises = totalExCount,
            targetMuscleCount = allUniqueMuscles.coerceAtLeast(10),
            days = generatedDays
        )
    }

    private fun filterExercisesByEquipment(
        preset: EquipmentPreset,
        customEquipment: Set<Equipment>
    ): List<Exercise> {
        val all = ExerciseDatabase.exercises
        return when (preset) {
            EquipmentPreset.FULL_GYM -> all
            EquipmentPreset.BODYWEIGHT -> all.filter { it.equipment == Equipment.BODYWEIGHT || it.equipment == Equipment.STRETCH }
            EquipmentPreset.HOME_GYM -> {
                if (customEquipment.isEmpty()) {
                    all.filter {
                        it.equipment == Equipment.BODYWEIGHT ||
                        it.equipment == Equipment.DUMBBELL ||
                        it.equipment == Equipment.KETTLEBELL ||
                        it.equipment == Equipment.BAND
                    }
                } else {
                    all.filter { it.equipment in customEquipment || it.equipment == Equipment.BODYWEIGHT }
                }
            }
        }
    }

    private fun getExercisesForMuscles(
        muscles: List<MuscleGroup>,
        availableExercises: List<Exercise>,
        count: Int
    ): List<Exercise> {
        val matched = availableExercises.filter { ex ->
            ex.primaryMuscle in muscles || ex.secondaryMuscles.any { it in muscles }
        }.distinctBy { it.id }

        if (matched.isEmpty()) {
            return ExerciseDatabase.exercises.take(count)
        }

        // Shuffle slightly for variation or take top matches
        val selected = matched.shuffled().take(count)
        return if (selected.size < count) {
            val fallback = availableExercises.filterNot { it in selected }.take(count - selected.size)
            selected + fallback
        } else {
            selected
        }
    }
}
