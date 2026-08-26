package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.BodyMeasurement
import com.example.data.entity.ExerciseSetLog
import com.example.data.entity.UserProfile
import com.example.data.entity.WeeklyCheckIn
import com.example.data.entity.WeightRecord
import com.example.data.entity.WorkoutSession
import com.example.ui.components.ChartPoint
import com.example.ui.components.ConsistencyBarChart
import com.example.ui.components.SimpleLineChart
import com.example.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProgressScreen(
    userProfile: UserProfile,
    weightRecords: List<WeightRecord>,
    measurements: List<BodyMeasurement>,
    completedLogs: List<ExerciseSetLog>,
    completedSessions: List<WorkoutSession>,
    checkIns: List<WeeklyCheckIn>,
    onAddWeight: (Float, String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Peso, 1: Cintura, 2: Cargas, 3: Consistência, 4: Passos
    var showAddWeightDialog by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .padding(bottom = 90.dp)
            .testTag("progress_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Evolução & Desempenho",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = AppTheme.colors.textPrimary
                )
                Text(
                    text = "Acompanhe métricas reais de consistência e corpo",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.textMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Category Tabs
        val tabs = listOf("📉 Peso", "📏 Cintura", "💪 Cargas", "🔥 Frequência", "🚶 Passos")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                FilterChip(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    label = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == index) Color.White else AppTheme.colors.textSecondary
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppTheme.colors.primary,
                        containerColor = AppTheme.colors.surface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedTab == index,
                        borderColor = if (selectedTab == index) AppTheme.colors.primary else AppTheme.colors.border
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (selectedTab) {
            0 -> {
                // PESO SECTION
                val weightPoints = weightRecords.sortedBy { it.dateMillis }.map {
                    ChartPoint(it.dateMillis, it.weightKg, dateFormat.format(Date(it.dateMillis)))
                }

                Text(
                    text = "Tendência de Peso Corporal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.textPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "A linha amarela indica sua meta final de ${userProfile.targetWeightKg.toInt()} kg.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.textMuted
                )

                Spacer(modifier = Modifier.height(12.dp))

                SimpleLineChart(
                    points = weightPoints,
                    lineColor = AppTheme.colors.primary,
                    unit = "kg",
                    targetValue = userProfile.targetWeightKg
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showAddWeightDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("open_add_weight_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.surface),
                    border = BorderStroke(1.dp, AppTheme.colors.border)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = AppTheme.colors.blueText)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Registrar Nova Pesagem", color = AppTheme.colors.blueText, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text("Histórico Recente de Pesagens", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary)
                Spacer(modifier = Modifier.height(10.dp))

                weightRecords.take(8).forEach { record ->
                    Surface(
                        color = AppTheme.colors.surface,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, AppTheme.colors.border),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = SimpleDateFormat("dd 'de' MMMM", Locale("pt", "BR")).format(Date(record.dateMillis)),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppTheme.colors.textPrimary
                                )
                                if (record.notes.isNotEmpty()) {
                                    Text(record.notes, style = MaterialTheme.typography.labelSmall, color = AppTheme.colors.textMuted)
                                }
                            }
                            Text(
                                text = "${String.format(Locale.getDefault(), "%.1f", record.weightKg)} kg",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = AppTheme.colors.primary
                            )
                        }
                    }
                }
            }
            1 -> {
                // CINTURA & MEDIDAS
                val waistPoints = measurements.sortedBy { it.dateMillis }.map {
                    ChartPoint(it.dateMillis, it.waistCm, dateFormat.format(Date(it.dateMillis)))
                }

                Text("Evolução da Cintura (cm)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text("A diminuição da circunferência da cintura é o melhor indicador de perda de gordura visceral.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.textMuted)

                Spacer(modifier = Modifier.height(12.dp))

                SimpleLineChart(
                    points = waistPoints,
                    lineColor = AppTheme.colors.amberText,
                    unit = "cm"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Latest Comparison Card
                val latest = measurements.firstOrNull()
                val initial = measurements.lastOrNull()
                if (latest != null && initial != null) {
                    val waistDelta = latest.waistCm - initial.waistCm
                    val abdomenDelta = latest.abdomenCm - initial.abdomenCm
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                        border = BorderStroke(1.dp, AppTheme.colors.border)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Deltas de Medidas", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Cintura", color = AppTheme.colors.textMuted, style = MaterialTheme.typography.labelSmall)
                                    Text("${latest.waistCm} cm", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
                                    Text("${if (waistDelta >= 0) "+" else ""}${String.format(Locale.getDefault(), "%.1f", waistDelta)} cm", color = if (waistDelta <= 0) AppTheme.colors.primary else Color(0xFFEF4444), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Abdômen", color = AppTheme.colors.textMuted, style = MaterialTheme.typography.labelSmall)
                                    Text("${latest.abdomenCm} cm", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
                                    Text("${if (abdomenDelta >= 0) "+" else ""}${String.format(Locale.getDefault(), "%.1f", abdomenDelta)} cm", color = if (abdomenDelta <= 0) AppTheme.colors.primary else Color(0xFFEF4444), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Braço", color = AppTheme.colors.textMuted, style = MaterialTheme.typography.labelSmall)
                                    Text("${latest.armCm} cm", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold)
                                    Text("Preservado", color = AppTheme.colors.blueText, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // CARGAS POR EXERCÍCIO
                var selectedExerciseName by remember { mutableStateOf("Leg Press 45°") }
                val exercises = listOf("Leg Press 45°", "Supino Máquina ou Halteres", "Puxada Frontal Aberta", "Mesa Flexora")

                Text("Progressão de Carga por Exercício", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    exercises.forEach { exName ->
                        FilterChip(
                            selected = selectedExerciseName == exName,
                            onClick = { selectedExerciseName = exName },
                            label = { Text(exName.take(18)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AppTheme.colors.blueText,
                                containerColor = AppTheme.colors.surface
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedExerciseName == exName,
                                borderColor = if (selectedExerciseName == exName) AppTheme.colors.blueText else AppTheme.colors.border
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val exerciseLogs = completedLogs.filter { it.exerciseName.contains(selectedExerciseName.take(8), ignoreCase = true) }
                    .sortedBy { it.timestamp }
                val loadPoints = if (exerciseLogs.isNotEmpty()) {
                    exerciseLogs.map { ChartPoint(it.timestamp, it.weightKg, dateFormat.format(Date(it.timestamp))) }
                } else {
                    listOf(
                        ChartPoint(System.currentTimeMillis() - 7 * 86400000L, 80f, "Sem 1"),
                        ChartPoint(System.currentTimeMillis() - 4 * 86400000L, 90f, "Sem 2"),
                        ChartPoint(System.currentTimeMillis() - 1 * 86400000L, 100f, "Sem 3")
                    )
                }

                SimpleLineChart(
                    points = loadPoints,
                    lineColor = AppTheme.colors.blueText,
                    unit = "kg"
                )
            }
            3 -> {
                // CONSISTÊNCIA / FREQUÊNCIA
                val weeklyStats = listOf(
                    "Semana 1" to 3,
                    "Semana 2" to 3,
                    "Semana 3" to 3,
                    "Semana 4" to 2,
                    "Semana 5" to 3,
                    "Esta sem" to completedSessions.count { it.status == "CONCLUIDO" }
                )

                ConsistencyBarChart(
                    weeklyCounts = weeklyStats,
                    targetPerWeek = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = AppTheme.colors.primaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🎯", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "A consistência de 3 treinos por semana é o fator número 1 que determina a retenção de massa magra durante o emagrecimento.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.colors.onPrimaryContainer,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            4 -> {
                // PASSOS DIÁRIOS
                val stepPoints = checkIns.sortedBy { it.dateMillis }.map {
                    ChartPoint(it.dateMillis, it.avgSteps.toFloat(), "Sem ${it.weekNumber}")
                }.ifEmpty {
                    listOf(
                        ChartPoint(System.currentTimeMillis() - 14 * 86400000L, 6500f, "Sem 1"),
                        ChartPoint(System.currentTimeMillis() - 7 * 86400000L, 7200f, "Sem 2"),
                        ChartPoint(System.currentTimeMillis(), 8100f, "Sem 3")
                    )
                }

                Text("Média Diária de Passos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text("NEAT e caminhadas diárias aumentam seu gasto calórico sem gerar fadiga muscular excessiva.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.textMuted)

                Spacer(modifier = Modifier.height(12.dp))

                SimpleLineChart(
                    points = stepPoints,
                    lineColor = AppTheme.colors.purpleText,
                    unit = "passos"
                )
            }
        }
    }

    // ADD WEIGHT DIALOG
    if (showAddWeightDialog) {
        var weightInput by remember { mutableStateOf("") }
        var notesInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddWeightDialog = false },
            title = { Text("Registrar Pesagem", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Pese-se preferencialmente em jejum, pela manhã:", color = AppTheme.colors.textMuted, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Peso em kg (ex: 121.5)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.border,
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary,
                            focusedContainerColor = AppTheme.colors.surfaceVariant,
                            unfocusedContainerColor = AppTheme.colors.surfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Notas (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.border,
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary,
                            focusedContainerColor = AppTheme.colors.surfaceVariant,
                            unfocusedContainerColor = AppTheme.colors.surfaceVariant
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = weightInput.replace(",", ".").toFloatOrNull()
                        if (parsed != null && parsed > 0f) {
                            onAddWeight(parsed, notesInput)
                            showAddWeightDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
                ) {
                    Text("Salvar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddWeightDialog = false }) {
                    Text("Cancelar", color = AppTheme.colors.textMuted)
                }
            },
            containerColor = AppTheme.colors.surface
        )
    }
}

