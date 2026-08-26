package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseIllustration(
    exerciseName: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    val emerald = Color(0xFF10B981)
    val teal = Color(0xFF06B6D4)
    val amber = Color(0xFFF59E0B)
    val slateDark = Color(0xFF1E293B)
    val slateLight = Color(0xFF334155)
    val metalGray = Color(0xFF94A3B8)
    val accentRed = Color(0xFFEF4444)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize(0.85f)) {
            val w = this.size.width
            val h = this.size.height

            val nameLower = exerciseName.lowercase()
            when {
                nameLower.contains("leg press") -> {
                    drawLegPress(w, h, emerald, slateLight, metalGray)
                }
                nameLower.contains("supino") || nameLower.contains("chest press") -> {
                    drawChestPress(w, h, teal, slateLight, metalGray)
                }
                nameLower.contains("puxada") || nameLower.contains("pulldown") -> {
                    drawLatPulldown(w, h, emerald, slateLight, metalGray)
                }
                nameLower.contains("flexora") -> {
                    drawLegCurl(w, h, amber, slateLight, metalGray)
                }
                nameLower.contains("smith") || nameLower.contains("agachamento") -> {
                    drawSmithSquat(w, h, emerald, slateLight, metalGray)
                }
                nameLower.contains("remada") -> {
                    drawSeatedRow(w, h, teal, slateLight, metalGray)
                }
                nameLower.contains("extensora") -> {
                    drawLegExtension(w, h, emerald, slateLight, metalGray)
                }
                nameLower.contains("desenvolvimento") || nameLower.contains("ombro") -> {
                    drawShoulderPress(w, h, amber, slateLight, metalGray)
                }
                nameLower.contains("lateral") || nameLower.contains("elevação") -> {
                    drawLateralRaise(w, h, teal, slateLight, metalGray)
                }
                nameLower.contains("tríceps") || nameLower.contains("triceps") -> {
                    drawTricepsPushdown(w, h, emerald, slateLight, metalGray)
                }
                nameLower.contains("rosca") || nameLower.contains("biceps") || nameLower.contains("bíceps") -> {
                    drawBicepsCurl(w, h, amber, slateLight, metalGray)
                }
                nameLower.contains("terra") || nameLower.contains("rdl") || nameLower.contains("romeno") -> {
                    drawRDL(w, h, amber, slateLight, metalGray)
                }
                nameLower.contains("core") || nameLower.contains("pallof") || nameLower.contains("abdominal") || nameLower.contains("prancha") -> {
                    drawCoreExercise(w, h, emerald, slateLight, metalGray)
                }
                nameLower.contains("panturrilha") -> {
                    drawCalfRaise(w, h, teal, slateLight, metalGray)
                }
                else -> {
                    drawGenericBarbell(w, h, emerald, slateLight, metalGray)
                }
            }
        }
    }
}

// Drawing helper functions for crisp equipment representations

private fun DrawScope.drawLegPress(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // 45° Frame
    drawLine(frame, Offset(w * 0.15f, h * 0.85f), Offset(w * 0.85f, h * 0.15f), strokeWidth = 5f, cap = StrokeCap.Round)
    // Sled
    drawRoundRect(accent, Offset(w * 0.45f, h * 0.35f), Size(w * 0.28f, h * 0.16f), CornerRadius(4f, 4f))
    // Footplate
    drawLine(metal, Offset(w * 0.65f, h * 0.25f), Offset(w * 0.85f, h * 0.45f), strokeWidth = 6f, cap = StrokeCap.Round)
    // Weight plate on sled
    drawCircle(Color(0xFF34D399), radius = w * 0.09f, center = Offset(w * 0.52f, h * 0.48f))
    // Seat base
    drawLine(metal, Offset(w * 0.15f, h * 0.85f), Offset(w * 0.35f, h * 0.85f), strokeWidth = 4f)
    drawRoundRect(metal, Offset(w * 0.2f, h * 0.65f), Size(w * 0.18f, h * 0.22f), CornerRadius(4f, 4f))
}

private fun DrawScope.drawChestPress(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Backrest and seat
    drawRoundRect(frame, Offset(w * 0.25f, h * 0.3f), Size(w * 0.12f, h * 0.5f), CornerRadius(4f, 4f))
    drawRoundRect(frame, Offset(w * 0.25f, h * 0.75f), Size(w * 0.35f, h * 0.1f), CornerRadius(4f, 4f))
    // Press levers
    drawLine(metal, Offset(w * 0.35f, h * 0.35f), Offset(w * 0.65f, h * 0.45f), strokeWidth = 4f, cap = StrokeCap.Round)
    drawLine(accent, Offset(w * 0.65f, h * 0.35f), Offset(w * 0.65f, h * 0.55f), strokeWidth = 6f, cap = StrokeCap.Round)
    // Weight stack column
    drawRoundRect(frame, Offset(w * 0.78f, h * 0.2f), Size(w * 0.15f, h * 0.65f), CornerRadius(3f, 3f))
    drawRect(accent, Offset(w * 0.8f, h * 0.45f), Size(w * 0.11f, h * 0.25f))
}

