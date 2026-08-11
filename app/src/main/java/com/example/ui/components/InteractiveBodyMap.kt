package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BodyView
import com.example.data.model.Gender
import com.example.data.model.MuscleGroup

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveBodyMap(
    gender: Gender,
    bodyView: BodyView,
    selectedMuscle: MuscleGroup?,
    onGenderSelected: (Gender) -> Unit,
    onViewSelected: (BodyView) -> Unit,
    onMuscleSelected: (MuscleGroup?) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_body_map_card"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Controls: Gender & View Selectors
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Male / Female Toggle
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        selected = gender == Gender.MALE,
                        onClick = { onGenderSelected(Gender.MALE) },
                        shape = CircleShape,
                        color = if (gender == Gender.MALE) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (gender == Gender.MALE) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("gender_male_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Male,
                                contentDescription = "Male",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Male", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }

                    Surface(
                        selected = gender == Gender.FEMALE,
                        onClick = { onGenderSelected(Gender.FEMALE) },
                        shape = CircleShape,
                        color = if (gender == Gender.FEMALE) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (gender == Gender.FEMALE) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("gender_female_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Female,
                                contentDescription = "Female",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Female", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Front / Back View Toggle
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(4.dp)
                ) {
                    BodyView.entries.forEach { view ->
                        val isSelected = bodyView == view
                        Surface(
                            selected = isSelected,
                            onClick = { onViewSelected(view) },
                            shape = CircleShape,
                            color = if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag("view_${view.name.lowercase()}_btn")
                        ) {
                            Text(
                                text = view.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Body Map Diagram Canvas + Tap Areas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Interactive Body Canvas Diagram
                BodyCanvasGraphic(
                    gender = gender,
                    bodyView = bodyView,
                    selectedMuscle = selectedMuscle,
                    onMuscleTapped = { muscle -> onMuscleSelected(muscle) }
                )

                // Overlay Helper Instruction
                if (selectedMuscle == null) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.9f))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tap any muscle to isolate exercises",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Muscle Group Quick Chips for Easy Direct Access
            val availableMuscles = MuscleGroup.entries.filter { it.defaultView == bodyView }
            
            Text(
                text = "${bodyView.label} Muscle Groups:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // "All Muscles" chip
                FilterChip(
                    selected = selectedMuscle == null,
                    onClick = { onMuscleSelected(null) },
                    label = { Text("All (${availableMuscles.size})", style = MaterialTheme.typography.labelSmall) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("muscle_chip_all")
                )

                availableMuscles.forEach { muscle ->
                    val isSelected = selectedMuscle == muscle
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) onMuscleSelected(null) else onMuscleSelected(muscle)
                        },
                        label = {
                            Text(
                                text = muscle.displayName.split(" ").first(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(muscle.colorHex),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("muscle_chip_${muscle.id}")
                    )
                }
            }
        }
    }
}

@Composable
fun BodyCanvasGraphic(
    gender: Gender,
    bodyView: BodyView,
    selectedMuscle: MuscleGroup?,
    onMuscleTapped: (MuscleGroup) -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val activeGlowColor = MaterialTheme.colorScheme.tertiary
    val baseBodyColor = MaterialTheme.colorScheme.outlineVariant
    val bodyFillColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(gender, bodyView) {
                detectTapGestures { offset ->
                    val w = size.width
                    val h = size.height

                    // Normalize hit-testing relative coordinates (0..1)
                    val nx = offset.x / w
                    val ny = offset.y / h

                    val hitMuscle = detectMuscleHit(bodyView, nx, ny)
                    if (hitMuscle != null) {
                        onMuscleTapped(hitMuscle)
                    }
                }
            }
    ) {
        val w = size.width
        val h = size.height

        val cx = w / 2f

        // Draw Anatomical Body Frame Silhouette
        drawBodyOutline(cx, w, h, baseBodyColor, bodyFillColor)

        // Render Muscle Anatomical Regions for selected view
        if (bodyView == BodyView.FRONT) {
            // CHEST
            drawAnatomicalRegion(
                muscle = MuscleGroup.CHEST,
                isSelected = selectedMuscle == MuscleGroup.CHEST,
                bounds = Rect(cx - w * 0.18f, h * 0.18f, cx + w * 0.18f, h * 0.28f),
                label = "Chest",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.CHEST.colorHex)
            )

            // SHOULDERS
            drawAnatomicalRegion(
                muscle = MuscleGroup.SHOULDERS,
                isSelected = selectedMuscle == MuscleGroup.SHOULDERS,
                bounds = Rect(cx - w * 0.30f, h * 0.16f, cx + w * 0.30f, h * 0.24f),
                label = "Shoulders",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.SHOULDERS.colorHex)
            )

            // BICEPS
            drawAnatomicalRegion(
                muscle = MuscleGroup.BICEPS,
                isSelected = selectedMuscle == MuscleGroup.BICEPS,
                bounds = Rect(cx - w * 0.32f, h * 0.26f, cx + w * 0.32f, h * 0.38f),
                label = "Biceps",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.BICEPS.colorHex)
            )

            // ABS
            drawAnatomicalRegion(
                muscle = MuscleGroup.ABS,
                isSelected = selectedMuscle == MuscleGroup.ABS,
                bounds = Rect(cx - w * 0.12f, h * 0.29f, cx + w * 0.12f, h * 0.44f),
                label = "Abs",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.ABS.colorHex)
            )

            // QUADS
            drawAnatomicalRegion(
                muscle = MuscleGroup.QUADS,
                isSelected = selectedMuscle == MuscleGroup.QUADS,
                bounds = Rect(cx - w * 0.20f, h * 0.48f, cx + w * 0.20f, h * 0.72f),
                label = "Quads",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.QUADS.colorHex)
            )

            // CALVES
            drawAnatomicalRegion(
                muscle = MuscleGroup.CALVES,
                isSelected = selectedMuscle == MuscleGroup.CALVES,
                bounds = Rect(cx - w * 0.18f, h * 0.76f, cx + w * 0.18f, h * 0.92f),
                label = "Calves",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.CALVES.colorHex)
            )

        } else {
            // BACK VIEW
            // TRAPS
            drawAnatomicalRegion(
                muscle = MuscleGroup.TRAPS,
                isSelected = selectedMuscle == MuscleGroup.TRAPS,
                bounds = Rect(cx - w * 0.16f, h * 0.12f, cx + w * 0.16f, h * 0.22f),
                label = "Traps",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.TRAPS.colorHex)
            )

            // LATS
            drawAnatomicalRegion(
                muscle = MuscleGroup.LATS,
                isSelected = selectedMuscle == MuscleGroup.LATS,
                bounds = Rect(cx - w * 0.22f, h * 0.22f, cx + w * 0.22f, h * 0.38f),
                label = "Lats",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.LATS.colorHex)
            )

            // TRICEPS
            drawAnatomicalRegion(
                muscle = MuscleGroup.TRICEPS,
                isSelected = selectedMuscle == MuscleGroup.TRICEPS,
                bounds = Rect(cx - w * 0.32f, h * 0.24f, cx + w * 0.32f, h * 0.38f),
                label = "Triceps",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.TRICEPS.colorHex)
            )

            // GLUTES
            drawAnatomicalRegion(
                muscle = MuscleGroup.GLUTES,
                isSelected = selectedMuscle == MuscleGroup.GLUTES,
                bounds = Rect(cx - w * 0.20f, h * 0.44f, cx + w * 0.20f, h * 0.58f),
                label = "Glutes",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.GLUTES.colorHex)
            )

            // HAMSTRINGS
            drawAnatomicalRegion(
                muscle = MuscleGroup.HAMSTRINGS,
                isSelected = selectedMuscle == MuscleGroup.HAMSTRINGS,
                bounds = Rect(cx - w * 0.18f, h * 0.59f, cx + w * 0.18f, h * 0.74f),
                label = "Hamstrings",
                cx = cx, h = h, w = w,
                primaryColor = primaryColor,
                activeColor = Color(MuscleGroup.HAMSTRINGS.colorHex)
            )
        }
    }
}

