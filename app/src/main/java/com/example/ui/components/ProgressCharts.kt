package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChartPoint(
    val timestamp: Long,
    val value: Float,
    val label: String
)

@Composable
fun SimpleLineChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF10B981),
    gradientColor: Color = Color(0xFF10B981).copy(alpha = 0.2f),
    unit: String = "kg",
    targetValue: Float? = null
) {
    if (points.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E293B)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nenhum dado registrado para o período",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )
        }
        return
    }

    val minVal = (points.minOfOrNull { it.value } ?: 0f) - 1f
    val maxVal = (points.maxOfOrNull { it.value } ?: 100f) + 1f
    val range = maxOf(1f, maxVal - minVal)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E293B))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Último: ${String.format(Locale.getDefault(), "%.1f", points.last().value)} $unit",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                val diff = points.last().value - points.first().value
                val diffText = if (diff >= 0) "+${String.format(Locale.getDefault(), "%.1f", diff)}" else String.format(Locale.getDefault(), "%.1f", diff)
                Text(
                    text = "$diffText $unit",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (diff <= 0) Color(0xFF34D399) else Color(0xFFFBBF24),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Canvas Line Drawing
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val w = size.width
                val h = size.height

                // Draw horizontal guide lines
                for (i in 0..3) {
                    val y = h * (i / 3f)
                    drawLine(
                        color = Color(0xFF334155),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1f
                    )
                }

                // If target line
                if (targetValue != null && targetValue in minVal..maxVal) {
                    val targetY = h - ((targetValue - minVal) / range * h)
                    drawLine(
                        color = Color(0xFFF59E0B),
                        start = Offset(0f, targetY),
                        end = Offset(w, targetY),
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                }

                if (points.size == 1) {
                    val pt = points.first()
                    val y = h - ((pt.value - minVal) / range * h)
                    drawCircle(lineColor, radius = 6.dp.toPx(), center = Offset(w / 2, y))
                    return@Canvas
                }

                val coords = points.mapIndexed { index, pt ->
                    val x = (index.toFloat() / (points.size - 1)) * w
                    val y = h - ((pt.value - minVal) / range * h)
                    Offset(x, y)
                }

                // Smooth Path
                val strokePath = Path().apply {
                    moveTo(coords.first().x, coords.first().y)
                    for (i in 1 until coords.size) {
                        val prev = coords[i - 1]
                        val curr = coords[i]
                        val cx = (prev.x + curr.x) / 2
                        cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
                    }
                }

                // Filled gradient under path
                val fillPath = Path().apply {
                    addPath(strokePath)
                    lineTo(coords.last().x, h)
                    lineTo(coords.first().x, h)
                    close()
                }

                drawPath(
                    fillPath,
                    Brush.verticalGradient(
                        colors = listOf(gradientColor, Color.Transparent),
                        startY = 0f,
                        endY = h
                    )
                )

                drawPath(
                    strokePath,
                    color = lineColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                // Draw points
                coords.forEach { offset ->
                    drawCircle(Color(0xFF0F172A), radius = 5.dp.toPx(), center = offset)
                    drawCircle(lineColor, radius = 3.5.dp.toPx(), center = offset)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // X-axis labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(points.first().label, color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
                if (points.size > 2) {
                    Text(points[points.size / 2].label, color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
                }
                Text(points.last().label, color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun ConsistencyBarChart(
    weeklyCounts: List<Pair<String, Int>>,
    targetPerWeek: Int = 3,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E293B))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Consistência Semanal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Meta: $targetPerWeek treinos/sem",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF38BDF8),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                val maxBar = maxOf(4, (weeklyCounts.maxOfOrNull { it.second } ?: 3) + 1)

                weeklyCounts.takeLast(6).forEach { (weekLabel, count) ->
                    val fraction = (count.toFloat() / maxBar).coerceIn(0.1f, 1f)
                    val isTargetMet = count >= targetPerWeek

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "$count",
                            color = if (isTargetMet) Color(0xFF34D399) else Color(0xFFCBD5E1),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .height((80 * fraction).dp)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    if (isTargetMet) {
                                        Brush.verticalGradient(listOf(Color(0xFF10B981), Color(0xFF047857)))
                                    } else {
                                        Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0369A1)))
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = weekLabel,
                            color = Color(0xFF94A3B8),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