private fun DrawScope.drawLatPulldown(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // High pulley beam
    drawLine(frame, Offset(w * 0.5f, h * 0.15f), Offset(w * 0.8f, h * 0.15f), strokeWidth = 5f, cap = StrokeCap.Round)
    // Pulley wheel
    drawCircle(metal, radius = w * 0.06f, center = Offset(w * 0.5f, h * 0.15f))
    // Cable down to bar
    drawLine(metal, Offset(w * 0.5f, h * 0.15f), Offset(w * 0.5f, h * 0.35f), strokeWidth = 2.5f)
    // Wide curved Lat Bar
    val barPath = Path().apply {
        moveTo(w * 0.18f, h * 0.42f)
        quadraticTo(w * 0.5f, h * 0.33f, w * 0.82f, h * 0.42f)
    }
    drawPath(barPath, accent, style = Stroke(width = 5f, cap = StrokeCap.Round))
    // Seat
    drawRoundRect(frame, Offset(w * 0.35f, h * 0.7f), Size(w * 0.3f, h * 0.12f), CornerRadius(3f, 3f))
    // Leg pads
    drawRoundRect(metal, Offset(w * 0.4f, h * 0.58f), Size(w * 0.2f, h * 0.08f), CornerRadius(3f, 3f))
}

private fun DrawScope.drawLegCurl(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Bench angled
    drawLine(frame, Offset(w * 0.15f, h * 0.65f), Offset(w * 0.7f, h * 0.65f), strokeWidth = 6f, cap = StrokeCap.Round)
    // Lever arm
    drawLine(metal, Offset(w * 0.65f, h * 0.65f), Offset(w * 0.82f, h * 0.42f), strokeWidth = 4f, cap = StrokeCap.Round)
    // Roller pad
    drawCircle(accent, radius = w * 0.09f, center = Offset(w * 0.82f, h * 0.42f))
    // Frame support
    drawLine(frame, Offset(w * 0.25f, h * 0.65f), Offset(w * 0.25f, h * 0.88f), strokeWidth = 4f)
    drawLine(frame, Offset(w * 0.65f, h * 0.65f), Offset(w * 0.65f, h * 0.88f), strokeWidth = 4f)
}

private fun DrawScope.drawSmithSquat(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Two vertical guide rails
    drawLine(frame, Offset(w * 0.25f, h * 0.15f), Offset(w * 0.25f, h * 0.88f), strokeWidth = 4f)
    drawLine(frame, Offset(w * 0.75f, h * 0.15f), Offset(w * 0.75f, h * 0.88f), strokeWidth = 4f)
    // Top & bottom crossbars
    drawLine(frame, Offset(w * 0.18f, h * 0.15f), Offset(w * 0.82f, h * 0.15f), strokeWidth = 4f)
    drawLine(frame, Offset(w * 0.18f, h * 0.88f), Offset(w * 0.82f, h * 0.88f), strokeWidth = 5f)
    // Barbell fixed on rails
    drawLine(metal, Offset(w * 0.15f, h * 0.48f), Offset(w * 0.85f, h * 0.48f), strokeWidth = 4.5f, cap = StrokeCap.Round)
    // Weights on both ends
    drawRoundRect(accent, Offset(w * 0.16f, h * 0.4f), Size(w * 0.08f, h * 0.16f), CornerRadius(2f, 2f))
    drawRoundRect(accent, Offset(w * 0.76f, h * 0.4f), Size(w * 0.08f, h * 0.16f), CornerRadius(2f, 2f))
}

private fun DrawScope.drawSeatedRow(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Seat & chest pad
    drawRoundRect(frame, Offset(w * 0.2f, h * 0.65f), Size(w * 0.28f, h * 0.12f), CornerRadius(3f, 3f))
    drawRoundRect(metal, Offset(w * 0.45f, h * 0.35f), Size(w * 0.08f, h * 0.35f), CornerRadius(3f, 3f))
    // Pulley cable & grip
    drawLine(metal, Offset(w * 0.53f, h * 0.5f), Offset(w * 0.8f, h * 0.5f), strokeWidth = 3f)
    drawRoundRect(accent, Offset(w * 0.48f, h * 0.42f), Size(w * 0.07f, h * 0.16f), CornerRadius(2f, 2f))
    // Weight stack
    drawRoundRect(frame, Offset(w * 0.78f, h * 0.25f), Size(w * 0.14f, h * 0.6f), CornerRadius(3f, 3f))
    drawRect(accent, Offset(w * 0.8f, h * 0.45f), Size(w * 0.1f, h * 0.25f))
}