private fun DrawScope.drawBodyOutline(
    cx: Float,
    w: Float,
    h: Float,
    outlineColor: Color,
    fillColor: Color
) {
    // Head
    drawCircle(
        color = fillColor,
        radius = h * 0.05f,
        center = Offset(cx, h * 0.08f)
    )
    drawCircle(
        color = outlineColor,
        radius = h * 0.05f,
        center = Offset(cx, h * 0.08f),
        style = Stroke(width = 2.dp.toPx())
    )

    // Neck
    drawRect(
        color = fillColor,
        topLeft = Offset(cx - w * 0.04f, h * 0.12f),
        size = Size(w * 0.08f, h * 0.04f)
    )

    // Main Torso Outline Path
    val bodyPath = Path().apply {
        // Left Shoulder to Hip to Feet
        moveTo(cx, h * 0.14f)
        lineTo(cx - w * 0.26f, h * 0.18f) // L shoulder
        lineTo(cx - w * 0.32f, h * 0.38f) // L arm
        lineTo(cx - w * 0.22f, h * 0.40f) // L wrist
        lineTo(cx - w * 0.18f, h * 0.46f) // L hip
        lineTo(cx - w * 0.18f, h * 0.74f) // L knee
        lineTo(cx - w * 0.12f, h * 0.94f) // L foot
        lineTo(cx - w * 0.02f, h * 0.94f) // C gap
        lineTo(cx, h * 0.48f)             // C crotch
        lineTo(cx + w * 0.02f, h * 0.94f) // R inner
        lineTo(cx + w * 0.12f, h * 0.94f) // R foot
        lineTo(cx + w * 0.18f, h * 0.74f) // R knee
        lineTo(cx + w * 0.18f, h * 0.46f) // R hip
        lineTo(cx + w * 0.22f, h * 0.40f) // R wrist
        lineTo(cx + w * 0.32f, h * 0.38f) // R arm
        lineTo(cx + w * 0.26f, h * 0.18f) // R shoulder
        close()
    }

    drawPath(path = bodyPath, color = fillColor)
    drawPath(path = bodyPath, color = outlineColor, style = Stroke(width = 2.5.dp.toPx()))
}

