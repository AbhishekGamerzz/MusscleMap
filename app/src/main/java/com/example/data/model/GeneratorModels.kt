package com.example.data.model

enum class GeneratorType(val title: String, val subtitle: String) {
    SINGLE_WORKOUT("Single Workout", "Generate a targeted workout for today. Perfect when you're short on time."),
    WEEKLY_ROUTINE("Weekly Routine", "Build a structured multi-day program tailored to your goals.")
}

enum class FitnessLevel(val displayName: String, val description: String) {
    NOVICE("Novice", "New to working out, learning movement form"),
    BEGINNER("Beginner", "< 1 year of consistent weight training"),
    INTERMEDIATE("Intermediate", "1 - 3 years of structured training"),
    ADVANCED("Advanced", "3+ years of dedicated strength & hypertrophy")
}

enum class FitnessGoal(val displayName: String, val description: String) {
    LOSE_WEIGHT("Lose Weight", "Higher rep volume, active recovery & muscle retention"),
    GAIN_STRENGTH("Gain Strength", "Heavy compound lifts, power focus (3 - 6 reps)"),
    GAIN_MUSCLE("Gain Muscle", "Hypertrophy focus, optimal muscle tension (8 - 12 reps)")
}

enum class TrainingSplit(val displayName: String, val description: String, val minDays: Int) {
    FULL_BODY("Full Body", "Train your entire body each session for maximum efficiency", 3),
    UPPER_LOWER("Upper / Lower", "Alternating upper and lower body for balanced training", 4),
    PUSH_PULL_LEGS("Push / Pull / Legs", "Separate pushing, pulling, and leg muscles", 6),
    BRO_SPLIT("Bro Split", "Targeted body part split for maximum hypertrophy", 5)
}

enum class EquipmentPreset(val displayName: String, val description: String) {
    FULL_GYM("Full Gym", "Access to barbells, dumbbells, cables & machines"),
    HOME_GYM("Home Gym", "Basic dumbbells, kettlebells & bodyweight setup"),
    BODYWEIGHT("Bodyweight", "No equipment needed. Ideal for training anywhere")
}

data class GeneratedDayPlan(
    val dayName: String, // e.g. "Day 1 - Push", "Monday - Upper Body"
    val muscleFocus: String,
    val targetMuscles: List<MuscleGroup>,
    val exercises: List<Exercise>
)

data class GeneratedWorkoutProgram(
    val title: String,
    val type: GeneratorType,
    val daysPerWeek: Int,
    val totalExercises: Int,
    val targetMuscleCount: Int,
    val days: List<GeneratedDayPlan>
)
