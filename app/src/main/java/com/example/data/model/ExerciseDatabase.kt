package com.example.data.model

object ExerciseDatabase {
    val exercises: List<Exercise> = listOf(
        // CHEST EXERCISES
        Exercise(
            id = "chest_barbell_bench_press",
            name = "Barbell Bench Press",
            primaryMuscle = MuscleGroup.CHEST,
            secondaryMuscles = listOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.STRENGTH,
            summary = "The classic compound chest press exercise for mass and strength.",
            setupInstructions = listOf(
                "Lie flat on the bench with feet firmly planted on the floor.",
                "Grasp the barbell slightly wider than shoulder-width apart.",
                "Unrack the bar and hold it directly over your upper chest with arms fully extended."
            ),
            executionSteps = listOf(
                "Inhale and lower the barbell slowly to your mid-chest line, keeping elbows at a 45-degree angle.",
                "Pause briefly at the bottom without letting the bar bounce off your chest.",
                "Drive through your chest and triceps to press the barbell explosively back to the starting position."
            ),
            keyTips = listOf(
                "Retract your shoulder blades and keep them locked down throughout the rep.",
                "Avoid flare-out elbows at a 90-degree angle to protect shoulder joints."
            ),
            recommendedSetsReps = "4 sets x 6 - 8 reps",
            diagramType = "BARBELL_PRESS"
        ),
        Exercise(
            id = "chest_dumbbell_incline_press",
            name = "Incline Dumbbell Press",
            primaryMuscle = MuscleGroup.CHEST,
            secondaryMuscles = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            equipment = Equipment.DUMBBELL,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Target upper pectoral development with free weight incline movement.",
            setupInstructions = listOf(
                "Set an adjustable bench to a 30 to 45 degree incline.",
                "Sit with dumbbells resting on your thighs, then kick them up one by one as you lay back."
            ),
            executionSteps = listOf(
                "Position dumbbells at chest height with palms facing forward.",
                "Press dumbbells upward in a slight arc until arms are extended above upper chest.",
                "Squeeze pectorals at top position then lower under control."
            ),
            keyTips = listOf(
                "Do not set bench angle too steep (above 45 degrees) or shoulder deltoids will take over."
            ),
            recommendedSetsReps = "3 sets x 10 - 12 reps",
            diagramType = "DUMBBELL_PRESS"
        ),
        Exercise(
            id = "chest_pushups",
            name = "Bodyweight Push-Up",
            primaryMuscle = MuscleGroup.CHEST,
            secondaryMuscles = listOf(MuscleGroup.TRICEPS, MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            equipment = Equipment.BODYWEIGHT,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Fundamental bodyweight pushing movement for chest and core stability.",
            setupInstructions = listOf(
                "Place hands slightly wider than shoulder width on the floor.",
                "Extend legs straight back, forming a straight rigid line from head to heels."
            ),
            executionSteps = listOf(
                "Lower your chest until it nearly touches the floor, bending elbows backwards.",
                "Push back up smoothly to full arm extension."
            ),
            keyTips = listOf(
                "Keep glutes engaged and core braced so hips do not sag."
            ),
            recommendedSetsReps = "3 sets x 15 - 20 reps",
            diagramType = "BODYWEIGHT_PUSH"
        ),
        Exercise(
            id = "chest_cable_crossover",
            name = "Cable Chest Fly",
            primaryMuscle = MuscleGroup.CHEST,
            secondaryMuscles = listOf(MuscleGroup.SHOULDERS),
            equipment = Equipment.CABLE,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Provides constant cable tension throughout inner pectoral stretch and contraction.",
            setupInstructions = listOf(
                "Set cable pulleys at chest or high position and attach stirrup handles.",
                "Grab handles and step forward in a staggered stance."
            ),
            executionSteps = listOf(
                "With slight bend in elbows, bring hands together in front of chest in a wide hugging arc.",
                "Pause and squeeze inner chest muscles for 1 second.",
                "Return slowly to open stretch position."
            ),
            keyTips = listOf(
                "Keep elbow bend constant like hugging a large tree trunk."
            ),
            recommendedSetsReps = "3 sets x 12 - 15 reps",
            diagramType = "CABLE_FLY"
        ),

        // SHOULDERS EXERCISES
        Exercise(
            id = "shoulders_overhead_press",
            name = "Overhead Barbell Military Press",
            primaryMuscle = MuscleGroup.SHOULDERS,
            secondaryMuscles = listOf(MuscleGroup.TRICEPS, MuscleGroup.UPPER_BACK),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.STRENGTH,
            summary = "Premier overhead press for shoulder width, overhead strength, and core stability.",
            setupInstructions = listOf(
                "Set barbell at collarbone height in squat rack.",
                "Grip bar with hands just outside shoulders, elbows pointing slightly forward."
            ),
            executionSteps = listOf(
                "Unrack bar and step back. Inhale and brace core.",
                "Press barbell overhead in straight vertical path, clearing your head.",
                "Lock out arms over shoulders, then lower slowly back to upper chest."
            ),
            keyTips = listOf(
                "Squeeze glutes and abs to avoid arching lower back excesively."
            ),
            recommendedSetsReps = "4 sets x 6 - 8 reps",
            diagramType = "OVERHEAD_PRESS"
        ),
        Exercise(
            id = "shoulders_lateral_raise",
            name = "Dumbbell Lateral Raise",
            primaryMuscle = MuscleGroup.SHOULDERS,
            secondaryMuscles = listOf(MuscleGroup.TRAPS),
            equipment = Equipment.DUMBBELL,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Isolates lateral deltoids for wide capped shoulders visual profile.",
            setupInstructions = listOf(
                "Stand tall holding dumbbells at sides, palms facing inward.",
                "Maintain soft bend in elbows."
            ),
            executionSteps = listOf(
                "Raise dumbbells outward to sides until arms reach shoulder height.",
                "Lead slightly with elbows and pinkies elevated.",
                "Lower arms slowly back down."
            ),
            keyTips = listOf(
                "Avoid swinging hips or momentum; use controlled tempo."
            ),
            recommendedSetsReps = "4 sets x 12 - 15 reps",
            diagramType = "LATERAL_RAISE"
        ),
        Exercise(
            id = "shoulders_face_pull",
            name = "Cable Face Pull",
            primaryMuscle = MuscleGroup.SHOULDERS,
            secondaryMuscles = listOf(MuscleGroup.UPPER_BACK, MuscleGroup.TRAPS),
            equipment = Equipment.CABLE,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.MOBILITY,
            summary = "Essential postural exercise targeting rear deltoids and external rotators.",
            setupInstructions = listOf(
                "Attach rope to high cable pulley.",
                "Grip rope ends with thumbs pointing backwards."
            ),
            executionSteps = listOf(
                "Step back and pull rope directly towards face level.",
                "Separate hands as you pull so knuckles end up beside ears.",
                "Squeeze rear shoulders and shoulder blades, then lower back."
            ),
            keyTips = listOf(
                "Focus on external shoulder rotation rather than just pulling with arms."
            ),
            recommendedSetsReps = "3 sets x 15 reps",
            diagramType = "CABLE_PULL"
        ),

        // BICEPS EXERCISES
        Exercise(
            id = "biceps_barbell_curl",
            name = "Barbell Bicep Curl",
            primaryMuscle = MuscleGroup.BICEPS,
            secondaryMuscles = listOf(MuscleGroup.FOREARMS),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Classic mass builder for bicep peak and arm thickness.",
            setupInstructions = listOf(
                "Stand erect holding straight or EZ bar with shoulder-width underhand grip."
            ),
            executionSteps = listOf(
                "Keep upper arms stationary at sides.",
                "Curl weight upward contracting biceps until forearms are near vertical.",
                "Squeeze biceps at peak, then lower weight under full control."
            ),
            keyTips = listOf(
                "Do not let elbows swing forward or use momentum from lower back."
            ),
            recommendedSetsReps = "3 sets x 10 - 12 reps",
            diagramType = "ARM_CURL"
        ),
        Exercise(
            id = "biceps_hammer_curl",
            name = "Dumbbell Hammer Curl",
            primaryMuscle = MuscleGroup.BICEPS,
            secondaryMuscles = listOf(MuscleGroup.FOREARMS),
            equipment = Equipment.DUMBBELL,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Targets brachialis and forearms for thick arm appearance.",
            setupInstructions = listOf(
                "Stand holding dumbbells with neutral grip (palms facing each other)."
            ),
            executionSteps = listOf(
                "Curl dumbbells upward keeping palms facing inward throughout movement.",
                "Raise to shoulder level, squeeze forearm and bicep, then lower down."
            ),
            keyTips = listOf(
                "Alternate arms or curl simultaneously for intense forearm pump."
            ),
            recommendedSetsReps = "3 sets x 10 - 12 reps",
            diagramType = "ARM_CURL"
        ),

        // TRICEPS EXERCISES
        Exercise(
            id = "triceps_pushdown",
            name = "Cable Tricep Rope Pushdown",
            primaryMuscle = MuscleGroup.TRICEPS,
            secondaryMuscles = listOf(MuscleGroup.FOREARMS),
            equipment = Equipment.CABLE,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "High isolation exercise targeting lateral and medial tricep heads.",
            setupInstructions = listOf(
                "Attach rope to high cable pulley and grab ends.",
                "Keep elbows pinned close to torso."
            ),
            executionSteps = listOf(
                "Extend arms downward by pushing rope towards thighs.",
                "Spread rope ends apart at bottom for complete tricep extension.",
                "Slowly allow elbows to flex back up to 90 degrees."
            ),
            keyTips = listOf(
                "Keep upper arms motionless; motion happens purely at elbow joint."
            ),
            recommendedSetsReps = "3 sets x 12 - 15 reps",
            diagramType = "TRICEP_EXTENSION"
        ),
        Exercise(
            id = "triceps_skull_crusher",
            name = "Lying EZ-Bar Skull Crusher",
            primaryMuscle = MuscleGroup.TRICEPS,
            secondaryMuscles = listOf(MuscleGroup.FOREARMS),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Deep overhead stretch for long head tricep development.",
            setupInstructions = listOf(
                "Lie flat on bench holding EZ curl bar over upper chest.",
                "Angle arms slightly backwards toward head."
            ),
            executionSteps = listOf(
                "Flex elbows to lower bar towards forehead or top of head.",
                "Keep upper arms stationary.",
                "Extend elbows to press weight back up."
            ),
            keyTips = listOf(
                "Keep elbows tucked inward rather than flaring outwards."
            ),
            recommendedSetsReps = "3 sets x 10 - 12 reps",
            diagramType = "TRICEP_EXTENSION"
        ),

        // FOREARMS EXERCISES
        Exercise(
            id = "forearms_wrist_curl",
            name = "Barbell Wrist Curl",
            primaryMuscle = MuscleGroup.FOREARMS,
            secondaryMuscles = listOf(MuscleGroup.BICEPS),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Direct flexor forearm exercise for grip strength and forearm size.",
            setupInstructions = listOf(
                "Sit on bench resting forearms on thighs with wrists hanging over knees, palms up.",
                "Hold barbell with fingertips."
            ),
            executionSteps = listOf(
                "Unroll bar down fingers, then curl wrists upward squeezing forearms.",
                "Lower bar back down into stretched fingers."
            ),
            keyTips = listOf(
                "Use manageable weight to avoid wrist joint strain."
            ),
            recommendedSetsReps = "3 sets x 15 reps",
            diagramType = "WRIST_CURL"
        ),

        // ABS & OBLIQUES EXERCISES
        Exercise(
            id = "abs_hanging_leg_raise",
            name = "Hanging Leg Raise",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = listOf(MuscleGroup.OBLIQUES, MuscleGroup.FOREARMS),
            equipment = Equipment.BODYWEIGHT,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Advanced core movement for lower abdominal definition and hip strength.",
            setupInstructions = listOf(
                "Hang from pull-up bar with overhand grip, arms extended.",
                "Brace abdominal wall."
            ),
            executionSteps = listOf(
                "Raise legs straight out in front until parallel to floor (or knees to chest for regression).",
                "Pause at top, flexing abs.",
                "Lower legs down under control without swinging body."
            ),
            keyTips = listOf(
                "Tilt pelvis upward at top of rep to fully engage rectus abdominis."
            ),
            recommendedSetsReps = "3 sets x 10 - 15 reps",
            diagramType = "CORE_RAISE"
        ),
        Exercise(
            id = "abs_plank",
            name = "Forearm Plank Hold",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = listOf(MuscleGroup.OBLIQUES, MuscleGroup.SHOULDERS),
            equipment = Equipment.BODYWEIGHT,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.ENDURANCE,
            summary = "Isometric core pillar stability builder.",
            setupInstructions = listOf(
                "Rest on forearms and toes with elbows directly beneath shoulders."
            ),
            executionSteps = listOf(
                "Maintain flat neutral spine line from head to ankles.",
                "Contract core, glutes, and quad muscles tightly.",
                "Hold static position for target time duration."
            ),
            keyTips = listOf(
                "Breathe rhythmically; do not hold breath."
            ),
            recommendedSetsReps = "3 sets x 45 - 60 seconds",
            diagramType = "PLANK_HOLD"
        ),
        Exercise(
            id = "obliques_russian_twist",
            name = "Weighted Russian Twist",
            primaryMuscle = MuscleGroup.OBLIQUES,
            secondaryMuscles = listOf(MuscleGroup.ABS),
            equipment = Equipment.DUMBBELL,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Rotational core exercise targeting side obliques.",
            setupInstructions = listOf(
                "Sit on floor, knees bent, feet elevated slightly.",
                "Hold weight plate or dumbbell with both hands."
            ),
            executionSteps = listOf(
                "Lean torso back at 45 degree angle.",
                "Rotate torso side to side, touching weight near floor beside hip on each side."
            ),
            keyTips = listOf(
                "Twist shoulders and ribcage rather than just moving arms."
            ),
            recommendedSetsReps = "3 sets x 20 twists total",
            diagramType = "CORE_RAISE"
        ),

        // QUADS EXERCISES
        Exercise(
            id = "quads_barbell_squat",
            name = "Barbell Back Squat",
            primaryMuscle = MuscleGroup.QUADS,
            secondaryMuscles = listOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.LOWER_BACK),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.STRENGTH,
            summary = "King of leg exercises for full lower body development.",
            setupInstructions = listOf(
                "Rest barbell across upper traps in squat rack.",
                "Unrack bar and stand with feet shoulder-width apart, toes slightly angled outward."
            ),
            executionSteps = listOf(
                "Inhale deeply, hinge at hips and bend knees to lower body.",
                "Squat down until thighs are at least parallel to floor.",
                "Drive through heels and midfoot to push back up to standing lockout."
            ),
            keyTips = listOf(
                "Keep knees tracking over toes; do not let knees cave inward."
            ),
            recommendedSetsReps = "4 sets x 6 - 8 reps",
            diagramType = "LEG_SQUAT"
        ),
        Exercise(
            id = "quads_leg_extension",
            name = "Machine Leg Extension",
            primaryMuscle = MuscleGroup.QUADS,
            secondaryMuscles = emptyList(),
            equipment = Equipment.MACHINE,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Isolated quad developer targeting teardrop muscle.",
            setupInstructions = listOf(
                "Sit on machine with back flush against pad.",
                "Adjust lower leg pad so it rests right above ankles."
            ),
            executionSteps = listOf(
                "Extend knees to lift weight until legs are straight.",
                "Squeeze quad muscles forcefully at top.",
                "Lower pad smoothly back to starting bend."
            ),
            keyTips = listOf(
                "Do not slam weights or use momentum."
            ),
            recommendedSetsReps = "3 sets x 12 - 15 reps",
            diagramType = "LEG_SQUAT"
        ),

        // HAMSTRINGS & CALVES EXERCISES
        Exercise(
            id = "hamstrings_romanian_deadlift",
            name = "Barbell Romanian Deadlift (RDL)",
            primaryMuscle = MuscleGroup.HAMSTRINGS,
            secondaryMuscles = listOf(MuscleGroup.GLUTES, MuscleGroup.LOWER_BACK),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Hip hinge loaded stretch for hamstring length and glute tie-in.",
            setupInstructions = listOf(
                "Hold barbell with overhand grip at hip height, soft bend in knees."
            ),
            executionSteps = listOf(
                "Push hips far back while sliding bar down close along thighs.",
                "Lower until deep stretch is felt in hamstrings (around mid-shin height).",
                "Contract hamstrings and glutes to pull hips forward back upright."
            ),
            keyTips = listOf(
                "Keep spine straight and bar touching legs throughout movement."
            ),
            recommendedSetsReps = "3 sets x 8 - 10 reps",
            diagramType = "DEADLIFT"
        ),
        Exercise(
            id = "calves_standing_raise",
            name = "Standing Calf Raise",
            primaryMuscle = MuscleGroup.CALVES,
            secondaryMuscles = emptyList(),
            equipment = Equipment.MACHINE,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Gastrocnemius calf builder with full range stretch and peak rise.",
            setupInstructions = listOf(
                "Position balls of feet on step edge with heels hanging off.",
                "Place shoulder pads over shoulders."
            ),
            executionSteps = listOf(
                "Lower heels below step level for deep calf stretch.",
                "Press up onto big toes as high as possible.",
                "Pause for 1 second at top squeeze."
            ),
            keyTips = listOf(
                "Avoid bouncing; slow controlled tempo yields best calf growth."
            ),
            recommendedSetsReps = "4 sets x 15 reps",
            diagramType = "CALF_RAISE"
        ),

        // BACK EXERCISES (Lats, Traps, Upper Back, Lower Back)
        Exercise(
            id = "lats_pullups",
            name = "Bodyweight Pull-Up",
            primaryMuscle = MuscleGroup.LATS,
            secondaryMuscles = listOf(MuscleGroup.BICEPS, MuscleGroup.UPPER_BACK),
            equipment = Equipment.BODYWEIGHT,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.STRENGTH,
            summary = "Ultimate bodyweight vertical pulling exercise for V-taper lat width.",
            setupInstructions = listOf(
                "Grip overhead bar slightly wider than shoulder width with palms facing away."
            ),
            executionSteps = listOf(
                "Hang with straight arms.",
                "Pull chest up toward bar by driving elbows down and back.",
                "Bring chin over bar, squeeze lats, then lower back down fully."
            ),
            keyTips = listOf(
                "Depress shoulder blades before pulling to engage lats properly."
            ),
            recommendedSetsReps = "3 sets x 6 - 10 reps",
            diagramType = "PULL_UP"
        ),
        Exercise(
            id = "lats_pulldown",
            name = "Lat Pulldown Machine",
            primaryMuscle = MuscleGroup.LATS,
            secondaryMuscles = listOf(MuscleGroup.BICEPS, MuscleGroup.SHOULDERS),
            equipment = Equipment.CABLE,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Accessible lat developer with adjustable weight pin.",
            setupInstructions = listOf(
                "Sit facing pulley with thigh pads snug over knees.",
                "Grip wide bar with overhand grip."
            ),
            executionSteps = listOf(
                "Lean back slightly, pull bar down to upper chest level.",
                "Focus on squeezing lats beneath armpits.",
                "Return bar smoothly upward to full lat stretch."
            ),
            keyTips = listOf(
                "Do not pull bar behind neck; pull to collarbone."
            ),
            recommendedSetsReps = "3 sets x 10 - 12 reps",
            diagramType = "CABLE_PULL"
        ),
        Exercise(
            id = "upper_back_barbell_row",
            name = "Bent-Over Barbell Row",
            primaryMuscle = MuscleGroup.UPPER_BACK,
            secondaryMuscles = listOf(MuscleGroup.LATS, MuscleGroup.BICEPS, MuscleGroup.LOWER_BACK),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.STRENGTH,
            summary = "Heavy horizontal pulling exercise for back density and thickness.",
            setupInstructions = listOf(
                "Hinge at hips to 45 degree angle, knees slightly bent, holding barbell with overhand grip."
            ),
            executionSteps = listOf(
                "Pull barbell toward belly button driving elbows toward ceiling.",
                "Squeeze shoulder blades together firmly at top.",
                "Lower bar under control."
            ),
            keyTips = listOf(
                "Keep torso rigid; do not jerk body up and down."
            ),
            recommendedSetsReps = "4 sets x 8 reps",
            diagramType = "ROWING"
        ),
        Exercise(
            id = "traps_barbell_shrug",
            name = "Barbell Shrug",
            primaryMuscle = MuscleGroup.TRAPS,
            secondaryMuscles = listOf(MuscleGroup.FOREARMS),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Direct trap builder for upper neck and shoulder girdle mass.",
            setupInstructions = listOf(
                "Stand holding heavy barbell at thigh height, feet hip-width apart."
            ),
            executionSteps = listOf(
                "Elevate shoulders toward ears as high as possible.",
                "Hold peak contraction for 1 second at top.",
                "Lower shoulders back down."
            ),
            keyTips = listOf(
                "Do not roll shoulders in circles; move straight up and down."
            ),
            recommendedSetsReps = "4 sets x 12 - 15 reps",
            diagramType = "SHRUG"
        ),
        Exercise(
            id = "lower_back_hyperextension",
            name = "45-Degree Back Extension",
            primaryMuscle = MuscleGroup.LOWER_BACK,
            secondaryMuscles = listOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            equipment = Equipment.BODYWEIGHT,
            difficulty = Difficulty.BEGINNER,
            category = ExerciseCategory.MOBILITY,
            summary = "Posterior chain strengthener for lower back health and endurance.",
            setupInstructions = listOf(
                "Anchor thighs on bench pad with hips free to hinge, ankles locked behind foot pads."
            ),
            executionSteps = listOf(
                "Cross arms over chest and bend forward at hips.",
                "Raise torso up until body forms straight line.",
                "Pause brief moment before lowering back down."
            ),
            keyTips = listOf(
                "Do not hyperextend spine excessively beyond straight line at top."
            ),
            recommendedSetsReps = "3 sets x 12 - 15 reps",
            diagramType = "PLANK_HOLD"
        ),

        // GLUTES EXERCISES
        Exercise(
            id = "glutes_barbell_hip_thrust",
            name = "Barbell Hip Thrust",
            primaryMuscle = MuscleGroup.GLUTES,
            secondaryMuscles = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.QUADS),
            equipment = Equipment.BARBELL,
            difficulty = Difficulty.INTERMEDIATE,
            category = ExerciseCategory.HYPERTROPHY,
            summary = "Maximum glute activation compound movement.",
            setupInstructions = listOf(
                "Sit on floor with upper back against bench, barbell resting across hip crease with pad."
            ),
            executionSteps = listOf(
                "Drive through heels to extend hips upward until torso and thighs form bridge parallel to floor.",
                "Squeeze glutes hard at top lockout.",
                "Lower hips back down under control."
            ),
            keyTips = listOf(
                "Keep chin tucked to chest throughout movement to prevent lower back arching."
            ),
            recommendedSetsReps = "3 sets x 10 - 12 reps",
            diagramType = "HIP_THRUST"
        )
    )

    fun getExerciseById(id: String): Exercise? {
        return exercises.find { it.id == id }
    }

    fun filterExercises(
        muscleGroup: MuscleGroup? = null,
        equipment: Equipment = Equipment.ALL,
        difficulty: Difficulty? = null,
        query: String = ""
    ): List<Exercise> {
        return exercises.filter { ex ->
            val matchesMuscle = muscleGroup == null || ex.primaryMuscle == muscleGroup || ex.secondaryMuscles.contains(muscleGroup)
            val matchesEquipment = equipment == Equipment.ALL || ex.equipment == equipment
            val matchesDifficulty = difficulty == null || ex.difficulty == difficulty
            val matchesQuery = query.isBlank() || ex.name.contains(query, ignoreCase = true) || ex.primaryMuscle.displayName.contains(query, ignoreCase = true)
            matchesMuscle && matchesEquipment && matchesDifficulty && matchesQuery
        }
    }
}
