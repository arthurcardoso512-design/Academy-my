package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.UserProfile
import com.example.data.entity.WeeklyCheckIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeeklyCheckInScreen(
    userProfile: UserProfile,
    checkIns: List<WeeklyCheckIn>,
    onSubmitCheckIn: (Float, Int, Int, Int, Int, Int, Boolean, String, String) -> Unit
) {
    var avgWeight by remember { mutableStateOf(userProfile.currentWeightKg.toString()) }
    var avgSteps by remember { mutableStateOf("7500") }
    var workoutsCompleted by remember { mutableIntStateOf(3) }
    var energyScore by remember { mutableFloatStateOf(8f) }
    var sleepScore by remember { mutableFloatStateOf(7f) }
    var hungerScore by remember { mutableFloatStateOf(5f) }
    var hasPain by remember { mutableStateOf(false) }
    var painLocation by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var checkInSavedMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .padding(bottom = 90.dp)
            .testTag("weekly_checkin_screen")
    ) {
        Text(
            text = "Check-in Semanal",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = Color(0xFF1A1C1E)
        )
        Text(
            text = "Avaliação global de consistência, recuperação e energia",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF74777F)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (checkInSavedMessage != null) {
            Surface(
                color = Color(0xFFECFDF5),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF047857))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(checkInSavedMessage!!, color = Color(0xFF047857), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE1E2E9)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Questionário da Semana",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Average Weight
                Text("Média de Peso da Semana (kg)", style = MaterialTheme.typography.labelMedium, color = Color(0xFF44474E), fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = avgWeight,
                    onValueChange = { avgWeight = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10B981),
                        unfocusedBorderColor = Color(0xFFE1E2E9),
                        focusedTextColor = Color(0xFF1A1C1E),
                        unfocusedTextColor = Color(0xFF1A1C1E),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Daily Steps Average
                Text("Média de Passos Diários", style = MaterialTheme.typography.labelMedium, color = Color(0xFF44474E), fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = avgSteps,
                    onValueChange = { avgSteps = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10B981),
                        unfocusedBorderColor = Color(0xFFE1E2E9),
                        focusedTextColor = Color(0xFF1A1C1E),
                        unfocusedTextColor = Color(0xFF1A1C1E),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Workouts completed selector
                Text("Treinos Realizados nesta semana", style = MaterialTheme.typography.labelMedium, color = Color(0xFF44474E), fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (0..4).forEach { count ->
                        FilterChip(
                            selected = workoutsCompleted == count,
                            onClick = { workoutsCompleted = count },
                            label = { Text("$count", fontWeight = FontWeight.Bold, color = if (workoutsCompleted == count) Color.White else Color(0xFF1A1C1E)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF10B981),
                                containerColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = workoutsCompleted == count,
                                borderColor = if (workoutsCompleted == count) Color(0xFF10B981) else Color(0xFFE1E2E9)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sliders: Energy, Sleep, Hunger
                ScoreSlider("Nível de Energia & Disposição", energyScore, { energyScore = it }, "🔋 ${energyScore.toInt()}/10")
                Spacer(modifier = Modifier.height(14.dp))
                ScoreSlider("Qualidade do Sono & Recuperação", sleepScore, { sleepScore = it }, "😴 ${sleepScore.toInt()}/10")
                Spacer(modifier = Modifier.height(14.dp))
                ScoreSlider("Nível de Fome & Apetite", hungerScore, { hungerScore = it }, "🍽️ ${hungerScore.toInt()}/10")

                Spacer(modifier = Modifier.height(16.dp))

                // Joint Pain Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sentiu dores articulares ou desconforto?", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1A1C1E), fontWeight = FontWeight.SemiBold)
                        Text("Diferente do cansaço muscular normal", style = MaterialTheme.typography.labelSmall, color = Color(0xFF74777F))
                    }
                    Switch(
                        checked = hasPain,
                        onCheckedChange = { hasPain = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFEF4444),
                            uncheckedThumbColor = Color(0xFF74777F),
                            uncheckedTrackColor = Color(0xFFE1E2E9)
                        )
                    )
                }

                if (hasPain) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = painLocation,
                        onValueChange = { painLocation = it },
                        label = { Text("Qual região? (ex: ombro direito, lombar)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD97706),
                            unfocusedBorderColor = Color(0xFFE1E2E9),
                            focusedTextColor = Color(0xFF1A1C1E),
                            unfocusedTextColor = Color(0xFF1A1C1E),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Observações gerais da semana") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF10B981),
                        unfocusedBorderColor = Color(0xFFE1E2E9),
                        focusedTextColor = Color(0xFF1A1C1E),
                        unfocusedTextColor = Color(0xFF1A1C1E),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val wt = avgWeight.replace(",", ".").toFloatOrNull() ?: userProfile.currentWeightKg
                        val st = avgSteps.toIntOrNull() ?: 7000
                        onSubmitCheckIn(
                            wt,
                            st,
                            workoutsCompleted,
                            energyScore.toInt(),
                            sleepScore.toInt(),
                            hungerScore.toInt(),
                            hasPain,
                            painLocation,
                            notes
                        )
                        checkInSavedMessage = "Check-in salvo com sucesso! Resumo da semana gerado."
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_checkin_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Salvar Check-in Semanal", fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // History of CheckIns
        Text("Histórico de Check-ins", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
        Spacer(modifier = Modifier.height(12.dp))

        checkIns.forEach { c ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE1E2E9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Semana ${c.weekNumber} — ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(c.dateMillis))}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )
                        Surface(
                            color = Color(0xFFECFDF5),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0))
                        ) {
                            Text(
                                text = "${c.workoutsCompleted}/3 treinos",
                                color = Color(0xFF047857),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Média: ${c.avgWeightKg} kg • ${c.avgSteps} passos/dia • Energia: ${c.energyScore}/10 • Sono: ${c.sleepScore}/10",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF74777F)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = Color(0xFFF8F9FA),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE1E2E9)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\"${c.summaryFeedback}\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF047857),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreSlider(title: String, value: Float, onValueChange: (Float) -> Unit, label: String) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = Color(0xFF44474E), fontWeight = FontWeight.Medium)
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color(0xFF0284C7), fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..10f,
            steps = 8,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF10B981),
                activeTrackColor = Color(0xFF10B981),
                inactiveTrackColor = Color(0xFFE1E2E9)
            )
        )
    }
}
