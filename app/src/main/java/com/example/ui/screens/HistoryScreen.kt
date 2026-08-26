package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.WorkoutSession
import com.example.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    sessions: List<WorkoutSession>
) {
    val completed = sessions.filter { it.status == "CONCLUIDO" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .padding(bottom = 90.dp)
            .testTag("history_screen")
    ) {
        Text(
            text = "Histórico de Treinos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = AppTheme.colors.textPrimary
        )
        Text(
            text = "Registro completo de todas as sessões finalizadas",
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.colors.textMuted
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (completed.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppTheme.colors.surface)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum treino concluído ainda. Vamos começar!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.textMuted
                )
            }
        } else {
            completed.forEach { session ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                    border = BorderStroke(1.dp, AppTheme.colors.border),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(AppTheme.colors.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = session.workoutCode,
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = session.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.textPrimary
                                    )
                                    Text(
                                        text = SimpleDateFormat("dd 'de' MMMM, HH:mm", Locale("pt", "BR")).format(Date(session.dateMillis)),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AppTheme.colors.textMuted
                                    )
                                }
                            }

                            Surface(
                                color = AppTheme.colors.primaryContainer,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "✓ Feito",
                                    color = AppTheme.colors.onPrimaryContainer,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("⏱️ ${session.durationMinutes} min", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.textSecondary, fontWeight = FontWeight.Medium)
                            Text("🏋️ ${session.completedExercisesCount}/${session.totalExercisesCount} exerc.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.textSecondary, fontWeight = FontWeight.Medium)
                            Text("🏃 Cardio: ${session.cardioMinutes} min", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.blueText, fontWeight = FontWeight.Bold)
                        }

                        if (session.notes.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Notas: \"${session.notes}\"",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.colors.textMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

