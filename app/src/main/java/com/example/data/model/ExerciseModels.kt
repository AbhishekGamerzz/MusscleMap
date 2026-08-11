package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class Gender(val label: String) {
    MALE("Male"),
    FEMALE("Female")
}

enum class BodyView(val label: String) {
    FRONT("Front"),
    BACK("Back")
}

enum class MuscleGroup(
    val id: String,
    val displayName: String,
    val defaultView: BodyView,
    val colorHex: Long = 0xFF00B4D8
) {
    CHEST("chest", "Chest (Pectorals)", BodyView.FRONT, 0xFF00B4D8),
    SHOULDERS("shoulders", "Shoulders (Deltoids)", BodyView.FRONT, 0xFF06B6D4),
    BICEPS("biceps", "Biceps", BodyView.FRONT, 0xFF3B82F6),
    FOREARMS("forearms", "Forearms", BodyView.FRONT, 0xFF6366F1),
    ABS("abs", "Abs (Abdominis)", BodyView.FRONT, 0xFF10B981),
    OBLIQUES("obliques", "Obliques", BodyView.FRONT, 0xFF34D399),
    QUADS("quads", "Quadriceps", BodyView.FRONT, 0xFFF59E0B),
    CALVES("calves", "Calves (Gastrocnemius)", BodyView.FRONT, 0xFFEF4444),

    TRAPS("traps", "Traps (Trapezius)", BodyView.BACK, 0xFF8B5CF6),
    LATS("lats", "Lats (Latissimus Dorsi)", BodyView.BACK, 0xFF6366F1),
    UPPER_BACK("upper_back", "Upper Back (Rhomboids)", BodyView.BACK, 0xFF3B82F6),
    LOWER_BACK("lower_back", "Lower Back (Erector Spinae)", BodyView.BACK, 0xFFF97316),
    TRICEPS("triceps", "Triceps", BodyView.BACK, 0xFFEC4899),
    GLUTES("glutes", "Glutes (Gluteus Max)", BodyView.BACK, 0xFFD97706),
    HAMSTRINGS("hamstrings", "Hamstrings", BodyView.BACK, 0xFFEF4444)
}

enum class Equipment(val displayName: String) {
    ALL("All Equipment"),
    BARBELL("Barbell"),
    DUMBBELL("Dumbbell"),
    BODYWEIGHT("Bodyweight"),
    MACHINE("Machine"),
    CABLE("Cable"),
    KETTLEBELL("Kettlebell"),
    BAND("Band"),
    STRETCH("Stretching")
}

enum class Difficulty(val displayName: String, val colorHex: Long) {
    BEGINNER("Beginner", 0xFF10B981),
    INTERMEDIATE("Intermediate", 0xFFF59E0B),
    ADVANCED("Advanced", 0xFFEF4444)
}

enum class ExerciseCategory(val displayName: String) {
    STRENGTH("Strength"),
    HYPERTROPHY("Hypertrophy"),
    ENDURANCE("Endurance"),
    MOBILITY("Mobility / Flex")
}

data class Exercise(
    val id: String,
    val name: String,
    val primaryMuscle: MuscleGroup,
    val secondaryMuscles: List<MuscleGroup> = emptyList(),
    val equipment: Equipment,
    val difficulty: Difficulty,
    val category: ExerciseCategory = ExerciseCategory.HYPERTROPHY,
    val summary: String,
    val setupInstructions: List<String>,
    val executionSteps: List<String>,
    val keyTips: List<String>,
    val recommendedSetsReps: String = "3 sets x 8 - 12 reps",
    val diagramType: String = "BARBELL_PRESS" // Graphic visualizer indicator
)