private fun DrawScope.drawAnatomicalRegion(
    muscle: MuscleGroup,
    isSelected: Boolean,
    bounds: Rect,
    label: String,
    cx: Float,
    h: Float,
    w: Float,
    primaryColor: Color,
    activeColor: Color
) {
    val fillColor = if (isSelected) activeColor.copy(alpha = 0.85f) else activeColor.copy(alpha = 0.25f)
    val strokeColor = if (isSelected) Color.White else activeColor

    // Draw Rounded Anatomical Highlight Box
    drawRoundRect(
        color = fillColor,
        topLeft = bounds.topLeft,
        size = bounds.size,
        cornerRadius = CornerRadius(16f, 16f)
    )

    drawRoundRect(
        color = strokeColor,
        topLeft = bounds.topLeft,
        size = bounds.size,
        cornerRadius = CornerRadius(16f, 16f),
        style = Stroke(width = if (isSelected) 3.5.dp.toPx() else 1.5.dp.toPx())
    )

    // Glow Effect if Selected
    if (isSelected) {
        drawRoundRect(
            color = activeColor.copy(alpha = 0.3f),
            topLeft = Offset(bounds.left - 6f, bounds.top - 6f),
            size = Size(bounds.width + 12f, bounds.height + 12f),
            cornerRadius = CornerRadius(20f, 20f),
            style = Stroke(width = 4.dp.toPx())
        )
    }
}

private fun detectMuscleHit(bodyView: BodyView, nx: Float, ny: Float): MuscleGroup? {
    // Normalised coordinates mapping (0.0 to 1.0)
    return if (bodyView == BodyView.FRONT) {
        when {
            ny in 0.16f..0.28f && nx in 0.32f..0.68f -> MuscleGroup.CHEST
            ny in 0.15f..0.25f && (nx in 0.18f..0.32f || nx in 0.68f..0.82f) -> MuscleGroup.SHOULDERS
            ny in 0.26f..0.40f && (nx in 0.15f..0.30f || nx in 0.70f..0.85f) -> MuscleGroup.BICEPS
            ny in 0.28f..0.46f && nx in 0.38f..0.62f -> MuscleGroup.ABS
            ny in 0.47f..0.74f && nx in 0.30f..0.70f -> MuscleGroup.QUADS
            ny in 0.75f..0.94f && nx in 0.32f..0.68f -> MuscleGroup.CALVES
            else -> null
        }
    } else {
        when {
            ny in 0.12f..0.22f && nx in 0.34f..0.66f -> MuscleGroup.TRAPS
            ny in 0.22f..0.38f && nx in 0.28f..0.72f -> MuscleGroup.LATS
            ny in 0.24f..0.40f && (nx in 0.15f..0.30f || nx in 0.70f..0.85f) -> MuscleGroup.TRICEPS
            ny in 0.42f..0.58f && nx in 0.30f..0.70f -> MuscleGroup.GLUTES
            ny in 0.59f..0.75f && nx in 0.32f..0.68f -> MuscleGroup.HAMSTRINGS
            ny in 0.76f..0.94f && nx in 0.32f..0.68f -> MuscleGroup.CALVES
            else -> null
        }
    }
}
