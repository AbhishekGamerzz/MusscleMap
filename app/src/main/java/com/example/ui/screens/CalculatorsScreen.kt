package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorsScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("1RM Calculator", "Macros & TDEE", "Body Fat %")

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("calculators_screen")
    ) {
        PrimaryTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("calc_tab_$index")
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (selectedTab) {
                0 -> OneRepMaxCalculator()
                1 -> MacroCalculator()
                2 -> BodyFatCalculator()
            }
        }
    }
}

@Composable
private fun OneRepMaxCalculator() {
    var weightInput by remember { mutableStateOf("100") }
    var repsInput by remember { mutableStateOf("5") }

    val weight = weightInput.toFloatOrNull() ?: 0f
    val reps = repsInput.toIntOrNull() ?: 0

    // Epley Formula: 1RM = Weight * (1 + Reps / 30)
    val epley1RM = if (reps > 0 && weight > 0) (weight * (1f + reps / 30f)).roundToInt() else 0
    // Brzycki Formula: 1RM = Weight * (36 / (37 - Reps))
    val brzycki1RM = if (reps in 1..12 && weight > 0) (weight * (36f / (37f - reps))).roundToInt() else epley1RM

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "One-Rep Max (1RM) Estimator",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Calculate your maximum strength capacity based on reps lifted",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Weight (kg / lbs)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("1rm_weight_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = repsInput,
                    onValueChange = { repsInput = it },
                    label = { Text("Reps Completed") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("1rm_reps_input"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1RM Estimated Output Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Estimated 1RM (Max)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$epley1RM kg",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Brzycki: $brzycki1RM kg",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Percentage Training Load Breakdown",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val percentages = listOf(
                95 to "1 - 2 Reps (Max Strength)",
                90 to "3 - 4 Reps (Power)",
                85 to "5 - 6 Reps (Strength/Hypertrophy)",
                80 to "7 - 8 Reps (Hypertrophy)",
                75 to "9 - 10 Reps (Hypertrophy)",
                70 to "11 - 12 Reps (Endurance)"
            )

            percentages.forEach { (pct, targetText) ->
                val targetWeight = (epley1RM * pct / 100f).roundToInt()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest
                        ) {
                            Text(
                                text = "$pct%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(targetText, style = MaterialTheme.typography.bodySmall)
                    }

                    Text(
                        text = "$targetWeight kg",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MacroCalculator() {
    var genderIndex by remember { mutableIntStateOf(0) } // 0 Male, 1 Female
    var ageInput by remember { mutableStateOf("25") }
    var weightInput by remember { mutableStateOf("75") }
    var heightInput by remember { mutableStateOf("178") }
    var goalIndex by remember { mutableIntStateOf(1) } // 0 Fat Loss, 1 Maintenance, 2 Muscle Gain

    val age = ageInput.toIntOrNull() ?: 25
    val weight = weightInput.toFloatOrNull() ?: 75f
    val height = heightInput.toFloatOrNull() ?: 178f

    // Mifflin-St Jeor BMR Formula
    val bmr = if (genderIndex == 0) {
        (10 * weight + 6.25 * height - 5 * age + 5).roundToInt()
    } else {
        (10 * weight + 6.25 * height - 5 * age - 161).roundToInt()
    }

    // TDEE (Moderate Activity x1.55)
    val tdee = (bmr * 1.55f).roundToInt()

    val targetCalories = when (goalIndex) {
        0 -> tdee - 500 // Fat Loss
        2 -> tdee + 350 // Muscle Gain
        else -> tdee
    }

    // Macros: Protein 2.0g/kg, Fat 25% cals, Carbs remainder
    val proteinGrams = (weight * 2.0f).roundToInt()
    val fatGrams = ((targetCalories * 0.25f) / 9f).roundToInt()
    val carbGrams = ((targetCalories - (proteinGrams * 4 + fatGrams * 9)) / 4f).coerceAtLeast(50f).roundToInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "TDEE & Macro Nutrition Calculator",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Daily Calorie & Macro targets for your fitness goals",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Goal Selection
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                val goals = listOf("Fat Loss", "Maintenance", "Muscle Gain")
                goals.forEachIndexed { idx, label ->
                    SegmentedButton(
                        selected = goalIndex == idx,
                        onClick = { goalIndex = idx },
                        shape = SegmentedButtonDefaults.itemShape(index = idx, count = goals.size),
                        modifier = Modifier.testTag("macro_goal_$idx")
                    ) {
                        Text(label, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = ageInput,
                    onValueChange = { ageInput = it },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it },
                    label = { Text("Height (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Calories Summary Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Daily Target Calories",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "$targetCalories kcal",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("BMR: $bmr kcal", style = MaterialTheme.typography.labelSmall)
                        Text("TDEE: $tdee kcal", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Macro Split
            Text("Recommended Daily Macros", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MacroCard(title = "Protein", value = "${proteinGrams}g", color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                MacroCard(title = "Carbs", value = "${carbGrams}g", color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
                MacroCard(title = "Fats", value = "${fatGrams}g", color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MacroCard(title: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun BodyFatCalculator() {
    var waistInput by remember { mutableStateOf("82") }
    var neckInput by remember { mutableStateOf("38") }
    var heightInput by remember { mutableStateOf("178") }

    val waist = waistInput.toFloatOrNull() ?: 82f
    val neck = neckInput.toFloatOrNull() ?: 38f
    val height = heightInput.toFloatOrNull() ?: 178f

    // US Navy Method Body Fat Estimator
    val bfPercent = if (waist > neck && height > 0) {
        val valIn = 86.010 * Math.log10((waist - neck).toDouble()) - 70.041 * Math.log10(height.toDouble()) + 36.76
        valIn.coerceIn(5.0, 45.0).roundToInt()
    } else 15

    val category = when {
        bfPercent < 10 -> "Essential / Lean Athlete"
        bfPercent in 10..15 -> "Athletic / Fit"
        bfPercent in 16..20 -> "Average Healthy"
        else -> "Above Average"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Navy Body Fat % Estimator",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Tape measure method using waist, neck, and height",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = waistInput,
                    onValueChange = { waistInput = it },
                    label = { Text("Waist (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = neckInput,
                    onValueChange = { neckInput = it },
                    label = { Text("Neck (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it },
                    label = { Text("Height (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Estimated Body Fat", style = MaterialTheme.typography.labelMedium)
                    Text("$bfPercent%", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(category, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