private fun DrawScope.drawLegExtension(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Seat and backrest
    drawRoundRect(frame, Offset(w * 0.22f, h * 0.35f), Size(w * 0.1f, h * 0.4f), CornerRadius(3f, 3f))
    drawRoundRect(frame, Offset(w * 0.22f, h * 0.68f), Size(w * 0.38f, h * 0.1f), CornerRadius(3f, 3f))
    // Lever arm pivoting down
    drawLine(metal, Offset(w * 0.58f, h * 0.68f), Offset(w * 0.65f, h * 0.85f), strokeWidth = 4f, cap = StrokeCap.Round)
    // Shin roller
    drawCircle(accent, radius = w * 0.08f, center = Offset(w * 0.65f, h * 0.85f))
}

private fun DrawScope.drawShoulderPress(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Seat
    drawRoundRect(frame, Offset(w * 0.38f, h * 0.45f), Size(w * 0.1f, h * 0.4f), CornerRadius(3f, 3f))
    drawRoundRect(frame, Offset(w * 0.38f, h * 0.8f), Size(w * 0.24f, h * 0.08f), CornerRadius(3f, 3f))
    // Overhead press levers
    drawLine(metal, Offset(w * 0.25f, h * 0.6f), Offset(w * 0.25f, h * 0.22f), strokeWidth = 4f, cap = StrokeCap.Round)
    drawLine(metal, Offset(w * 0.75f, h * 0.6f), Offset(w * 0.75f, h * 0.22f), strokeWidth = 4f, cap = StrokeCap.Round)
    // Handles
    drawRoundRect(accent, Offset(w * 0.2f, h * 0.2f), Size(w * 0.12f, h * 0.07f), CornerRadius(2f, 2f))
    drawRoundRect(accent, Offset(w * 0.68f, h * 0.2f), Size(w * 0.12f, h * 0.07f), CornerRadius(2f, 2f))
}

private fun DrawScope.drawLateralRaise(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Torso silhouette
    drawLine(metal, Offset(w * 0.5f, h * 0.25f), Offset(w * 0.5f, h * 0.75f), strokeWidth = 5f, cap = StrokeCap.Round)
    drawCircle(metal, radius = w * 0.08f, center = Offset(w * 0.5f, h * 0.18f))
    // Arms raised laterally
    drawLine(accent, Offset(w * 0.5f, h * 0.35f), Offset(w * 0.2f, h * 0.38f), strokeWidth = 4f, cap = StrokeCap.Round)
    drawLine(accent, Offset(w * 0.5f, h * 0.35f), Offset(w * 0.8f, h * 0.38f), strokeWidth = 4f, cap = StrokeCap.Round)
    // Dumbbells in hands
    drawRoundRect(Color(0xFFF59E0B), Offset(w * 0.15f, h * 0.32f), Size(w * 0.07f, h * 0.12f), CornerRadius(2f, 2f))
    drawRoundRect(Color(0xFFF59E0B), Offset(w * 0.78f, h * 0.32f), Size(w * 0.07f, h * 0.12f), CornerRadius(2f, 2f))
}

private fun DrawScope.drawTricepsPushdown(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Pulley wheel top
    drawCircle(metal, radius = w * 0.07f, center = Offset(w * 0.5f, h * 0.15f))
    // Cable
    drawLine(metal, Offset(w * 0.5f, h * 0.15f), Offset(w * 0.5f, h * 0.45f), strokeWidth = 2.5f)
    // V-bar / rope attachment
    val path = Path().apply {
        moveTo(w * 0.35f, h * 0.6f)
        lineTo(w * 0.5f, h * 0.45f)
        lineTo(w * 0.65f, h * 0.6f)
    }
    drawPath(path, accent, style = Stroke(width = 5f, cap = StrokeCap.Round))
    // Knots
    drawCircle(Color(0xFFF59E0B), radius = w * 0.05f, center = Offset(w * 0.35f, h * 0.6f))
    drawCircle(Color(0xFFF59E0B), radius = w * 0.05f, center = Offset(w * 0.65f, h * 0.6f))
}

