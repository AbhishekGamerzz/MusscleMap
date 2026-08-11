package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun VisualExerciseDiagram(
    diagramType: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "exercise_motion")
    val motionProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "motion_anim"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val accentColor = MaterialTheme.colorScheme.tertiary
    val trackColor = MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f))
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            when (diagramType) {
                "BARBELL_PRESS", "DUMBBELL_PRESS" -> {
                    // Bench / Press Motion (Vertical Barbell Movement)
                    val startY = h * 0.75f
                    val endY = h * 0.25f
                    val currentY = startY + (endY - startY) * motionProgress

                    // Motion track line
                    drawLine(
                        color = trackColor,
                        start = Offset(cx, startY),
                        end = Offset(cx, endY),
                        strokeWidth = 4.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )

                    // Barbell line
                    val barWidth = w * 0.5f
                    drawLine(
                        color = primaryColor,
                        start = Offset(cx - barWidth / 2f, currentY),
                        end = Offset(cx + barWidth / 2f, currentY),
                        strokeWidth = 10.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Weight plates
                    drawCircle(
                        color = accentColor,
                        radius = 16.dp.toPx(),
                        center = Offset(cx - barWidth / 2f, currentY)
                    )
                    drawCircle(
                        color = accentColor,
                        radius = 16.dp.toPx(),
                        center = Offset(cx + barWidth / 2f, currentY)
                    )
                }

                "ARM_CURL" -> {
                    // Bicep Curl Arc
                    val startAngle = 180f
                    val sweepAngle = -110f * motionProgress

                    val arcRect = androidx.compose.ui.geometry.Rect(
                        cx - w * 0.25f, cy - h * 0.35f,
                        cx + w * 0.25f, cy + h * 0.35f
                    )

                    // Track arc
                    drawArc(
                        color = trackColor,
                        startAngle = 180f,
                        sweepAngle = -110f,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
                    )

                    // Active motion arc
                    drawArc(
                        color = primaryColor,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Dumbbell icon head at tip of arc
                    val radians = Math.toRadians((startAngle + sweepAngle).toDouble())
                    val rx = cx + (w * 0.25f) * Math.cos(radians).toFloat()
                    val ry = cy + (h * 0.35f) * Math.sin(radians).toFloat()

                    drawCircle(color = accentColor, radius = 14.dp.toPx(), center = Offset(rx, ry))
                }

                "PULL_UP", "CABLE_PULL" -> {
                    // Vertical Pulling Track
                    val startY = h * 0.20f
                    val endY = h * 0.75f
                    val currentY = startY + (endY - startY) * motionProgress

                    // Fixed Overhead Anchor
                    drawLine(
                        color = trackColor,
                        start = Offset(cx - w * 0.3f, startY),
                        end = Offset(cx + w * 0.3f, startY),
                        strokeWidth = 8.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Cable or Body line
                    drawLine(
                        color = primaryColor,
                        start = Offset(cx, startY),
                        end = Offset(cx, currentY),
                        strokeWidth = 6.dp.toPx()
                    )

                    // Handle / Grip Bar
                    drawLine(
                        color = accentColor,
                        start = Offset(cx - w * 0.2f, currentY),
                        end = Offset(cx + w * 0.2f, currentY),
                        strokeWidth = 10.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                "LEG_SQUAT", "HIP_THRUST" -> {
                    // Squat / Hip Thrust Vertical Arc
                    val startY = h * 0.30f
                    val endY = h * 0.80f
                    val currentY = startY + (endY - startY) * (1f - motionProgress)

                    // Leg platform
                    drawLine(
                        color = trackColor,
                        start = Offset(cx - w * 0.35f, h * 0.85f),
                        end = Offset(cx + w * 0.35f, h * 0.85f),
                        strokeWidth = 8.dp.toPx()
                    )

                    // Moving Barbell / Torso center
                    drawCircle(
                        color = primaryColor,
                        radius = 24.dp.toPx(),
                        center = Offset(cx, currentY)
                    )

                    drawCircle(
                        color = Color.White,
                        radius = 10.dp.toPx(),
                        center = Offset(cx, currentY)
                    )
                }

                else -> {
                    // Default Motion Wave
                    val wavePath = Path().apply {
                        moveTo(w * 0.1f, cy)
                        quadraticTo(cx, cy - h * 0.3f * motionProgress, w * 0.9f, cy)
                    }

                    drawPath(
                        path = wavePath,
                        color = primaryColor,
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
        }
    }
}