private fun DrawScope.drawBicepsCurl(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Arm curled
    val armPath = Path().apply {
        moveTo(w * 0.4f, h * 0.25f)
        lineTo(w * 0.4f, h * 0.6f)
        lineTo(w * 0.65f, h * 0.35f)
    }
    drawPath(armPath, frame, style = Stroke(width = 6f, cap = StrokeCap.Round))
    // Dumbbell held in hand
    drawLine(metal, Offset(w * 0.55f, h * 0.35f), Offset(w * 0.75f, h * 0.35f), strokeWidth = 3f)
    drawRoundRect(accent, Offset(w * 0.55f, h * 0.26f), Size(w * 0.06f, h * 0.18f), CornerRadius(2f, 2f))
    drawRoundRect(accent, Offset(w * 0.72f, h * 0.26f), Size(w * 0.06f, h * 0.18f), CornerRadius(2f, 2f))
}

private fun DrawScope.drawRDL(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Hinged figure at 45°
    drawLine(frame, Offset(w * 0.4f, h * 0.85f), Offset(w * 0.4f, h * 0.55f), strokeWidth = 5f, cap = StrokeCap.Round) // legs
    drawLine(frame, Offset(w * 0.4f, h * 0.55f), Offset(w * 0.7f, h * 0.4f), strokeWidth = 5f, cap = StrokeCap.Round) // spine
    // Arms hanging down with dumbbells
    drawLine(metal, Offset(w * 0.65f, h * 0.43f), Offset(w * 0.55f, h * 0.7f), strokeWidth = 3.5f, cap = StrokeCap.Round)
    // Dumbbell
    drawRoundRect(accent, Offset(w * 0.5f, h * 0.68f), Size(w * 0.12f, h * 0.08f), CornerRadius(2f, 2f))
}

private fun DrawScope.drawCoreExercise(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Plank / Core shield symbol
    val shieldPath = Path().apply {
        moveTo(w * 0.5f, h * 0.2f)
        lineTo(w * 0.8f, h * 0.35f)
        lineTo(w * 0.7f, h * 0.75f)
        lineTo(w * 0.5f, h * 0.88f)
        lineTo(w * 0.3f, h * 0.75f)
        lineTo(w * 0.2f, h * 0.35f)
        close()
    }
    drawPath(shieldPath, Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF047857))))
    // Center lightning / strength mark
    val markPath = Path().apply {
        moveTo(w * 0.52f, h * 0.32f)
        lineTo(w * 0.42f, h * 0.52f)
        lineTo(w * 0.52f, h * 0.52f)
        lineTo(w * 0.46f, h * 0.72f)
        lineTo(w * 0.6f, h * 0.48f)
        lineTo(w * 0.5f, h * 0.48f)
        close()
    }
    drawPath(markPath, Color.White)
}

private fun DrawScope.drawCalfRaise(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Step platform
    drawRoundRect(frame, Offset(w * 0.2f, h * 0.75f), Size(w * 0.6f, h * 0.12f), CornerRadius(3f, 3f))
    // Foot on ball of foot elevated
    val footPath = Path().apply {
        moveTo(w * 0.35f, h * 0.75f)
        lineTo(w * 0.55f, h * 0.55f)
        lineTo(w * 0.6f, h * 0.7f)
    }
    drawPath(footPath, accent, style = Stroke(width = 5f, cap = StrokeCap.Round))
    // Arrow pointing up
    val arrowPath = Path().apply {
        moveTo(w * 0.75f, h * 0.5f)
        lineTo(w * 0.75f, h * 0.3f)
        moveTo(w * 0.68f, h * 0.37f)
        lineTo(w * 0.75f, h * 0.3f)
        lineTo(w * 0.82f, h * 0.37f)
    }
    drawPath(arrowPath, Color(0xFF34D399), style = Stroke(width = 3.5f, cap = StrokeCap.Round))
}

private fun DrawScope.drawGenericBarbell(w: Float, h: Float, accent: Color, frame: Color, metal: Color) {
    // Bar
    drawLine(Color.White, Offset(w * 0.15f, h * 0.5f), Offset(w * 0.85f, h * 0.5f), strokeWidth = 4f, cap = StrokeCap.Round)
    // Left Plates
    drawRoundRect(accent, Offset(w * 0.2f, h * 0.28f), Size(w * 0.08f, h * 0.44f), CornerRadius(3f, 3f))
    drawRoundRect(frame, Offset(w * 0.28f, h * 0.34f), Size(w * 0.05f, h * 0.32f), CornerRadius(2f, 2f))
    // Right Plates
    drawRoundRect(frame, Offset(w * 0.67f, h * 0.34f), Size(w * 0.05f, h * 0.32f), CornerRadius(2f, 2f))
    drawRoundRect(accent, Offset(w * 0.72f, h * 0.28f), Size(w * 0.08f, h * 0.44f), CornerRadius(3f, 3f))
}
